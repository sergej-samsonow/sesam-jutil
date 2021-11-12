/**
 *  SPDX-License-Identifier: GPL-3.0-only
 *  SPDX-FileCopyrightText: © 2021 Sergej Samsonow <https://github.com/sergej-samsonow/sesam-jutil/issues>
 */
package sesam.jutil.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SimpleListIteratorTest {

    @Mock
    private SimpleList<String> container;

    private SimpleListIterator<String> iterator;

    private void initialiseContainerWithOneElementAndStartIndex0() {
        when(container.size()).thenReturn(1);
        iterator = new SimpleListIterator<>(0, container);
    }

    @Test
    @DisplayName("Constructor simple initialisation")
    public void testSimpleListIterator() {
        initialiseContainerWithOneElementAndStartIndex0();
    }

    @Nested
    @DisplayName("Constructor more initialisation cases")
    public class MoreConstructorTests {

        @Test
        @DisplayName("Index is lower then 0")
        public void testSimpleListIteratorIllegalIndex() {
            assertThatThrownBy(() -> new SimpleListIterator<>(-1, container)).isInstanceOf(IllegalArgumentException.class);
        }


        @ParameterizedTest
        @ValueSource(ints = {-3, -10, -21, -45})
        @DisplayName("More unexpected indexs")
        public void testSimpleListIteratorConstructorMoreIllegalIndexes(int index) {
            assertThatThrownBy(() -> new SimpleListIterator<>(index, container)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Container size is lower then 0")
        public void testSimpleListIteratorContainerReturnIllegalSize() {
            assertThatThrownBy(() -> {
                when(container.size()).thenReturn(-1);
                new SimpleListIterator<>(0, container);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = {-40448, -10404, -2, -5})
        @DisplayName("More unexpected container sizes")
        public void testSimpleListIteratorConstructorMoreIllegalContainerSizes(int size) {
            assertThatThrownBy(() -> {
                when(container.size()).thenReturn(size);
                new SimpleListIterator<>(0, container);
            }).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    public void testSet() {
        initialiseContainerWithOneElementAndStartIndex0();
        assertThatThrownBy(() -> iterator.set("")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testAdd() {
        initialiseContainerWithOneElementAndStartIndex0();
        assertThatThrownBy(() -> iterator.add("")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testRemove() {
        initialiseContainerWithOneElementAndStartIndex0();
        assertThatThrownBy(iterator::remove).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Method nextIndex - standard behaviour")
    public void testNextIndex() {
        initialiseContainerWithOneElementAndStartIndex0();
        ListIterator<String> reference = new ArrayList<String>().listIterator();
        assertThat(iterator.nextIndex()).isEqualTo(reference.nextIndex());
    }

    @Test
    @DisplayName("Method hasNext - standard behaviour")
    public void testHasNext() {
        initialiseContainerWithOneElementAndStartIndex0();
        assertThat(iterator.hasNext()).isTrue();
        when(container.size()).thenReturn(0);
        iterator = new SimpleListIterator<>(0, container);
        ListIterator<String> reference = new ArrayList<String>().listIterator();
        assertThat(iterator.hasNext()).isEqualTo(reference.hasNext());
    }

    @Test
    @DisplayName("Method hasPrevious - standard behaviour")
    public void testHasPrevious() {
        initialiseContainerWithOneElementAndStartIndex0();
        assertThat(iterator.hasPrevious()).isFalse();
    }

    @Test
    @DisplayName("Method previousIndex - standard behaviour")
    public void testPreviousIndex() {
        initialiseContainerWithOneElementAndStartIndex0();
        ListIterator<String> reference = new ArrayList<String>().listIterator();
        assertThat(iterator.previousIndex()).isEqualTo(reference.previousIndex());
    }

    @Test
    @DisplayName("Method next - standard behaviour")
    public void testNext() {
        when(container.size()).thenReturn(2);
        when(container.get(eq(0))).thenReturn("A");
        iterator = new SimpleListIterator<>(0, container);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("A");
        ListIterator<String> reference = arrayList.listIterator();
        assertThat(iterator.next()).isEqualTo(reference.next());
        verify(container).get(eq(0));
    }

    @Test
    @DisplayName("Method previous - standard behaviour")
    public void testPrevious() {
        when(container.size()).thenReturn(2);
        when(container.get(eq(0))).thenReturn("A");
        iterator = new SimpleListIterator<>(0, container);
        ListIterator<String> reference = referenceListIterator();
        assertThat(iterator.next()).isEqualTo(reference.next());
        assertThat(iterator.previous()).isEqualTo(reference.previous());
        verify(container, times(2)).get(eq(0));
    }

    private ListIterator<String> referenceListIterator() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("A");
        arrayList.add("B");
        ListIterator<String> reference = arrayList.listIterator();
        return reference;
    }

    @Nested
    @DisplayName("Cursor behaivour related tests")
    public class CurosorBehaviourRelatedTests {

        @Test
        public void testNoSuchElementExceptionOnPrevious() {
            when(container.size()).thenReturn(2);
            when(container.get(eq(0))).thenReturn("A");
            iterator = new SimpleListIterator<>(0, container);
            ListIterator<String> reference = referenceListIterator();
            assertThat(iterator.next()).isEqualTo(reference.next()).isEqualTo("A");
            assertThat(iterator.previous()).isEqualTo(reference.previous()).isEqualTo("A");
            assertThatThrownBy(reference::previous).isInstanceOf(NoSuchElementException.class);
            assertThatThrownBy(iterator::previous).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        public void testNoSuchElementExceptionOnNext() {
            when(container.size()).thenReturn(2);
            when(container.get(eq(0))).thenReturn("A");
            when(container.get(eq(1))).thenReturn("B");
            iterator = new SimpleListIterator<>(0, container);
            ListIterator<String> reference = referenceListIterator();
            assertThat(iterator.next()).isEqualTo(reference.next()).isEqualTo("A");
            assertThat(iterator.next()).isEqualTo(reference.next()).isEqualTo("B");
            assertThatThrownBy(reference::next).isInstanceOf(NoSuchElementException.class);
            assertThatThrownBy(iterator::next).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        public void testVerifyHasPrevious() {
            when(container.size()).thenReturn(2);
            when(container.get(eq(0))).thenReturn("A");
            when(container.get(eq(1))).thenReturn("B");
            iterator = new SimpleListIterator<>(0, container);
            ListIterator<String> reference = referenceListIterator();
            assertThat(iterator.hasPrevious()).isEqualTo(reference.hasPrevious()).isFalse();
            assertThat(iterator.next()).isEqualTo(reference.next());
            assertThat(iterator.hasPrevious()).isEqualTo(reference.hasPrevious()).isTrue();
            assertThat(iterator.next()).isEqualTo(reference.next());
            assertThat(iterator.hasPrevious()).isEqualTo(reference.hasPrevious()).isTrue();
            assertThat(iterator.previous()).isEqualTo(reference.previous());
            assertThat(iterator.hasPrevious()).isEqualTo(reference.hasPrevious()).isTrue();
            assertThat(iterator.previous()).isEqualTo(reference.previous());
            assertThat(iterator.hasPrevious()).isEqualTo(reference.hasPrevious()).isFalse();
        }

        @Test
        public void testVerifyHasNext() {
            when(container.size()).thenReturn(2);
            when(container.get(eq(0))).thenReturn("A");
            when(container.get(eq(1))).thenReturn("B");
            iterator = new SimpleListIterator<>(0, container);
            ListIterator<String> reference = referenceListIterator();
            assertThat(iterator.hasNext()).isEqualTo(reference.hasNext()).isTrue();
            assertThat(iterator.next()).isEqualTo(reference.next());
            assertThat(iterator.hasNext()).isEqualTo(reference.hasNext()).isTrue();
            assertThat(iterator.next()).isEqualTo(reference.next());
            assertThat(iterator.hasNext()).isEqualTo(reference.hasNext()).isFalse();
        }
    }
}
