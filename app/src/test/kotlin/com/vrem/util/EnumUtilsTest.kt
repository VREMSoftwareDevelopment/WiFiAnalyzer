/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class EnumUtilsTest {

    @Test
    fun ordinals() {
        // setup
        val expected = TestObject.entries
        // execute
        val actual = ordinals(TestObject.entries)
        // validate
        assertThat(actual).hasSize(expected.size)
        expected.forEach {
            assertThat(actual).contains("" + it.ordinal)
        }
    }

    @Test
    fun findSetUsingOrdinals() {
        // setup
        val expected = TestObject.entries.toSet()
        val ordinals: Set<String> = setOf("" + TestObject.VALUE1.ordinal, "" + TestObject.VALUE2.ordinal, "" + TestObject.VALUE3.ordinal)
        // execute
        val actual = findSet(TestObject.entries, ordinals, TestObject.VALUE2)
        // validate
        validate(expected, actual)
    }

    @Test
    fun findSetUsingOrdinalsWithEmptyInput() {
        // setup
        val expected = TestObject.entries.toSet()
        // execute
        val actual = findSet(TestObject.entries, setOf(), TestObject.VALUE2)
        // validate
        validate(expected, actual)
    }

    @Test
    fun findSetUsingOrdinalsWithInvalidInput() {
        // setup
        val expected = TestObject.VALUE2
        val ordinals: Set<String> = setOf("-1")
        // execute
        val actual = findSet(TestObject.entries, ordinals, expected)
        // validate
        assertThat(actual).hasSize(1)
        assertThat(actual).contains(expected)
    }

    @Test
    fun findOneUsingIndex() {
        TestObject.entries.forEach {
            // execute
            val actual = findOne(TestObject.entries, it.ordinal, TestObject.VALUE2)
            // validate
            assertThat(actual).isEqualTo(it)
        }
    }

    @Test
    fun findOneUsingInvalidLowIndex() {
        // setup
        val index = -1
        val expected = TestObject.VALUE3
        // execute
        val actual = findOne(TestObject.entries, index, expected)
        // validate
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun findOneUsingInvalidHighIndex() {
        // setup
        val index = TestObject.entries.size
        val expected = TestObject.VALUE3
        // execute
        val actual = findOne(TestObject.entries, index, expected)
        // validate
        assertThat(actual).isEqualTo(expected)
    }

    private fun validate(expected: Collection<TestObject>, actual: Collection<TestObject>) {
        assertThat(actual).hasSize(expected.size)
        expected.forEach {
            assertThat(actual).contains(it)
        }
    }

    private enum class TestObject {
        VALUE1, VALUE3, VALUE2
    }

}