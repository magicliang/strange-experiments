package com.solomonl.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.*;
import java.util.function.Consumer;


/**
 * Created by magicliang on 2016/8/2. /** + enter above any annotation and types will create a new comment.
 * <p>
 * * @since   1.2
 *
 * @see java.util.Collection
 * @see java.util.List
 * @see LinkedList
 * @see Vector
 * @since 1.2
 */
public class ArrayList<E> implements List, java.io.Serializable {

    /**
     * Don't change this value when adding/removing fields. This value will keep consistency in serialization/deserialization.
     */
    private static final long serialVersionUID = -6604195725605343444L;
    /*
     * According to Java code style spec, the code and comment should be written in this style.
     * This is shared empty array instance used for empty instances.
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};
    /**
     * Shared empty array instance used for default sized empty instances. We
     * distinguish this from EMPTY_ELEMENTDATA to know how much to inflate when
     * first element is added.
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
    /**
     * Default initail capacity.
     */
    private final int DEFAULT_CAPACITY = 10;
    /**
     * Why don't use Node?
     */
    transient Object[] elementData;  // non-private to simplify nested class access // So use single line comment along with a line.

    /**
     * The size of the ArrayList (the number of elements it contains).
     *
     * @serial
     */
    private int size;

    private Node[] array;

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity
     *                                  is negative
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity:" + initialCapacity);
        }
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
    public ArrayList(Collection<? extends E> c) {
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class) {
                // Don't use System.arraycopy directly
                // This method can transform array to object array
                elementData = Arrays.copyOf(elementData, size,
                        // new line after ,
                        Object[].class);
            }
        } else {
            // replace with empty array.
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }


    /**
     * Trims the capacity of this <tt>ArrayList</tt> instance to be the
     * list's current size.  An application can use this operation to minimize
     * the storage of an <tt>ArrayList</tt> instance.
     */

    public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            elementData = (size == 0) ?
                    EMPTY_ELEMENTDATA :
                    // This is using orinal type to create new array, it is still resuing that class version.
                    Arrays.copyOf(elementData, size);
        }
    }

    /**
     * @return return the iterator for this ArrayList
     */
    public Iterator iterator() {
        return null;
    }

    /**
     * @param action a lambada as code block to consume every element of this list.
     */
    @Override
    public void forEach(Consumer action) {

    }

    @Override
    public Spliterator spliterator() {
        return null;
    }

    protected transient int modCount = 0;

}
