import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * LinkedMultiHashSet is an implementation of a (@see MultiSet), using a hashtable as the internal
 * data structure, and with predictable iteration order based on the insertion order
 * of elements.
 *
 * Its iterator orders elements according to when the first occurrence of the element
 * was added. When the multiset contains multiple instances of an element, those instances
 * are consecutive in the iteration order. If all occurrences of an element are removed,
 * after which that element is added to the multiset, the element will appear at the end of the
 * iteration.
 *
 * The internal hashtable array should be doubled in size after an add that would cause it to be
 * at full capacity. The internal capacity should never decrease.
 *
 * Collision handling for elements with the same hashcode (i.e. with hashCode()) should be done
 * using linear probing, as described in lectures.
 *
 * @param <T> type of elements in the set
 */
public class LinkedMultiHashSet<T> implements MultiSet<T>, Iterable<T> {
    /**
     * Represents the values of the LinkedMultiHashSet. Stores the element, along with its
     * relevant information such as the number of occurrences, whether it has been deleted, as
     * well as references to the next, and previous values (in order of the earliest added).
     *
     * Forms a doubly LinkedList representation. Each node is bounded by constant memory complexity.
     *
     * @param <T> The type of element stored
     */
    private class Value<T> {
        /** Represents the element stored. */
        T element;

        /** Represents the number of occurrences of the element within the LinkedMultiHashSet. */
        int count;

        /** Flag for if the element is deleted from the LinkedMultiHashSet - used for linear
         * probing. */
        boolean isDeleted;

        /** Reference to the next Value in the LinkedList. */
        Value<T> next;

        /** Reference to the previous Value in the LinkedList. */
        Value<T> previous;

        /**
         * Forms a new Value node.
         * @param element element to be stored
         * @param count initial number of occurrences of said element to be stored
         */
        private Value(T element, int count) {
            this.element = element;
            this.count = count;
            this.next = this.previous = null;
            this.isDeleted = false;
        }
    }

    /** The head of the LinkedList */
    private Value<T> head;

    /** The tail of the LinkedList */
    private Value<T> tail;

    /** The resizeable hash table that stores all Values. */
    private Value<T>[] hashTable;

    /** The distinct number of elements. */
    private int distinctNumElements;

    /** The total number of elements (i.e. including duplicates). */
    private int totalNumElements;

    /**
     * Creates a new LinkedMultiHashSet with the given initialCapacity.
     *
     * @param initialCapacity the initial size of the LinkedMultiHashSet (i.e. the number of
     *                        distinct elements able to be stored).
     */
    public LinkedMultiHashSet(int initialCapacity) {
        this.hashTable = (Value<T>[]) new Value[initialCapacity];
        this.distinctNumElements = this.totalNumElements = 0;
        this.head = this.tail = null;
    }

    /**
     * Adds the element to the set. If an equal element is already in the set,
     * increases its occurrence count by 1.
     *
     * Let n denote the length of the hash table.
     *
     * As the overloaded add() method is bounded by O(n) runtime complexity and O(n) space
     * complexity in the worst case, and O(1) amortised runtime complexity as a result, so is
     * this method.
     *
     * @param element to add
     * @require element != null
     */
    @Override
    public void add(T element) {
        this.add(element, 1);
    }

    /**
     * Adds count to the number of occurrences of the element in set.
     *
     * Let n denote the length of the hash table.
     *
     * In the worst case, the element to be added would require linear probing of the entire hash
     * table. Hence, we may say it is bounded by O(n) runtime complexity, as all other operations
     * are merely either constant, or calls to other methods bounded by O(n) runtime complexity.
     * Thus O(1) amortised time.
     *
     * Observe that this method simply stores the new element to be added, and its index. In the
     * worst case, the hash table will have the be doubled in size. As a result, we may say this
     * method is bounded by O(n) space complexity.
     *
     * @param element to add
     * @require element != null && count >= 0
     */
    @Override
    public void add(T element, int count) {
        if (count > 0) {
            int index;
            if (this.contains(element)) {
                index = hashAndProbe(element, this.internalCapacity(), true, this.hashTable);
                (this.hashTable[index]).count += count;
            } else {
                index = hashAndProbe(element, this.internalCapacity(), false, this.hashTable);
                Value toAdd = new Value<T>(element, count);
                this.hashTable[index] = toAdd;
                if (this.distinctCount() == 0) {
                    this.head = this.tail = toAdd;
                } else {
                    this.tail.next = toAdd;
                    toAdd.previous = this.tail;
                    this.tail = toAdd;
                }
                this.distinctNumElements++;
            }
            this.totalNumElements += count;
            if (this.distinctCount() == this.internalCapacity()) {
                this.resize();
            }
        }
    }

