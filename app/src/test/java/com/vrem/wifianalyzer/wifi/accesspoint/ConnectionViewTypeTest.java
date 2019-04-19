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

package com.vrem.wifianalyzer.wifi.accesspoint;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ConnectionViewTypeTest {
    @Test
    public void testConnectionViewTypeCount() {
        assertEquals(3, ConnectionViewType.values().length);
    }

    @Test
    public void testGetLayout() {
        assertEquals(AccessPointViewType.COMPLETE, ConnectionViewType.COMPLETE.getAccessPointViewType());
        assertEquals(AccessPointViewType.COMPACT, ConnectionViewType.COMPACT.getAccessPointViewType());
        assertNull(ConnectionViewType.HIDE.getAccessPointViewType());
    }

    @Test
    public void testIsHide() {
        assertFalse(ConnectionViewType.COMPLETE.isHide());
        assertFalse(ConnectionViewType.COMPACT.isHide());
        assertTrue(ConnectionViewType.HIDE.isHide());
    }

}