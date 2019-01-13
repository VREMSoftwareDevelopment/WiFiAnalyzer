/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.vrem.util;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumUtilsTest {

    @Test
    public void testOrdinals() {
        // setup
        Set<TestObject> expected = EnumUtils.values(TestObject.class);
        // execute
        Set<String> actual = EnumUtils.ordinals(TestObject.class);
        // validate
        assertEquals(expected.size(), actual.size());
        IterableUtils.forEach(expected, new OrdinalsClosure(actual));

    }

    @Test
    public void testValues() {
        // setup
        List<TestObject> expected = Arrays.asList(TestObject.values());
        // execute
        Set<TestObject> actual = EnumUtils.values(TestObject.class);
        // validate
        validate(expected, actual);
    }

    @Test
    public void testFindUsingValues() {
        // setup
        TestObject expected = TestObject.VALUE1;
        Set<TestObject> values = Collections.singleton(expected);
        // execute
        Set<String> actual = EnumUtils.find(values);
        // validate
        assertEquals(1, actual.size());
        assertTrue(actual.contains("" + expected.ordinal()));
    }

    @Test
    public void testFindUsingOrdinals() {
        // setup
        Set<TestObject> expected = EnumUtils.values(TestObject.class);
        Set<String> ordinals = new HashSet<>(
            Arrays.asList("" + TestObject.VALUE1.ordinal(), "" + TestObject.VALUE2.ordinal(), "" + TestObject.VALUE3.ordinal()));
        // execute
        Set<TestObject> actual = EnumUtils.find(TestObject.class, ordinals, TestObject.VALUE2);
        // validate
        validate(expected, actual);
    }

    @Test
    public void testFindUsingOrdinalsWithEmptyInput() {
        // setup
        Set<TestObject> expected = EnumUtils.values(TestObject.class);
        // execute
        Set<TestObject> actual = EnumUtils.find(TestObject.class, Collections.emptySet(), TestObject.VALUE2);
        // validate
        validate(expected, actual);
    }

    @Test
    public void testFindUsingOrdinalsWithInvalidInput() {
        // setup
        TestObject expected = TestObject.VALUE2;
        Set<String> ordinals = new HashSet<>(Arrays.asList("-1", null));
        // execute
        Set<TestObject> actual = EnumUtils.find(TestObject.class, ordinals, expected);
        // validate
        assertEquals(1, actual.size());
        assertTrue(actual.contains(expected));
    }

    @Test
    public void testFindUsingIndex() {
        // setup
        TestObject expected = TestObject.VALUE3;
        // execute
        TestObject actual = EnumUtils.find(TestObject.class, expected.ordinal(), TestObject.VALUE2);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testFindUsingInvalidIndex() {
        // setup
        int index = -1;
        TestObject expected = TestObject.VALUE2;
        // execute
        TestObject actual = EnumUtils.find(TestObject.class, index, expected);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testFindUsingPredicate() {
        // setup
        final TestObject expected = TestObject.VALUE3;
        Predicate<TestObject> predicate = PredicateUtils.equalPredicate(expected);
        // execute
        TestObject actual = EnumUtils.find(TestObject.class, predicate, TestObject.VALUE2);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testFindUsingPredicateWhenNothingFound() {
        // setup
        TestObject expected = TestObject.VALUE2;
        Predicate<TestObject> predicate = PredicateUtils.falsePredicate();
        // execute
        TestObject actual = EnumUtils.find(TestObject.class, predicate, expected);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testPredicateExpectsTruePredicateWithAllValues() {
        // setup
        Set<TestObject> inputs = EnumUtils.values(TestObject.class);
        // execute
        Predicate<TestObject> actual = EnumUtils.predicate(TestObject.class, inputs, new TestObjectTransformer());
        // validate
        assertTrue(actual instanceof TruePredicate);
    }

    @Test
    public void testPredicateExpectsAnyPredicateWithSomeValues() {
        // setup
        List<TestObject> inputs = Arrays.asList(TestObject.VALUE1, TestObject.VALUE3);
        // execute
        Predicate<TestObject> actual = EnumUtils.predicate(TestObject.class, inputs, new TestObjectTransformer());
        // validate
        assertTrue(actual instanceof AnyPredicate);
    }

    private void validate(Collection<TestObject> expected, Collection<TestObject> actual) {
        assertEquals(expected.size(), actual.size());
        IterableUtils.forEach(expected, new TestObjectClosure(actual));
    }

    private enum TestObject {
        VALUE1,
        VALUE3,
        VALUE2
    }

    private static class TestObjectTransformer implements Transformer<TestObject, Predicate<TestObject>> {
        @Override
        public Predicate<TestObject> transform(TestObject input) {
            return PredicateUtils.truePredicate();
        }
    }

    private static class OrdinalsClosure implements Closure<TestObject> {
        private final Set<String> actual;

        private OrdinalsClosure(Set<String> actual) {
            this.actual = actual;
        }

        @Override
        public void execute(TestObject input) {
            assertTrue(actual.contains("" + input.ordinal()));
        }
    }

    private static class TestObjectClosure implements Closure<TestObject> {
        private final Collection<TestObject> actual;

        private TestObjectClosure(Collection<TestObject> actual) {
            this.actual = actual;
        }

        @Override
        public void execute(TestObject input) {
            assertTrue(actual.contains(input));
        }
    }
}