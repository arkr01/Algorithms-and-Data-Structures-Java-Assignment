import java.util.Comparator;

/**
 * A comparator for Binary Trees.
 */
public class BinaryTreeComparator<E extends Comparable<E>> implements Comparator<BinaryTree<E>> {

    /**
     * Compares two binary trees with the given root nodes.
     *
     * Two nodes are compared by their left children, their values, then their right children,
     * in that order. A null is less than a non-null, and equal to another null.
     *
     * As we search for the first unequal comparison between nodes, the worst case would be a
     * comparison between identical trees.
     *
     * In this situation, both trees would have the same number of nodes. Let this quantity be n.
     *
     * Thus, there would be at most n recursive calls of this method. Then, as each method call
     * simply contains constant time operations such as if statements and return statements, it
     * follows that this method is bounded by O(n) runtime complexity.
     *
     * Now, as each stack frame stores constant integers, boolean variables, and two tree nodes,
     * each of which simply store a single generic element and two references to (possibly null)
     * child nodes, it follows that each recursive method call is bounded by constant memory
     * complexity.
     *
     * As there are at most n recursive calls, this method is bounded by O(n) space complexity.
     *
     * @param tree1 root of the first binary tree, may be null.
     * @param tree2 root of the second binary tree, may be null.
     * @return -1, 0, +1 if tree1 is less than, equal to, or greater than tree2, respectively.
     */
    @Override
    public int compare(BinaryTree<E> tree1, BinaryTree<E> tree2) {
        final int LESS_THAN = -1;
        final int EQUAL = 0;
        final int GREATER_THAN = 1;
        if (tree1 == null) {
            return (tree2 == null) ? EQUAL : LESS_THAN;
        }
        if (tree2 == null) {
            return GREATER_THAN; // tree1 is not null, otherwise method would have returned already
        }
        int result = compare(tree1.getLeft(), tree2.getLeft());

        // If left subtree presents no unequal comparison, then check this node
        if (result == EQUAL) {
            // If this node presents no unequal comparison, then check the right subtree
            if ((result = tree1.getValue().compareTo(tree2.getValue())) == EQUAL) {
                return compare(tree1.getRight(), tree2.getRight());
            }
        }
        return result;
    }
}
