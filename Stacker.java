/**
 * Stacks boxes of fixed orientation using the Principle of Optimality (All things going well)
 * This goes as follows
 * The Stacker is passed a list of boxes, these boxes have a right way up
 * The list is ordered by the longest side on the base of the box
 * An empty stack is made
 * This stack is placed in a Tree set
 * While there are boxes in the list
 * 	A new stack is formed by placing the largest box of the list on top of the tallest stack
 * 		it fits on. The largest box is removed from the list. Largest is defined as having the
 * 		longest longSide.
 * 	The new stack is added to the Tree set.
 * The while loop ends
 * The tallest stack if taken from the tree
 * This stack is the optimal stack (Again all things going well)
 * 
 * The idea is the optimal stack for the box is a subset of the optimal stack over all if
 * 	the box is in the optimal stack.
 */
public class Stacker{
}
