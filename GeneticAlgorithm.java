/**
 * Implements a genetic algorithm that selects rotations for boxes.
 *
 * It gets passed a list of boxes, and it will create a pool of genes
 * where each gene contains a rotation of boxes.
 *
 * To compare the fitness of each gene, it will run each gene through
 * the box stacker algorithm and sort the genes by the height they
 * produce.
 */

import java.util.ArrayList;
import java.util.Collections;

public class GeneticAlgorithm {
    /*
     * ---------------------------------------------------------------
     *        Algorithm Parameters
     * ---------------------------------------------------------------
     */
    // Fraction of the father to keep
    // child = father*FATHER_RATIO + mother*1-FATHER_RATIO
    static final double FATHER_RATIO = 0.5;

    // Mutation probabilities
    static final double P_swapShort = 0.4;
    static final double P_swapLong = 0.4;

    // Birth mutation probability
    static final double P_birthSwapShort = 0.3;
    static final double P_birthSwapLong = 0.3;

    // Number of genes in a pool
    static final int POOL_SIZE = 3;

    // NOTE: The ratio values should add to POOL_SIZE
    // Number of genes to keep between rounds
    static final int KEEP_RATIO = 2;
    // Number of genes to create through breeding
    static final int BREED_RATIO = 1;
    // Number of genes to create through mutation
    static final int MUTATE_RATIO = 0;


    ArrayList<Box> boxes;

    /**
     * Inner Gene Class
     *
     * Each gene will be represented by this object which will
     * hold the rotated boxes, and the height that they generate.
     */
    private class Gene implements Comparable<Gene> {
	ArrayList<Box> boxes;
	BoxStack stack;

	/**
	 * Create a new gene based on a box collection.
	 */
	public Gene(ArrayList<Box> boxes) {
	    Box tmp;

	    this.boxes = new ArrayList<Box>();

	    for (Box b : boxes) {
		// Clone the box
		tmp = new Box(b.getHeight(),
			      b.getLongSide(),
			      b.getShortSide());

		// Apply mutations
		mutateSwapShort(tmp, P_swapShort);
		mutateSwapLong(tmp, P_swapLong);

		// Save the box into the list
		this.boxes.add(tmp);
	    }

	    this.stack = null;
	}

	/**
	 * Create a new gene based on parents
	 */
	public Gene(Gene father, Gene mother) {
	    Box tmp, tmp2;
	    int N_boxes = father.getBoxes().size();
	    int N_father, index;

	    this.boxes = new ArrayList<Box>();

	    // Make sure the father and the mother are the same size
	    // Maybe this will catch bugs...
	    if (mother.getBoxes().size() != N_boxes)
		throw new RuntimeException("Father and Mother box lists differ in size!");

	    // Take a percentage of the father
	    N_father = (int)(N_boxes * BREED_RATIO);
	    for (index = 0; index < N_father; index++) {
		tmp = father.getBoxes().get(index);
		tmp2 = new Box(tmp.getHeight(),
			       tmp.getLongSide(),
			       tmp.getShortSide());

		// Apply mutations
		mutateSwapShort(tmp2, P_birthSwapShort);
		mutateSwapLong(tmp2, P_birthSwapLong);
		
		this.boxes.add(tmp2);
	    }

	    // Take the rest from the mother
	    for (; index < N_boxes; index++) {
		tmp = mother.getBoxes().get(index);
		tmp2 = new Box(tmp.getHeight(),
			       tmp.getLongSide(),
			       tmp.getShortSide());

		// Apply mutations
		mutateSwapShort(tmp2, P_birthSwapShort);
		mutateSwapLong(tmp2, P_birthSwapLong);
		
		this.boxes.add(tmp2);
	    }

	    this.stack = null;
	}

	private void mutateSwapShort(Box b, double P) {
	    double r = Math.random();

	    if (r < P)
		b.swapShort();
	}

	private void mutateSwapLong(Box b, double P) {
	    double r = Math.random();

	    if (r < P)
		b.swapLong();
	}

	public ArrayList<Box> getBoxes() {
	    return this.boxes;
	}

