/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EnumUtilsTest {

    @Test
    fun testOrdinals() {
        // setup
        val expected = TestObject.values()
        // execute
        val actual = ordinals(TestObject.values())
        // validate
        assertEquals(expected.size, actual.size)
        expected.forEach {
            assertTrue(actual.contains("" + it.ordinal))
        }
    }

    @Test
    fun testFindSetUsingOrdinals() {
        // setup
        val expected = TestObject.values().toSet()
        val ordinals: Set<String> = setOf("" + TestObject.VALUE1.ordinal, "" + TestObject.VALUE2.ordinal, "" + TestObject.VALUE3.ordinal)
        // execute
        val actual = findSet(TestObject.values(), ordinals, TestObject.VALUE2)
        // validate
        validate(expected, actual)
    }

    @Test
    fun testFindSetUsingOrdinalsWithEmptyInput() {
        // setup
        val expected = TestObject.values().toSet()
        // execute
        val actual = findSet(TestObject.values(), setOf(), TestObject.VALUE2)
        // validate
        validate(expected, actual)
    }

    @Test
    fun testFindSetUsingOrdinalsWithInvalidInput() {
        // setup
        val expected = TestObject.VALUE2
        val ordinals: Set<String> = setOf("-1")
        // execute
        val actual = findSet(TestObject.values(), ordinals, expected)
        // validate
        assertEquals(1, actual.size)
        assertTrue(actual.contains(expected))
    }

    @Test
    fun testFindOneUsingIndex() {
        TestObject.values().forEach {
            // execute
            val actual = findOne(TestObject.values(), it.ordinal, TestObject.VALUE2)
            // validate
            assertEquals(it, actual)
        }
    }

    @Test
    fun testFindOneUsingInvalidLowIndex() {
        // setup
        val index = -1
        val expected = TestObject.VALUE3
        // execute
        val actual = findOne(TestObject.values(), index, expected)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testFindOneUsingInvalidHighIndex() {
        // setup
        val index = TestObject.values().size
        val expected = TestObject.VALUE3
        // execute
        val actual = findOne(TestObject.values(), index, expected)
        // validate
        assertEquals(expected, actual)
    }

    private fun validate(expected: Collection<TestObject>, actual: Collection<TestObject>) {
        assertEquals(expected.size, actual.size)
        expected.forEach {
            assertTrue(actual.contains(it))
        }
    }

    private enum class TestObject {
        VALUE1, VALUE3, VALUE2
    }

}