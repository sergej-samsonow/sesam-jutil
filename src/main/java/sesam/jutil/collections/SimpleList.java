/**
 *  SPDX-License-Identifier: GPL-3.0-only
 *  SPDX-FileCopyrightText: © 2021 Sergej Samsonow <https://github.com/sergej-samsonow/sesam-jutil/issues>
 */
package sesam.jutil.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * Reduced implementation of {@link List} interface. <br />
 *
 * <b>Supported methods:</b>
 * <ul>
 * <li>{@link SimpleList#isEmpty()}</li>
 * <li>{@link SimpleList#add(Object)}</li>
 * <li>{@link SimpleList#addAll(Collection)}</li>
 * <li>{@link SimpleList#clear()}</li>
 * <li>{@link SimpleList#contains(Object)}</li>
 * <li>{@link SimpleList#containsAll(Collection)}</li>
 * <li>{@link SimpleList#indexOf(Object)}</li>
 * <li>{@link SimpleList#iterator()}</li>
 * <li>{@link SimpleList#get(int)}</li>
 * <li>{@link SimpleList#lastIndexOf(Object)}</li>
 * <li>{@link SimpleList#listIterator()}</li>
 * <li>{@link SimpleList#listIterator(int)}</li>
 * <li>{@link SimpleList#size()}</li>
 * <li>{@link SimpleList#toArray()}</li>
 * <li>{@link SimpleList#toArray(Object[])}</li>
 * </ul>
 *
 * <p>Any other method throws {@link UnsupportedOperationException}</p>
 * <p>The basic idea behind implementations is {@link List} implementation
 * without list modifications method like {@link List#remove(int)} or
 * {@link List#set(int, Object)}.</p>
 *
 * <p><b>Internal array initialization:</b></p>
 * <p>Another idea is to resize internal array with fixed elements count
 * instead of double it. It is possible to define first initialization
 * block size and follow initialization block size. Default value for
 * initial initialization size is {@link SimpleList#DEFAULT_INITIAL} and
 * default additional initialization size is {@link SimpleList#DEFAULT_ADDITIONAL}.
 * <br />
 * <br />
 * <b>Algorithm code:</b>
 * <pre>
 * {@code
 * if (data == null) {
 *     data = new Object[incomingCount + initial];
 * }
 * else {
 *     int free = data.length - count;
 *     int totalCount = count + incomingCount;
 *     if (free < incomingCount) {
 *         int newSize = totalCount + additional;
 *         Object[] largeStorage = new Object[newSize];
 *         System.arraycopy(data, 0, largeStorage, 0, count);
 *         data = largeStorage;
 *     }
 * }
 * }
 * </pre>
 * Internal array initialization code is located in: {@link SimpleList#increaseIfNecessary(int)}
 * </p>
 *
 * @author Sergej Samsonow
 *
 * @param <E>
 */

@NotThreadSafe
public class SimpleList<E> implements List<E> {

    /**
     * Default initial (empty) elements count that will be added to size of internal array on initial initialization step.
     */
    public static final int DEFAULT_INITIAL 	= 10;

    /**
     * Default additional (empty) elements count that will be added to size of internal array on resize array step.
     */
    public static final int DEFAULT_ADDITIONAL	= 100;

    private Object[] data;
    private int count;
    private int initial		= DEFAULT_INITIAL;
    private int additional 	= DEFAULT_ADDITIONAL;

    /**
     * Default constructor.
     */
    public SimpleList() {
        super();
    }

    /**
     * Default constructor with customized internal array initialization.
     *
     * @param initial amount of elements that will be added to size of internal array on initial initialization step.
     * @param additional amount of elements that will be added to size of internal array on resize array step.
     */
    public SimpleList(int initial, int additional) {
        this();
        if (initial < 1) {
            throw new IllegalArgumentException();
        }
        if (additional < 1) {
            throw new IllegalArgumentException();
        }
        this.initial = initial;
        this.additional = additional;
    }

    /**
     * Default constructor with customized internal array initialization.
     *
     * @param additional amount of elements that will be added to size of internal array on first initialization step and on resize array step.
     */
    public SimpleList(int additional) {
        this(additional, additional);
    }

    /**
     * Initialize list with elements from incoming collection.
     *
     * @param incoming initialize with elements from this collection
     */
    public SimpleList(Collection<? extends E> incoming) {
        this();
        addAll(incoming);
    }

    /**
     * Initialize list with elements from incoming collection and customized internal array initialization.
     *
     * @param incoming initialize with elements from this collection
     * @param additional amount of elements that will be added to size of internal array on first initialization step and on resize array step.
     */
    public SimpleList(Collection<? extends E> incoming, int additional) {
        this(additional, additional);
        addAll(incoming);
    }

    /**
     * Initialize list with elements from incoming collection and customized internal array initialization.
     *
     * @param incoming initialize with elements from this collection
     * @param initial amount of elements that will be added to size of internal array on initial initialization step.
     * @param additional amount of elements that will be added to size of internal array on resize array step.
     */
    public SimpleList(Collection<? extends E> incoming, int initial, int additional) {
        this(initial, additional);
        addAll(incoming);
    }

    private void increaseIfNecessary(int incomingCount) {
        if (data == null) {
            data = new Object[incomingCount + initial];
        }
        else {
            int free = data.length - count;
            int totalCount = count + incomingCount;
            if (free < incomingCount) {
                int newSize = totalCount + additional;
                Object[] largeStorage = new Object[newSize];
                System.arraycopy(data, 0, largeStorage, 0, count);
                data = largeStorage;
            }
        }
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (indexOf(o) == -1) {
                return false;
            }
        }
        return true;
    }


    @Override
    public Iterator<E> iterator() {
        return listIterator(0);
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[count];
        if (count > 0) {
            System.arraycopy(data, 0, result, 0, count);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (count > 0) {
            if (a.length < count) {
                return (T[]) Arrays.copyOf(data, count, a.getClass());
            }
            System.arraycopy(data, 0, a, 0, count);
            if (a.length > count) {
                a[count] = null;
            }
        }
        return a;
    }

    /**
     * Not supported.
     *
     * @throws {@link UnsupportedOperationException}
     */
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(E e) {
        increaseIfNecessary(1);
        data[count] = e;
        count = count + 1;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        int incomingCount = c.size();
        if (incomingCount  < 1) {
            return false;
        }
        increaseIfNecessary(incomingCount);
        Iterator<? extends E> iterator = c.iterator();
        for (int i = 0; i < incomingCount; i++) {
            data[count] = iterator.next();
            count = count + 1;
        }
        /**
         * An other one algorithm cut be:
         * <code>
         * Object[] incomming = c.toArray();
         * if (incomming.length < 1) {
         *     return false;
         * }
         * increaseIfNecessary(incomming.length);
         * System.arraycopy(incomming, 0, data, count, incomming.length);
         * count = count + incomming.length;
         * </code>
         *
         * Assumption is that one java: for (int i = 0; i < incomingCount; i++)
         * in current algorithm perform better that: Object[] incomming = c.toArray();
         * and System.arraycopy(incomming, 0, data, count, incomming.length);
         * but need to be measured
         */
        return true;
    }

    /**
     * Not supported.
     *
     * @throws {@link UnsupportedOperationException}
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     *
     * @throws {@link UnsupportedOperationException}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     *
     * @throws {@link UnsupportedOperationException}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        count = 0;
        for (int i = 0; i < count; i++) {
            data[i] = null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (count < 1 || index > count -1 || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return (E)data[index];
    }

    /**
     * Not supported.
     *
     * @throws {@link UnsupportedOperationException}
     */
    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     *
     * @throws {@link UnsupportedOperationException}
     */
    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     *
     * @throws {@link UnsupportedOperationException}
     */
    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                if (Objects.equals(data[i], o)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (count > 0) {
            for (int i = count - 1; i > -1; i--) {
                if (Objects.equals(data[i], o)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new SimpleListIterator<>(index, this);
    }

    /**
     * Not supported.
     *
     * @throws {@link UnsupportedOperationException}
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

}
