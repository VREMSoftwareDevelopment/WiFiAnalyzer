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

import com.vrem.wifianalyzer.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccessPointViewTypeTest {
    @Test
    public void testAccessPointViewCount() {
        assertEquals(2, AccessPointViewType.values().length);
    }

    @Test
    public void testGetLayout() {
        assertEquals(R.layout.access_point_view_complete, AccessPointViewType.COMPLETE.getLayout());
        assertEquals(R.layout.access_point_view_compact, AccessPointViewType.COMPACT.getLayout());
    }

    @Test
    public void testIsFull() {
        assertTrue(AccessPointViewType.COMPLETE.isFull());
        assertFalse(AccessPointViewType.COMPACT.isFull());
    }

    @Test
    public void testIsCompact() {
        assertFalse(AccessPointViewType.COMPLETE.isCompact());
        assertTrue(AccessPointViewType.COMPACT.isCompact());
    }

}