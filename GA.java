import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * A genetic algorithm to select the orientation of boxes
 */
public class GA{
	static final double CROSSOVER = 0.75;
	static final double MUTATION = 0.02;
	static final double POPULATION = 0.05;
	static final double REJECT = 0.75;

	static final Stacker STACKER = new Stacker();
	static final Random RAND = new Random();
	static List<Box> boxes;
	// ONLY EVER USE GENES WITH A FIXED SIZE WHILE THIS EXISTS
	static int size;

	/**
	 * A gene
	 * Has all the information to make a stack and a fitness
	 */
	private class Gene implements Comparable{
		BitSet gene;
		Integer fitness;

		public Gene(int s){
			size = s;
			gene = new BitSet(size*2);
			for (int i = 0; i < size * 2; i += 2){
				double rand = RAND.nextDouble();
				int value = (int)(rand * 3);
				if (value == 1){
					gene.set(i);
				} else if (value == 2){
					gene.set(i+1);
				}
				
			}
		}

		/*
		 * The crossover constructor
		 */
		public Gene(Gene p1, Gene p2){
			BitSet g1 = p1.getGene();
			BitSet g2 = p2.getGene();
			BitSet g3 = new BitSet(size * 2);
			if (g1.size() != g2.size()){
				throw new RuntimeException("Insane!");
			}
			double rand = RAND.nextDouble();
			int cross = (int)(rand * size);
			cross *= 2;
			g1.clear(cross, size*2);
			g2.clear(0, cross);
			g3.or(g1);
			g3.or(g2);
			mutateGene(g3);
		}

		/*
		 * The mutate constructor
		 * Unfortunately the bit pattern 11 is nothing and must be avoided.
		 * Using it would remove boxes or bias the genes
		 */
		public void mutateGene(BitSet g){
			for (int i = 0; i < size * 2; i +=2){
				double rand = RAND.nextDouble();
				if (rand < MUTATION){
					rand = RAND.nextDouble();
					int value = (int)(rand * 3);
					if (value == 0){
						g.clear(i, i+2);
					} else if (value == 1) {
						g.set(i);
						g.clear(i+1);
					} else {
						g.clear(i);
						g.set(i+1);
					}
				}
			}
			gene = g;
		}

		public int getFitness(){
			if (fitness != null){
				return fitness;
			}
			setBoxes();
			fitness = STACKER.stack(boxes).getHeight();
			return fitness;
		}	

		public void setBoxes(){
			Iterator<Box> boxIt = boxes.iterator();
			for (int i = 0; i < size * 2; i += 2){
				int rotation = gene.get(i, i+2).length() == 0 ? 0 : (int)gene.get(i, i+2).toLongArray()[0];
				boxIt.next().rotate(rotation);
			}
		}

		/*
		 * The other mutate constructor
		 */
		public Gene(Gene g){
			mutateGene(g.getGene());
		}

		public BitSet getGene(){
			return (BitSet)gene.clone();
		}

		/*
		 * Compares genes
		 */
		@Override
		public int compareTo(Object o){
			Gene g = (Gene)o;
			return Integer.compare(this.getFitness(), g.getFitness());
		}
	}

	public BoxStack optimise(List<Box> boxes, int remainingGenes){
		GA.boxes = boxes;
		int population = (int)Math.ceil(remainingGenes*POPULATION);
		ArrayList<Gene> genes = new ArrayList<Gene>(population);
		while (genes.size() < population){
			genes.add(new Gene(boxes.size()));
			remainingGenes--;
		}
		while (remainingGenes > 0){
			int reject = (int)(genes.size()*REJECT);
			reject = genes.size() - reject;
			Collections.sort(genes, Collections.reverseOrder());
			genes.subList(reject, genes.size()).clear();

			int size = genes.size() - 1;
			while (remainingGenes > 0 && genes.size() < population){
				double rand = RAND.nextDouble();
				if (rand < CROSSOVER){
					int mother = (int)(RAND.nextDouble()*size);
					int father = (int)(RAND.nextDouble()*size);
					genes.add(new Gene(genes.get(mother), genes.get(father)));
				} else {
					int mutant = (int)(RAND.nextDouble()*size);
					genes.add(new Gene(genes.get(mutant)));
				}
				remainingGenes--;
			}
		}
		Collections.sort(genes, Collections.reverseOrder());
		genes.get(0).setBoxes();
		return STACKER.stack(boxes);
	}
}
