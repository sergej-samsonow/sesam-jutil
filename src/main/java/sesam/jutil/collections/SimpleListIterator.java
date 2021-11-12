/**
 *  SPDX-License-Identifier: GPL-3.0-only
 *  SPDX-FileCopyrightText: © 2021 Sergej Samsonow <https://github.com/sergej-samsonow/sesam-jutil/issues>
 */
package sesam.jutil.collections;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.lang.UnsupportedOperationException;

/**
 * Reduced version of {@link ListIterator} for {@link SimpleList} object. <br />
 * <b>Supported methods:</b>
 * <ul>
 * <li>{@link SimpleListIterator#hasNext()}</li>
 * <li>{@link SimpleListIterator#next()}</li>
 * <li>{@link SimpleListIterator#nextIndex()}</li>
 * <li>{@link SimpleListIterator#hasPrevious()}</li>
 * <li>{@link SimpleListIterator#previous()}</li>
 * <li>{@link SimpleListIterator#previousIndex()}</li>
 * </ul>
 *
 * Any other methods throws {@link UnsupportedOperationException}
 *
 * @author Sergej Samsonow
 *
 * @param <E>
 */
@NotThreadSafe
public class SimpleListIterator<E> implements ListIterator<E> {

    private int cursor;
    private SimpleList<E> container;

    public SimpleListIterator(int index, SimpleList<E> container) {
        if (index < 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid cursor value: [%s] the lowerst possible cursor is 0", index));
        }
        this.cursor = index - 1;
        this.container = container;
        if (this.container.size() < 0) {
            throw new IllegalArgumentException(String.format("Ridiculous container size value: [%s]", this.container.size()));
        }
    }

    @Override
    public boolean hasNext() {
        return container.size() > cursor + 1;

    }

    @Override
    public E next() {
        int pos = nextIndex();
        E value = null;
        if (pos < container.size()) {
            value = container.get(pos);
            cursor = cursor + 1;
        }
        else {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public boolean hasPrevious() {
        return previousIndex() > -1;
    }

    @Override
    public E previous() {
        int pos = previousIndex();
        if (pos < 0) {
            throw new NoSuchElementException();
        }
        E value = container.get(pos);
        cursor = cursor - 1;
        return value;
    }

    @Override
    public int nextIndex() {
        return cursor + 1;
    }

    @Override
    public int previousIndex() {
        return cursor;
    }

    /**
     * Not supported.
     *
     * @throws {@link UnsupportedOperationException}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     *
     * @throws {@link UnsupportedOperationException}
     */
    @Override
    public void set(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     *
     * @throws {@link UnsupportedOperationException}
     */
    @Override
    public void add(E e) {
        throw new UnsupportedOperationException();
    }

}
