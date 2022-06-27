public class StrongHeap {
    /**
     * Determines whether the binary tree with the given root node is
     * a "strong binary heap", as described in the assignment task sheet.
     *
     * A strong binary heap is a binary tree which is:
     *  - a complete binary tree, AND
     *  - its values satisfy the strong heap property.
     *
     * Let n denote the number of nodes in the given binary tree.
     *
     * As this method simply calls and compares the return values of two methods (in succession)
     * that are both bounded by O(n) runtime and space complexity, we may say this method is also
     * bounded by O(n) runtime and space complexity.
     *
     * @param root root of a binary tree, cannot be null.
     * @return true if the tree is a strong heap, otherwise false.
     */
    public static boolean isStrongHeap(BinaryTree<Integer> root) {
        return isCompleteBinaryTree(root) && satisfiesStrongMaxHeapProperty(root, null);
    }

    /**
     * Checks and returns if the tree with the given root node, is a complete binary tree.
     *
     * Let n denote the number of nodes in the given binary tree.
     *
     * In the worst case, each node would have to be visited at least once. Hence, there may be
     * upto n recursive calls of this function. As only if statements, calls to methods bounded by
     * constant runtime complexity, and return statements are present, we may say that each
     * recursive call is bounded by constant runtime complexity. Hence, this method is bounded by
     * O(n) runtime complexity.
     *
     * Regarding space complexity, in the worst case, each stack frame would have to store a
     * single root node. As each node only stores a single element of some generic type, and two
     * references to possibly null child nodes, we may say that each stack frame is bounded by
     * constant space complexity. Hence, we may say that this method is bounded by O(n) space
     * complexity.
     *
     * @param root root of a binary tree, cannot be null.
     * @return true if the tree is a complete binary tree, otherwise false.
     */
    private static boolean isCompleteBinaryTree(BinaryTree<Integer> root) {
        if (root.isLeaf()) {
            return true; // Base case
        }

        // Ensures internal level nodes are full and leaf nodes are as left as possible
        if (root.getLeft() == null || (root.getRight() == null && !root.getLeft().isLeaf())) {
            return false;
        }

        // root can never be null, hence on each recursive call, must ensure root.getRight() != null
        return (root.getRight() == null) ? isStrongHeap(root.getLeft()) :
                isStrongHeap(root.getLeft()) && isStrongHeap(root.getRight());
    }

    /**
     * Recursively examines a node and its (possibly null) children to check the strong max heap
     * property.
     *
     * Let n denote the number of nodes within the given binary tree.
     *
     * In the worst case, each node would have to be visited at least once. Hence, there may
     * be upto n recursive calls of this function. As only if statements, calls to methods
     * bounded by constant runtime complexity, and return statements are present, we may say that
     * each recursive call is bounded by constant runtime complexity. Hence, this method is
     * bounded by O(n) runtime complexity.
     *
     * Regarding space complexity, in the worst case, each stack frame would have to store a
     * single root node, a single Integer, and a boolean variable (maxHeapProperty). As each node
     * only stores a single element of some generic type, and two references to possibly null child
     * nodes, we may say that each stack frame is bounded by constant space complexity. Hence, we
     * may say that this method is bounded by O(n) space complexity.
     *
     * @param current The current node to be examined
     * @param parentValue The value of the parent node (null if the root is checked)
     * @require the given binary tree is complete
     * @return true if the given (complete) tree satisfies the strong max heap property, false
     * otherwise
     */
    private static boolean satisfiesStrongMaxHeapProperty(BinaryTree<Integer> current,
            Integer parentValue) {
        if (current.isLeaf()) {
            return true; // Base case
        }

        // Complete tree properties may now be applied in checking strong (max) heap property
        if (current.getRight() == null) {
            // Already confirmed current is not a leaf, hence left child must exist. Also,
            // left child must be a leaf by completeness if right child does not exist
            boolean maxHeapProperty = current.getLeft().getValue() < current.getValue();
            return (parentValue == null) ? maxHeapProperty :
                    current.getLeft().getValue() + current.getValue() < parentValue;
        }
        if (current.getRight().getValue() >= current.getValue() || current.getLeft().getValue() >=
                current.getValue()) {
            return false;
        }

        // i.e. if current is not the root
        if (parentValue != null) {
            if (current.getRight().getValue() + current.getValue() >= parentValue ||
                    current.getLeft().getValue() + current.getValue() >= parentValue) {
                return false;
            }
        }
        return satisfiesStrongMaxHeapProperty(current.getLeft(), current.getValue()) &&
                satisfiesStrongMaxHeapProperty(current.getRight(), current.getValue());
    }
}
