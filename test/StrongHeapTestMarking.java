import org.junit.Test;

import static org.junit.Assert.*;

public class StrongHeapTestMarking {
    public static <E> BinaryTree<E> tree(BinaryTree<E> left, E value, BinaryTree<E> right) {
        return new BinaryTree<>(value, left, right);
    }

    public static <E> BinaryTree<E> left(BinaryTree<E> left, E value) {
        return new BinaryTree<>(value, left, null);
    }

    public static <E> BinaryTree<E> right(E value, BinaryTree<E> right) {
        return new BinaryTree<>(value, null, right);
    }

    public static <E> BinaryTree<E> leaf(E value) {
        return new BinaryTree<>(value, null, null);
    }


    @Test
    public void testSingleNode() {
        assertTrue(StrongHeap.isStrongHeap(leaf(1)));
    }

    @Test
    public void testExamples() {
        assertTrue(StrongHeap.isStrongHeap(
                tree(tree(leaf(4), 5, leaf(3)), 10, leaf(6))
        ));
        assertFalse(StrongHeap.isStrongHeap(
                tree(tree(leaf(5), 5, leaf(3)), 10, leaf(6))
        ));
        assertFalse(StrongHeap.isStrongHeap(
                tree(right(5, leaf(3)), 10, leaf(6))
        ));
    }

    @Test
    public void testTwoNodesTrue() {
        assertTrue(StrongHeap.isStrongHeap(
                left(leaf(0), 10)));
    }

    @Test
    public void testTwoNodesFalse() {
        assertFalse(StrongHeap.isStrongHeap(
                left(leaf(100), 10)
        ));
    }

    @Test
    public void testNegatives() {
        assertTrue(StrongHeap.isStrongHeap(
                left(leaf(-100), 0)
        ));
    }

    @Test
    public void testThreeNodesTrue() {
        assertTrue(StrongHeap.isStrongHeap(
                tree(leaf(10), 100, leaf(99))
        ));
    }

    @Test
    public void testThreeNodesFalse() {
        // 100 not < 100
        assertFalse(StrongHeap.isStrongHeap(
                tree(leaf(100), 100, leaf(99))
        ));
    }

    @Test
    public void testFourNodesTrue() {
        // 0 + 99 < 100
        assertTrue(StrongHeap.isStrongHeap(
                tree(left(leaf(0), 99), 100, leaf(99))
        ));
    }

    @Test
    public void testFourNodesFalse() {
        // 98 + 99 not < 100
        assertFalse(StrongHeap.isStrongHeap(
                tree(left(leaf(98), 99), 100, leaf(99))
        ));
    }

    @Test
    public void testNonCompleteFalse() {
        // not a complete tree
        assertFalse(StrongHeap.isStrongHeap(
                right(100, leaf(0))
        ));
    }

    // ADDITIONAL MARKING TESTS

    // to be used as part of a larger test to guard against return true/false solutions.
    private void assertNonTrivial() {
        assertTrue("failed trivial true check", StrongHeap.isStrongHeap(
                left(leaf(0), 10)
        ));
        assertFalse("failed trivial false check", StrongHeap.isStrongHeap(
                left(leaf(100), 10)
        ));
    }

    @Test
    public void testNonTrivial() {
        assertNonTrivial();
    }