    /**
     * Let n denote the length of the resized hash table.
     *
     * Doubling the length of the hash table would lead to O(n) memory complexity, and O(n)
     * runtime complexity, in the worst case (as we must iterate over each element of the
     * original array, re-hash with an assumed O(1) hash function, compress, and linearly probe the
     * new array). Thus, O(1) amortised runtime complexity.
     *
     */
    private void resize() {
        int newCapacity = 2 * this.internalCapacity();
        Value<T>[] resizedTable = (Value<T>[]) new Value[newCapacity];
        for (int position = 0; position < this.internalCapacity(); position++) {
            Value<T> valueToCopy = this.hashTable[position];
            int newPosition = hashAndProbe(valueToCopy.element, newCapacity, false, resizedTable);
            resizedTable[newPosition] = valueToCopy;
        }
        this.hashTable = resizedTable;
    }

    /**
     * Checks if the element is in the set (at least once).
     *
     * Let n denote the length of the hash table.
     *
     * In the worst case, the element would not exist in the hash table, and the hash table would
     * be full. Hence one would have to linearly probe the entire hash table. Thus leading to
     * this method being bounded by O(n) runtime complexity, and O(1) amortised runtime complexity
     * as a result, and O(1) space complexity, as only the element to search for, and an index
     * variable are stored.
     *
     * @param element to check
     * @return true if the element is in the set, else false.
     */
    @Override
    public boolean contains(T element) {
        if (this.distinctCount() > 0) {
            int index = element.hashCode() % this.internalCapacity();
            while (this.hashTable[index] != null) {
                if ((this.hashTable[index]).element.equals(element)) {
                    return !(this.hashTable[index].isDeleted);
                }
                index = (index + 1) % this.internalCapacity();

                // Reached the original index again and element is still not found
                if (index == element.hashCode() % this.internalCapacity()) {
                    break;
                }
            }
        }
        return false;
    }

    /**
     * Compresses via simple division and conducts linear probing on the element of interest.
     *
     * Let n denote the length of the hash table passed in as a parameter.
     *
     * In the worst case, the entire hash table would be full and would have to be searched as a
     * result. Hence, this method is bounded by O(n) runtime complexity in the worst case, and
     * O(1) amortised runtime complexity as a result. Similarly, as we take in the hash table as
     * a parameter, this method is bounded by O(n) space complexity as well.
     *
     * @param element the element to search an index for
     * @param capacity the hash table length of interest (either that of the resized hash table or
     *                of the original table)
     * @param searchItem whether we are searching for an item already stored (true), or if we are
     *                  simply searching for the first null position to store the new element
     *                   (false)
     * @param table the hash table (either the resized table or the original)
     * @return the position where the element is (if searchItem is true) or where the element may
     * be stored.
     */
    private int hashAndProbe(T element, int capacity, boolean searchItem, Value<T>[] table) {
        int index = element.hashCode() % capacity;
        if (this.distinctCount() > 0) {
            while ((searchItem && !(table[index]).element.equals(element)) ||
                    (!searchItem && table[index] != null)) {
                index = (index + 1) % capacity;
            }
        }
        return index;
    }

    /**
     * Returns the count of how many occurrences of the given elements there
     * are currently in the set.
     *
     * Let n denote the length of the hash table.
     *
     * As the contains() and hashAndProbe() methods are bounded by O(n) runtime and space
     * complexity, and all other operations are simple return statements, array indexing, and
     * parameter accessing, this method is also bounded by O(n) runtime complexity in the worst
     * case (and as a result, O(1) amortised runtime complexity) and O(1) space complexity, as
     * all that is stored is the single element to count.
     *
     * @param element to check
     * @return the count of occurrences of element
     */
    @Override
    public int count(T element) {
        int result = 0;
        if (this.contains(element)) {
            int position = this.hashAndProbe(element, this.internalCapacity(), true,
                    this.hashTable);
            result = this.hashTable[position].count;
        }
        return result;
    }

    /**
     * Removes a single occurrence of element from the set.
     *
     * Let n denote the length of the hash table.
     *
     * As the overloaded remove() method is bounded by O(n) runtime complexity and O(n) space
     * complexity in the worst case, and O(1) amortised runtime complexity as a result, so is
     * this method.
     *
     * @param element to remove
     * @throws NoSuchElementException if the set doesn't currently contain the given element
     * @require element != null
     */
    @Override
    public void remove(T element) throws NoSuchElementException {
        this.remove(element, 1);
    }