	public int getHeight() {
	    // If we haven't cached the stack yet, calculate it.
	    if (this.stack == null) {
		Stacker s = new Stacker();
		this.stack = s.stack(this.boxes);
	    }

	    return this.stack.getHeight();
	}

	public BoxStack getBoxStack() {
	    // If we haven't cached the stack yet, calculate it.
	    if (this.stack == null) {
		Stacker s = new Stacker();
		this.stack = s.stack(this.boxes);
	    }

	    return this.stack;
	}

	/**
	 * Compare two genes to see which results in a higher stack
	 */
	@Override
	public int compareTo(Gene g) {
	    // If we haven't cached the stack yet, calculate it.
	    if (this.stack == null) {
		Stacker s = new Stacker();
		this.stack = s.stack(this.boxes);
	    }

	    return Integer.compare(this.stack.getHeight(),
				   g.getBoxStack().getHeight());
	}
    }

    /**
     * Creates a genetic algorithm object.  The constructor just
     * checks the compiled parameters.
     */
    public GeneticAlgorithm() {
	// Check POOL_SIZE
	int ratioSum = KEEP_RATIO + BREED_RATIO + MUTATE_RATIO;
	if (POOL_SIZE != ratioSum) {
	    System.err.println("GA ratios don't sum to POOL_SIZE!");
	    throw new RuntimeException("Invalid configuration.");
	}

	// Check KEEP_RATIO
	if (KEEP_RATIO < 2) {
	    System.err.println("KEEP_RATIO < 2 means no breeding pairs!");
	    throw new RuntimeException("Invalid configuration.");
	}
    }

    /**
     * Finds the highest stack that can be made by a set of boxes.
     *
     * @param boxes The boxes that can be used to form a stack
     * @param maxGenes The maximum number of genes that can be
     *                 generated
     * @returns The best stack that can be made
     */
    public BoxStack FindBest(ArrayList<Box> boxes, int maxGenes) {
	ArrayList<Gene> genePool = new ArrayList<Gene>();
	int poolSize;
	int roundNumber = 0;
	int breedCounter, mutateCounter, index;

	poolSize = Math.min(POOL_SIZE, maxGenes);

	// Create the initial gene pool, subtracting the number of
	// genes we are allowed to create
	for (int i = 0; i < poolSize; i++) {
	    Gene g = new Gene(boxes);
	    genePool.add(g);
	    maxGenes--;
	}

	do {
	    // Sort the list by the stack height (ascending)
	    Collections.sort(genePool);
	    // Reverse the list so that the list is sorted by height
	    // descending.
	    Collections.reverse(genePool);

	    // Create the next pool
	    // Only keep a certain number of elements
	    genePool.subList(KEEP_RATIO, genePool.size()).clear();

	    // Breed some children into the pool
	    // The best will be bred with as many other genes as it
	    // takes, descending.  If we hit the end of the parents,
	    // wrap back around.
	    breedCounter = BREED_RATIO;
	    index = 1;
	    while (maxGenes > 0 && breedCounter > 0) {
		// Breed a gene
		Gene g = new Gene(genePool.get(0),
				  genePool.get(index++));

		// Introduce into the pool
		genePool.add(g);

		// Wrap the index if we've run out of parents
		// (children aren't old enough to breed!)
		if (index >= KEEP_RATIO)
		    index = 1;

		// Update the counters
		breedCounter--;
		maxGenes--;
	    }

	    // Create some mutants
	    mutateCounter = MUTATE_RATIO;
	    index = 0;
	    while (maxGenes > 0 && mutateCounter > 0) {
		// Mutate a gene
		Gene g = new Gene(genePool.get(index++).getBoxes());

		// Introduce into the pool
		genePool.add(g);

		// Wrap the index if we've run out of original genes
		// (Children shouldn't be mutants!)
		if (index >= KEEP_RATIO)
		    index = 0;

		// Update the counters
		mutateCounter--;
		maxGenes--;
	    }
	} while (maxGenes > 0);

	// Sort the pool one last time, and return the best stack
	Collections.sort(genePool);
	Collections.reverse(genePool);

	return genePool.get(0).getBoxStack();
    }
}