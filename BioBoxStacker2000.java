/**
 * Bio Box Staker 2000
 *
 * Stacking boxes can be difficult, and computer scientists are lazy.
 * We wrote this program to accept a set of boxes from a file, and
 * find the largest stack possible.  The rule is that any box on top
 * of another box must have a smaller width and depth than the box
 * below.
 *
 * Dan Collins 1183446
 * Severin Mahoney-Marsh 1181754
 * COMP317-14A
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class BioBoxStacker2000 {
    public static void main(String[] args) {
	BufferedReader inFile;
	String inLine;
	String[] data;
	ArrayList<Box> boxes = new ArrayList<Box>();
	int N;
	GeneticAlgorithm ga = new GeneticAlgorithm();
	BoxStack best;

	// Test input argument
	if (args.length != 1) {
	    System.err.println("Invalid input arguments!");
	    System.err.println("You must specify an input file!");
	    return;
	}

	// Open the input file
	try {
	    inFile = new BufferedReader(new FileReader(args[0]));
	} catch (Exception e) {
	    System.err.printf("Failed to open input file: %s\n",
			      args[0]);
	    e.printStackTrace();
	    return;
	}

	// Load all of the boxes from the file
	try {
	    while ((inLine = inFile.readLine()) != null) {
		// Get the box dimensions
		data = inLine.split(" ");
	    
		// Ensure they're valid
		// This happens if the last line is blank, so it's
		// no big deal, we'll just skip it.
		if (data.length != 3)
		    continue;

		// Add the box to the list
		boxes.add(new Box(Integer.parseInt(data[0]),
				  Integer.parseInt(data[1]),
				  Integer.parseInt(data[2])));
	    }
	} catch(Exception e) {
	    System.err.println("Failed to read line from file.");
	    e.printStackTrace();
	}

	System.out.printf("There are %d boxes to stack\n", boxes.size());

	// Calculate how many genes we can create
	N = 3 * boxes.size();
	N *= N;

	System.out.printf("We can create %d genes to find a solution\n", N);

	// Run the GA
	best = ga.FindBest(boxes, N);

	System.out.println(best.toString());
    }
}