public class QuaternaryHeapsort {

    /**
     * Sorts the input array, in-place, using a quaternary heap sort.
     *
     * Let n denote the number of nodes within the quaternary heap.
     *
     * Worst case runtime complexity: O(n log n)
     *
     * Worst case space complexity: O(n)
     *
     * @param input to be sorted (modified in place)
     */
    public static <T extends Comparable<T>> void quaternaryHeapsort(T[] input) {
        // Bottom-Up Heap Construction - start at last internal node
        for (int position = input.length / 4 - 1; position >= 0; position--) {
            quaternaryDownheap(input, position, input.length);
        }
        quaternaryHeapsort(input, input.length);
    }

    /**
     * Worst case runtime complexity: O(n log n)
     *
     * Worst case space complexity: O(n)
     *
     * @param input heap in array form
     * @param size number of nodes to focus on
     * @param <T> type of each node
     */
    private static <T extends Comparable<T>> void quaternaryHeapsort(T[] input, int size) {
        // Base case
        if (size == 0) {
            return;
        }
        quaternaryDownheap(input, 0, size);

        // Swap root with last node
        T toSwap = input[size - 1];
        input[size - 1] = input[0];
        input[0] = toSwap;

        // 'Remove last node' by decrementing size, and perform down heap to restore max heap order
        quaternaryHeapsort(input, size - 1);
    }

    /**
     * Performs a downheap from the element in the given position on the given max heap array.
     *
     * A downheap should restore the heap order by swapping downwards as necessary.
     * The array should be modified in place.
     * 
     * You should only consider elements in the input array from index 0 to index (size - 1)
     * as part of the heap (i.e. pretend the input array stops after the inputted size).
     *
     * Worst case runtime complexity: O(log n)
     *
     * Worst case space complexity: O(n)
     *
     * @param input array representing a quaternary max heap.
     * @param start position in the array to start the downheap from.
     * @param size the size of the heap in the input array, starting from index 0
     */
    public static <T extends Comparable<T>> void quaternaryDownheap(T[] input, int start,
            int size) {
        int currentParent = start;
        while (hasFarLeft(currentParent, size)) {
            int largestChild = findLargestChild(input, currentParent, size);

            // Downheap complete
            if ((input[largestChild]).compareTo(input[currentParent]) <= 0) {
                break;
            }

            // Swap to downheap
            T toSwap = input[largestChild];
            input[largestChild] = input[currentParent];
            input[currentParent] = toSwap;
            currentParent = largestChild;
        }
    }

    /**
     * Worst case runtime complexity: O(1)
     *
     * Worst case space complexity: O(n)
     *
     * @param input
     * @param currentParent
     * @param size
     * @param <T>
     * @return
     */
    private static <T extends Comparable<T>> int findLargestChild(T[] input, int currentParent,
            int size) {
        int farLeftPosition = farLeft(currentParent);
        int largestChild = farLeftPosition;

        if (hasMidLeft(currentParent, size)) {
            int midLeftPosition = midLeft(currentParent);
            if ((input[largestChild]).compareTo(input[midLeftPosition]) < 0) {
                largestChild = midLeftPosition;
            }

            if (hasMidRight(currentParent, size)) {
                int midRightPosition = midRight(currentParent);
                if ((input[largestChild]).compareTo(input[midRightPosition]) < 0) {
                    largestChild = midRightPosition;
                }

                if (hasFarRight(currentParent, size)) {
                    int farRightPosition = farRight(currentParent);
                    if ((input[largestChild]).compareTo(input[farRightPosition]) < 0) {
                        largestChild = farRightPosition;
                    }
                }
            }
        }
        return largestChild;
    }

    /**
     * Worst case runtime complexity: O(1)
     *
     * Worst case space complexity: O(1)
     *
     * @param parentPosition
     * @param size
     * @return
     */
    private static boolean hasFarLeft(int parentPosition, int size) {
        return farLeft(parentPosition) < size;
    }

    /**
     * Worst case runtime complexity: O(1)
     *
     * Worst case space complexity: O(1)
     *
     * @param parentPosition
     * @param size
     * @return
     */
    private static boolean hasMidLeft(int parentPosition, int size) {
        return midLeft(parentPosition) < size;
    }

    /**
     * Worst case runtime complexity: O(1)
     *
     * Worst case space complexity: O(1)
     *
     * @param parentPosition
     * @param size
     * @return
     */
    private static boolean hasMidRight(int parentPosition, int size) {
        return midRight(parentPosition) < size;
    }

    /**
     * Worst case runtime complexity: O(1)
     *
     * Worst case space complexity: O(1)
     *
     * @param parentPosition
     * @param size
     * @return
     */
    private static boolean hasFarRight(int parentPosition, int size) {
        return farRight(parentPosition) < size;
    }

    /**
     * Worst case runtime complexity: O(1)
     *
     * Worst case space complexity: O(1)
     *
     * @param parentIndex
     * @return
     */
    private static int farLeft(int parentIndex) {
        return 4 * parentIndex + 1;
    }

    /**
     * Worst case runtime complexity: O(1)
     *
     * Worst case space complexity: O(1)
     *
     * @param parentIndex
     * @return
     */
    private static int midLeft(int parentIndex) {
        return 4 * parentIndex + 2;
    }

    /**
     * Worst case runtime complexity: O(1)
     *
     * Worst case space complexity: O(1)
     *
     * @param parentIndex
     * @return
     */
    private static int midRight(int parentIndex) {
        return 4 * parentIndex + 3;
    }

    /**
     * Worst case runtime complexity: O(1)
     *
     * Worst case space complexity: O(1)
     *
     * @param parentIndex
     * @return
     */
    private static int farRight(int parentIndex) {
        return 4 * parentIndex + 4;
    }
}
