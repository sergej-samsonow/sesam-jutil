/**
 *  SPDX-License-Identifier: GPL-3.0-only
 *  SPDX-FileCopyrightText: © 2021 Sergej Samsonow <https://github.com/sergej-samsonow/sesam-jutil/issues>
 */
package sesam.jutil.collections;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SimpleListTest {

    @Mock
    private Collection<Object> collection;

    @Test
    public void testListIteratorInt() {
        SimpleList<Object> list = new SimpleList<>();
        assertThat(list.listIterator(0)).isNotNull().isInstanceOf(SimpleListIterator.class);
    }

    @Test
    public void testListIterator() {
        SimpleList<Object> list = new SimpleList<>();
        assertThat(list.listIterator()).isNotNull().isInstanceOf(SimpleListIterator.class);
    }

    @Test
    public void testIterator() {
        SimpleList<Object> list = new SimpleList<>();
        assertThat(list.iterator()).isNotNull().isInstanceOf(SimpleListIterator.class);
    }

    @Test
    public void testSubList() {
        SimpleList<Object> list = new SimpleList<>();
        assertThatThrownBy(() -> list.subList(0, 1)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testRemoveInt() {
        SimpleList<Object> list = new SimpleList<>();
        assertThatThrownBy(() -> list.remove(1)).isInstanceOf(UnsupportedOperationException.class);

    }

    @Test
    public void testSet() {
        SimpleList<Object> list = new SimpleList<>();
        assertThatThrownBy(() -> list.set(0, 1)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testRetainAll() {
        SimpleList<Object> list = new SimpleList<>();
        assertThatThrownBy(() -> list.retainAll(collection)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testRemoveAll() {
        SimpleList<Object> list = new SimpleList<>();
        assertThatThrownBy(() -> list.removeAll(collection)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testRemoveObject() {
        SimpleList<Object> list = new SimpleList<>();
        assertThatThrownBy(() -> list.remove(1)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testAddAllAtIndex() {
        SimpleList<Object> list = new SimpleList<>();
        assertThatThrownBy(() -> list.addAll(0, collection)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testAddAtIndex() {
        SimpleList<Object> list = new SimpleList<>();
        assertThatThrownBy(() -> list.add(0, "")).isInstanceOf(UnsupportedOperationException.class);
    }


    @Nested
    @DisplayName("Methods add/get/size/isEmpty/toArray test cases")
    public class AddMethod {

        @Test
        @DisplayName("Empty list get/size/isEmpty/toArray")
        public void testEmptyList() {
            ArrayList<String> reference = new ArrayList<>();
            SimpleList<String> list 	= new SimpleList<>();
            assertThatThrownBy(() -> reference.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
            assertThatThrownBy(() -> list.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
            assertThat(list.size()).isEqualTo(reference.size()).isEqualTo(0);
            assertThat(list.isEmpty()).isEqualTo(reference.isEmpty()).isTrue();
            assertThat(list.toArray()).isEqualTo(reference.toArray()).isEqualTo(new String[] {});
            assertThat(list.toArray(new String[] {})).isEqualTo(reference.toArray(new String[] {})).isEqualTo(new String[] {});
        }

        @Test
        @DisplayName("One entry add/get/size/isEmpty/toArray")
        public void testOneEntry() {
            SimpleList<String> list     = new SimpleList<>();
            assertThat(list.add("A")).isTrue();
            assertThat(list.get(0)).isEqualTo("A");
            assertThat(list.size()).isEqualTo(1);
            assertThat(list.isEmpty()).isFalse();
            String[] expected = new String[] {"A"};
            String[] passedToList      = new String[list.size()];
            assertThat(list.toArray()).isEqualTo(expected);
            assertThat(list.toArray(passedToList)).isEqualTo(expected);
        }

        @Test
        @DisplayName("Two entries add/get/size/isEmpty/toArray")
        public void testTwoEntries() {
            SimpleList<String> list = new SimpleList<>();
            assertThat(list.add("A")).isTrue();
            assertThat(list.add("B")).isTrue();
            assertThat(list.get(0)).isEqualTo("A");
            assertThat(list.get(1)).isEqualTo("B");
            assertThat(list.size()).isEqualTo(2);
            assertThat(list.isEmpty()).isFalse();
            String[] expected = new String[] {"A", "B"};
            String[] passedToList      = new String[list.size()];
            assertThat(list.toArray()).isEqualTo(expected);
            assertThat(list.toArray(passedToList)).isEqualTo(expected);
        }

        @Test
        @DisplayName("Invalid passed array")
        public void testPassedArray() {
            SimpleList<String> list = new SimpleList<>();
            ArrayList<String> reference = new ArrayList<>();
            list.add("A");
            list.add("B");
            reference.add("A");
            reference.add("B");
            String[] expected 			= new String[] {"A", "B"};
            String[] passedToList		= new String[list.size()];
            String[] passedToReference	= new String[reference.size()];
            assertThat(list.toArray(passedToList)).isEqualTo(reference.toArray(passedToReference)).isEqualTo(expected);
            passedToList		= new String[0];
            passedToReference	= new String[0];
            assertThat(reference.toArray(passedToReference)).isEqualTo(reference.toArray(list.toArray(passedToList))).isEqualTo(expected);
            passedToList		= new String[] {"A0", "A1", "A2", "A3", "A4"};
            passedToReference	= new String[] {"A0", "A1", "A2", "A3", "A4"};
            assertThat(reference.toArray(passedToReference)).isEqualTo(list.toArray(passedToList)).isEqualTo(new String[] {"A", "B", null, "A3", "A4"});
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -1, 3, 8, Integer.MAX_VALUE})
        @DisplayName("Illegal index entry add/get/size/isEmpty")
        public void testGetOutOfBounds(int index) {
            ArrayList<String> reference = new ArrayList<>();
            SimpleList<String> list 	= new SimpleList<>();
            list.add("A");
            list.add("B");
            reference.add("A");
            reference.add("B");
            assertThatThrownBy(() -> reference.get(index)).isInstanceOf(IndexOutOfBoundsException.class);
            assertThatThrownBy(() -> list.get(index)).isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("One thousand entries add/get/size/isEmpty")
        public void testOneThousandEntries() {
            SimpleList<Integer> list = new SimpleList<>();
            for (int i = 1; i <= 10000; i++) {
                assertThat(list.add(i)).isTrue();
                assertThat(list.get(i - 1)).isEqualTo(i);
                assertThat(list.size()).isEqualTo(i);
            }
            assertThat(list.get(9)).isEqualTo(10);
            assertThat(list.get(10)).isEqualTo(11);
            assertThat(list.get(11)).isEqualTo(12);
            assertThat(list.get(13)).isEqualTo(14);
            assertThat(list.get(99)).isEqualTo(100);
            assertThat(list.get(100)).isEqualTo(101);
            assertThat(list.get(101)).isEqualTo(102);
            assertThat(list.get(109)).isEqualTo(110);
            assertThat(list.get(110)).isEqualTo(111);
            assertThat(list.get(111)).isEqualTo(112);
        }

        @Test
        @DisplayName("Add empty list add/get/size/isEmpty")
        public void testAddEmptyList() {
            ArrayList<String> reference = new ArrayList<>();
            SimpleList<String> list 	= new SimpleList<>();
            assertThat(list.addAll(Collections.emptyList())).isEqualTo(reference.addAll(Collections.emptyList())).isFalse();
            assertThatThrownBy(() -> reference.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
            assertThatThrownBy(() -> list.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
            assertThat(list.size()).isEqualTo(reference.size()).isEqualTo(0);
            assertThat(list.isEmpty()).isEqualTo(reference.isEmpty()).isTrue();
        }

        @Test
        @SuppressWarnings("unchecked")
        @DisplayName("Add list add/get/size/isEmpty")
        public void testAddList() {
            SimpleList<String> list 	= new SimpleList<>();
            //                                                         index:       0     1     2     3     4     5     6     7
            List<String> param = (List<String>) (Object) Arrays.asList(new String[]{"A0", "B0", "C0", "D0", "E0", "F0", "G0", "H0"});
            list.addAll(param);
            assertThat(list.get(0)).isEqualTo("A0");
            assertThat(list.get(4)).isEqualTo("E0");
            assertThat(list.get(7)).isEqualTo("H0");
            assertThat(list.size()).isEqualTo(8);
            assertThat(list.isEmpty()).isFalse();

            //                                            index:       8     9     10    11    12    13    14    15
            param = (List<String>) (Object) Arrays.asList(new String[]{"A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1"});
            list.addAll(param);

            assertThat(list.get( 0)).isEqualTo("A0");
            assertThat(list.get( 4)).isEqualTo("E0");
            assertThat(list.get( 7)).isEqualTo("H0");
            assertThat(list.get( 8)).isEqualTo("A1");
            assertThat(list.get(10)).isEqualTo("C1");
            assertThat(list.get(15)).isEqualTo("H1");
            assertThat(list.size()).isEqualTo(16);
            assertThat(list.isEmpty()).isFalse();

            //                                            index:       16    17    18    19    20    21    22    23
            param = (List<String>) (Object) Arrays.asList(new String[]{"A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2"});
            list.addAll(param);

            assertThat(list.get( 0)).isEqualTo("A0");
            assertThat(list.get( 4)).isEqualTo("E0");
            assertThat(list.get( 7)).isEqualTo("H0");
            assertThat(list.get( 8)).isEqualTo("A1");
            assertThat(list.get(10)).isEqualTo("C1");
            assertThat(list.get(15)).isEqualTo("H1");
            assertThat(list.get(16)).isEqualTo("A2");
            assertThat(list.get(19)).isEqualTo("D2");
            assertThat(list.get(23)).isEqualTo("H2");
            assertThat(list.size()).isEqualTo(24);
            assertThat(list.isEmpty()).isFalse();

        }

    }

    @Test
    public void testClear() {
        SimpleList<String> list = new SimpleList<>();
        list.add("A");
        list.add("B");
        assertThat(list.size()).isEqualTo(2);
        assertThat(list.isEmpty()).isFalse();
        assertThat(list.get(0)).isEqualTo("A");
        assertThat(list.get(1)).isEqualTo("B");
        assertThat(list.toArray()).isEqualTo(new String[] {"A", "B"});
        assertThat(list.toArray(new String[5])).isEqualTo(new String[] {"A", "B", null, null, null});
        list.clear();
        assertThat(list.size()).isEqualTo(0);
        assertThat(list.isEmpty()).isTrue();
        assertThatThrownBy(() -> list.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThat(list.toArray()).isEqualTo(new String[] {});
        assertThat(list.toArray(new String[5])).isEqualTo(new String[] {null, null, null, null, null});
    }

    @Nested
    @DisplayName("Methods indexOf/lastIndexOf/contains/containsAll")
    public class SearchMethods {

        @Test
        @DisplayName("Search on empty array")
        public void testSearchOnEmptyArray() {
            ArrayList<String> reference = new ArrayList<>();
            SimpleList<String> list 	= new SimpleList<>();
            assertThat(reference.indexOf("A")).isEqualTo(list.indexOf("A")).isEqualTo(-1);
            assertThat(reference.lastIndexOf("A")).isEqualTo(list.lastIndexOf("A")).isEqualTo(-1);
            assertThat(reference.contains("A")).isEqualTo(list.contains("A")).isFalse();
            List<Object> search = Arrays.asList(new String[] {"A", "B"});
            assertThat(reference.containsAll(search)).isEqualTo(list.containsAll(search)).isFalse();
        }

        @Test
        @DisplayName("Searches not found")
        public void testSearchNotFound() {
            @SuppressWarnings("unchecked")
            List<String> stored = (List<String>)(Object) Arrays.asList(new String[] {"E", "F"});
            ArrayList<String> reference = new ArrayList<>();
            reference.addAll(stored);
            SimpleList<String> list 	= new SimpleList<>();
            list.addAll(stored);
            assertThat(reference.indexOf("A")).isEqualTo(list.indexOf("A")).isEqualTo(-1);
            assertThat(reference.lastIndexOf("A")).isEqualTo(list.lastIndexOf("A")).isEqualTo(-1);
            assertThat(reference.contains("A")).isEqualTo(list.contains("A")).isFalse();
            List<Object> search = Arrays.asList(new String[] {"A", "B"});
            assertThat(reference.containsAll(search)).isEqualTo(list.containsAll(search)).isFalse();
        }

        @Test
        @DisplayName("Searches found")
        public void testSearchFound() {
            @SuppressWarnings("unchecked")
            List<String> stored = (List<String>)(Object) Arrays.asList(new String[] {"A", "B"});
            ArrayList<String> reference = new ArrayList<>();
            reference.addAll(stored);
            SimpleList<String> list 	= new SimpleList<>();
            list.addAll(stored);
            assertThat(reference.indexOf("A")).isEqualTo(list.indexOf("A")).isEqualTo(0);
            assertThat(reference.lastIndexOf("A")).isEqualTo(list.lastIndexOf("A")).isEqualTo(0);
            assertThat(reference.contains("A")).isEqualTo(list.contains("A")).isTrue();
            List<Object> search = Arrays.asList(new String[] {"A", "B"});
            assertThat(reference.containsAll(search)).isEqualTo(list.containsAll(search)).isTrue();
        }

        @Test
        @DisplayName("Searches containsAll collection is null")
        public void testContainsAllCollectionIsNull() {
            @SuppressWarnings("unchecked")
            List<String> stored = (List<String>)(Object) Arrays.asList(new String[] {"A", "B"});
            ArrayList<String> reference = new ArrayList<>();
            reference.addAll(stored);
            SimpleList<String> list 	= new SimpleList<>();
            list.addAll(stored);
            assertThatThrownBy(() -> reference.containsAll(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> list.containsAll(null)).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Search containsAll partial match")
        public void testSearchContainsAllPartialMatch() {
            @SuppressWarnings("unchecked")
            List<String> stored = (List<String>)(Object) Arrays.asList(new String[] {"A", "B"});
            ArrayList<String> reference = new ArrayList<>();
            reference.addAll(stored);
            SimpleList<String> list 	= new SimpleList<>();
            list.addAll(stored);
            List<Object> search = Arrays.asList(new String[] {"A", "C"});
            assertThat(reference.containsAll(search)).isEqualTo(list.containsAll(search)).isFalse();
        }

        @Test
        @DisplayName("Search lastIndexOf correct match")
        public void testSearchLastIndexOfCorrectMatch() {
            @SuppressWarnings("unchecked")
            List<String> stored = (List<String>)(Object) Arrays.asList(new String[] {"A", "B", "A", "C"});
            ArrayList<String> reference = new ArrayList<>();
            reference.addAll(stored);
            SimpleList<String> list 	= new SimpleList<>();
            list.addAll(stored);
            assertThat(reference.lastIndexOf("A")).isEqualTo(list.lastIndexOf("A")).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    public class ConstructorTests {

        @ParameterizedTest
        @ValueSource(ints = { Integer.MIN_VALUE, -1, 0 })
        @DisplayName("Negative Initialisations values")
        public void testSimpleListIntInt(int offsetSize) {
            assertThatThrownBy(() -> new SimpleList<>(10, offsetSize)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new SimpleList<>(offsetSize, offsetSize)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new SimpleList<>(offsetSize, 10)).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @DisplayName("Valid initialisation")
        @MethodSource("testSimpleListIntIntValues")
        public void testSimpleListIntInt(int initial, int additional) {
            assertThatNoException().isThrownBy(() -> new SimpleList<>(initial, additional));
        }

        public static Stream<Arguments> testSimpleListIntIntValues() {
            return Stream.of(
                    Arguments.of(10,   100),
                    Arguments.of(1,    100),
                    Arguments.of(100, 1000),
                    Arguments.of(2,      5),
                    Arguments.of(10,    10),
                    Arguments.of(100,   10)
            );
        }

    }

    @Nested
    @DisplayName("Iterator Tests")
    public class IteratorTests {

        @Test
        @DisplayName("Simple Iterator test")
        public void testSimpleIterator() {
            @SuppressWarnings("unchecked")
            List<String> stored = (List<String>)(Object) Arrays.asList(new String[] {"A", "B", "A", "C"});
            SimpleList<String> list 	= new SimpleList<>();
            list.addAll(stored);
            assertThat(list.size()).isEqualTo(stored.size());
            int i = 0;
            for (String current : list) {
                assertThat(current).isEqualTo(stored.get(i++));
            }
            Iterator<String> iterator = list.iterator();
            assertThat(iterator.next()).isEqualTo(stored.get(0));
            assertThat(iterator.next()).isEqualTo(stored.get(1));
            assertThat(iterator.next()).isEqualTo(stored.get(2));
            assertThat(iterator.next()).isEqualTo(stored.get(3));
        }

        @Test
        @DisplayName("Extend list during iteration")
        public void testExtendListDuringIteration() {
            @SuppressWarnings("unchecked")
            List<String> stored = (List<String>)(Object) Arrays.asList(new String[] {"A", "B", "A", "C"});
            SimpleList<String> list 	= new SimpleList<>();
            list.addAll(stored);
            Iterator<String> iterator = list.iterator();
            assertThat(iterator.next()).isEqualTo(stored.get(0));
            assertThat(iterator.next()).isEqualTo(stored.get(1));
            assertThat(iterator.next()).isEqualTo(stored.get(2));
            assertThat(iterator.next()).isEqualTo(stored.get(3));
            list.add("D");
            list.add("E");
            assertThat(iterator.next()).isEqualTo("D");
            assertThat(iterator.next()).isEqualTo("E");
        }

        @Test
        @DisplayName("Clear list during iteration")
        public void testClearListDuringIteration() {
            @SuppressWarnings("unchecked")
            List<String> stored = (List<String>)(Object) Arrays.asList(new String[] {"A", "B", "A", "C"});
            SimpleList<String> list 	= new SimpleList<>();
            list.addAll(stored);
            Iterator<String> iterator = list.iterator();
            assertThat(iterator.next()).isEqualTo(stored.get(0));
            assertThat(iterator.next()).isEqualTo(stored.get(1));
            list.clear();
            assertThatThrownBy(() -> iterator.next()).isInstanceOf(NoSuchElementException.class);
        }

    }


}