    /**
     * Removes several occurrences of the element from the set.
     *
     * Let n denote the length of the hash table.
     *
     * As this method simply performs conditional operations, parameter accesses, array indexing,
     * exception handling, and calls to methods bounded by O(n) runtime complexity, this method
     * is also bounded by O(n) runtime complexity in the worst case (and O(1) amortised
     * complexity as a result).
     *
     * Regarding space complexity, this method only stores a single element, and the number of
     * occurrences of said element to remove. As a result, this method is bounded by O(1) space
     * complexity.
     *
     * @param element to remove
     * @param count the number of occurrences of element to remove
     * @throws NoSuchElementException if the set contains less than
     *         count occurrences of the given element
     * @require element != null && count >= 0
     */
    @Override
    public void remove(T element, int count) throws NoSuchElementException {
        if (!this.contains(element) || this.count(element) < count) {
            throw new NoSuchElementException("There are fewer than " + count + "occurrences of " +
                    "this element");
        }
        int position = this.hashAndProbe(element, this.internalCapacity(), true, this.hashTable);
        this.hashTable[position].count -= count;
        this.totalNumElements -= count;

        if (this.count(element) == 0) {
            this.hashTable[position].isDeleted = true;

            if (this.distinctCount() == 1) {
                this.head = this.tail = null;
            } else if (this.hashTable[position] == this.head) {
                this.hashTable[position].next.previous = null;
                this.head = this.hashTable[position].next;
            } else if (this.hashTable[position] == this.tail) {
                this.hashTable[position].previous.next = null;
                this.tail = this.hashTable[position].previous;
            } else {
                this.hashTable[position].previous.next = this.hashTable[position].next;
                this.hashTable[position].next.previous = this.hashTable[position].previous;
            }

            this.hashTable[position].next = this.hashTable[position].previous = null;
            this.distinctNumElements--;
        }
    }

    /**
     * Returns the total count of all elements in the multiset.
     *
     * Note that duplicates of an element all contribute to the count here.
     *
     * As a single integer variable is returned, this method is bounded by O(1) runtime and space
     * complexity.
     *
     * @return total count of elements in the collection
     */
    @Override
    public int size() {
        return this.totalNumElements;
    }

    /**
     * Returns the maximum number of *distinct* elements the internal data
     * structure can contain before resizing.
     *
     * As a single integer variable is returned, this method is bounded by O(1) runtime and space
     * complexity.
     *
     * @return capacity of internal array
     */
    @Override
    public int internalCapacity() {
        return this.hashTable.length;
    }

    /**
     * Returns the number of distinct elements currently stored in the set.
     *
     * As a single integer variable is returned, this method is bounded by O(1) runtime and space
     * complexity.
     *
     * @return count of distinct elements in the set
     */
    @Override
    public int distinctCount() {
        return this.distinctNumElements;
    }

    /**
     * Returns an iterator that iterates from the earliest element added, to the latest.
     *
     * Each method is bounded by O(1) runtime complexity and space complexity (see below for
     * further details).
     *
     * @return an iterator that iterates from the earliest element added, to the latest
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            /** The current value node to iterate over */
            private Value<T> currentValue = null;

            /** The number of occurrences of currentValue. */
            private int currentValueOccurrence = 0;

            /**
             * As this method simply performs a conditional check and return statement, it is
             * bounded by O(1) runtime complexity. No variables are stored so it is bounded by
             * O(1) space as well.
             *
             * @return if there is another element to iterate over
             */
            @Override
            public boolean hasNext() {
                // Either head and tail are both null or both non-null, so only need to check one
                if (head == null) {
                    return false;
                }
                // Ensure last occurrence of the tail element is iterated over
                return this.currentValue != tail ||
                        this.currentValueOccurrence != this.currentValue.count;
            }

            /**
             * As this method simply performs conditional checks, exception handling, and parameter
             * accesses, it is bounded by O(1) runtime complexity.
             *
             * It stores no variables, and as a result is bounded by O(1) space complexity.
             *
             * @return the next element in the LinkedMultiHashSet.
             */
            @Override
            public T next() {
                if (this.currentValue == null) {
                    this.currentValue = head;
                }
                if (!this.hasNext()) {
                    throw new NoSuchElementException("No elements left.");
                } else if (this.currentValueOccurrence < this.currentValue.count) {
                    this.currentValueOccurrence++;
                } else {
                    this.currentValue = this.currentValue.next;
                    this.currentValueOccurrence = 1; // Element about to be returned
                }
                return this.currentValue.element;
            }
        };
    }
}