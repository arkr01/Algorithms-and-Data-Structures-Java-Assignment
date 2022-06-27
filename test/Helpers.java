import java.util.Arrays;

public class Helpers {
    // TREE HELPERS

    public static <E> BinaryTree<E> makeCompleteTree(E[] items) {
        return makeCompleteTreeArray(items)[0];
    }

    public static <E> BinaryTree<E>[] makeCompleteTreeArray(E[] items) {
        BinaryTree<E>[] nodes = new BinaryTree[items.length];
        nodes[0] = new BinaryTree<>(items[0]);
        for (int i = 1; i < items.length; i++) {
            int parent = (i - 1) / 2;

            if (items[i] != null) {
                nodes[i] = new BinaryTree<>(items[i]);

                if (nodes[parent] == null)
                    throw new RuntimeException("non-null child at " + i + " has null parent at " + parent);

                if (i == 2 * parent + 1)
                    nodes[parent].setLeft(nodes[i]);
                else
                    nodes[parent].setRight(nodes[i]);
            }
        }

        return nodes;
    }

    // STRONG HEAP THINGS

    public static Integer[] makeStrongHeapArray(int size) {
        Integer[] items = new Integer[size];
        int i = 0;
        int levelSize = 1;
        int num = Integer.MAX_VALUE / 2 + 1;

        outer:
        while (i < size) {
            for (int j = 0; j < levelSize; j++) {
                if (i >= size)
                    break outer;
                items[i] = num;
                i++;
            }
            levelSize *= 2;
            num /= 2;
            if (num == 0)
                throw new RuntimeException("size exceeds makeStrongHeap capabilities");
        }
//        System.out.println(Arrays.toString(items));
        return (items);
    }

    // QUATERNARY HEAPSORT THINGS

    private static final int K = 4;

    public static <T extends Comparable<T>> T[] makeDownheapCopy(T[] input, int start, int size) {
        T[] result = Arrays.copyOf(input, input.length);

        int current = start;
        while (true) {
//            System.out.println(current);
            T maxValue = result[current];
            int maxChild = current;

            // check if any children are larger than this current node.
            for (int i = 1; i <= K; i++) {
                int child = current * K + i;
                if (child >= size) break;
                if (result[child].compareTo(maxValue) > 0) {
                    maxValue = result[child];
                    maxChild = child;
                }
            }

            // no child larger. heap property holds, return.
            if (maxChild == current) {
                break;
            }

            // a child is larger. swap down.
            result[maxChild] = result[current];
            result[current] = maxValue;
            current = maxChild;
        }
        return result;
    }

    public static Integer[] makeHeap(int size) {
        Integer[] output = new Integer[size];
        for (int i = 0; i < output.length; i++) {
            output[i] = output.length - i;
        }
        return output;
    }

    public static Integer[] makeArray(int size) {
        Integer[] output = new Integer[size];
        for (int i = 0; i < output.length; i++) {
            output[i] = i;
        }
        output[0] = 42;
        return output;
    }

    public static <T extends Comparable<T>> T[] makeSortedCopy(T[] input) {
        T[] copy = Arrays.copyOf(input, input.length);
        Arrays.sort(copy);
        return copy;
    }
}
