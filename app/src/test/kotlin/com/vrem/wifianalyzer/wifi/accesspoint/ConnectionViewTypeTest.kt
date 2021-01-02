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
package com.vrem.wifianalyzer.wifi.accesspoint

import com.vrem.wifianalyzer.R
import org.junit.Assert.*
import org.junit.Test

class ConnectionViewTypeTest {
    @Test
    fun testConnectionViewTypeCount() {
        assertEquals(3, ConnectionViewType.values().size)
    }

    @Test
    fun testGetLayout() {
        assertEquals(R.layout.access_point_view_complete, ConnectionViewType.COMPLETE.layout)
        assertEquals(R.layout.access_point_view_compact, ConnectionViewType.COMPACT.layout)
        assertEquals(R.layout.access_point_view_hide, ConnectionViewType.HIDE.layout)
    }

    @Test
    fun testIsHide() {
        assertFalse(ConnectionViewType.COMPLETE.hide)
        assertFalse(ConnectionViewType.COMPACT.hide)
        assertTrue(ConnectionViewType.HIDE.hide)
    }

}