    @Test
    public void testMediumTrue_1() {
        assertNonTrivial();
        Integer[] items = new Integer[] {
                100,
                50, 50,
                49, 49,  49, 49,
        };
        assertTrue(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testMediumTrue_2() {
        assertNonTrivial();
        Integer[] items = new Integer[] {
                100,
                50, 50,
                49, 49,  49, 49,
                0, 0
        };
        assertTrue(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testMediumFalse_1() {
        assertNonTrivial();
        Integer[] items = new Integer[] {
                100,
                50, 50,
                49, 49,  49, 49,
                0, 1
        };
        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testMediumFalse_2() {
        assertNonTrivial();
        Integer[] items = new Integer[] {
                100,
                50, 50,
                49, 49,  49, 50,
                0, 0
        };
        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testMediumFalse_3() {
        assertNonTrivial();
        Integer[] items = new Integer[] {
                100,
                50, 100,
                49, 49,  0, 0,
                0, 1
        };
        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testMediumFalse_4() {
        assertNonTrivial();
        Integer[] items = new Integer[] {
                100,
                50, 50,
                50, 49,  0, 0,
                0, 1
        };
        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testIncomplete_1() {
        assertNonTrivial();

        BinaryTree<Integer> tree = tree(leaf(10), 100, right(10, leaf(0)));
        assertFalse(StrongHeap.isStrongHeap(tree));
    }

    @Test
    public void testIncomplete_2() {
        assertNonTrivial();

        Integer[] items = new Integer[] {
                100,
                50, 50,
                49, 49,  49, 49,
        };
        BinaryTree<Integer>[] nodes = Helpers.makeCompleteTreeArray(items);
        nodes[nodes.length - 1].setLeft(leaf(0));

        assertFalse(StrongHeap.isStrongHeap(nodes[0]));
    }

    @Test
    public void testIncomplete_3() {
        assertNonTrivial();

        Integer[] items = new Integer[] {
                100,
                50, 50,
                49, 49,  49, 49,
        };
        BinaryTree<Integer>[] nodes = Helpers.makeCompleteTreeArray(items);
        nodes[3].setRight(leaf(0));

        assertFalse(StrongHeap.isStrongHeap(nodes[0]));
    }

    @Test
    public void testIncomplete_4() {
        assertNonTrivial();

        Integer[] items = new Integer[] {
                100,
                50, 50,
                49, 49,  49,
        };
        BinaryTree<Integer>[] nodes = Helpers.makeCompleteTreeArray(items);
        nodes[5].setRight(leaf(0));

        assertFalse(StrongHeap.isStrongHeap(nodes[0]));
    }

    @Test
    public void testIncomplete_5() {
        assertNonTrivial();

        Integer[] items = new Integer[] {
                100,
                50, 50,
                49, 49,  49,
        };
        BinaryTree<Integer>[] nodes = Helpers.makeCompleteTreeArray(items);
        nodes[3].setLeft(leaf(0));

        assertFalse(StrongHeap.isStrongHeap(nodes[0]));
    }

    @Test
    public void testIncomplete_6() {
        assertNonTrivial();

        Integer[] items = new Integer[] {
                100,
                50, 50,
                49, 49,  49, 49,
        };
        BinaryTree<Integer>[] nodes = Helpers.makeCompleteTreeArray(items);
        nodes[3].setRight(leaf(0));
        nodes[4].setLeft(leaf(0));

        assertFalse(StrongHeap.isStrongHeap(nodes[0]));
    }

    @Test
    public void testBigTrue_1() {
        assertNonTrivial();

        Integer[] items = new Integer[] {
                1000,
                500, 500,
                250, 250,  250, 250,
                120, 120, 120, 120, 120, 120, 120, 120
        };
        assertTrue(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testBigTrue_2() {
        assertNonTrivial();

        Integer[] items = new Integer[] {
                1000,
                500, 500,
                250, 250,  250, 250,
                120, 120, 120, 120, 120, 120, 120, 120
        };
        assertTrue(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testBigTrue_Edge() {
        assertNonTrivial();

        Integer[] items = new Integer[] {
                1000,
                500, 500,
                499, 499,  499, 499,
                0, 0, 0, 0,
        };
        assertTrue(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testStrongHeap_True_1000() {
        assertNonTrivial();
        assertTrue(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(Helpers.makeStrongHeapArray(1000))));
    }

    @Test
    public void testStrongHeap_True_2000() {
        assertNonTrivial();
        assertTrue(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(Helpers.makeStrongHeapArray(2000))));
    }

    @Test
    public void testStrongHeap_True_5000() {
        assertNonTrivial();
        assertTrue(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(Helpers.makeStrongHeapArray(5000))));
    }

    @Test
    public void testStrongHeap_True_10000() {
        assertNonTrivial();
        assertTrue(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(Helpers.makeStrongHeapArray(10000))));
    }

    private static void breakHeap(Integer[] items, int pos) {
        // breaks child + parent < grandparent
        items[(pos - 1) / 2] += 1;
        items[pos] *= 2;
    }

    @Test
    public void testStrongHeap_False_1000() {
        assertNonTrivial();

        Integer[] items = Helpers.makeStrongHeapArray(1000);
        breakHeap(items, 501);
        
        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testStrongHeap_False_2000() {
        assertNonTrivial();

        Integer[] items = Helpers.makeStrongHeapArray(2000);
        breakHeap(items, 321);
        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testStrongHeap_False_5000() {
        assertNonTrivial();

        Integer[] items = Helpers.makeStrongHeapArray(5000);
        breakHeap(items, 4221);

        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testStrongHeap_False_10000() {
        assertNonTrivial();

        Integer[] items = Helpers.makeStrongHeapArray(10000);
        breakHeap(items, 7777);

        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testStrongHeap_Right_1() {
        Integer[] items = new Integer[] {
                100,
                50, 50,
                49, 49, 49, 49,
                0, 0, 0, 0,  0, 0, 0, 1
        };
        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testStrongHeap_Right_2() {
        Integer[] items = new Integer[] {
                200,
                50, 52,
                49, 49, 49, 50,
                0, 0, 0, 0,  0, 0, 0, 2
        };
        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testStrongHeap_Right_3() {
        Integer[] items = new Integer[] {
                200,
                50, 100,
                49, 49, 49, 50,
                0, 0, 0, 1,  0, 0, 0, 1
        };
        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testStrongHeap_Right_4() {
        Integer[] items = new Integer[] {
                1000,
                500, 500,
                250, 400,  250, 250,
                120, 120, 0, 120,  120, 120, 120, 120
        };
        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testStrongHeap_Right_5() {
        Integer[] items = new Integer[] {
                1000,
                500, 500,
                250, 250,  250, 250,
                120, 120, 120, 120,  120, 250, 120, 120
        };
        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testStrongHeap_Right_6() {
        Integer[] items = new Integer[] {
                1000,
                500, 999,
                250, 250,  250, 250,
                120, 120, 120, 120,  120, 250, 120, 120
        };
        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

    @Test
    public void testStrongHeap_Right_7() {
        Integer[] items = new Integer[] {
                1000,
                500, 500,
                251, 250,  250, 250,
                120, 249, 120, 120,  120, 250, 120, 120
        };
        assertFalse(StrongHeap.isStrongHeap(Helpers.makeCompleteTree(items)));
    }

}