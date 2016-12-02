/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi;

import com.vrem.wifianalyzer.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccessPointViewTest {
    @Test
    public void testAccessPointViewNumber() throws Exception {
        assertEquals(2, AccessPointView.values().length);
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(AccessPointView.FULL, AccessPointView.find(-1));
        assertEquals(AccessPointView.FULL, AccessPointView.find(AccessPointView.values().length));

        assertEquals(AccessPointView.FULL, AccessPointView.find(AccessPointView.FULL.ordinal()));
        assertEquals(AccessPointView.COMPACT, AccessPointView.find(AccessPointView.COMPACT.ordinal()));
    }

    @Test
    public void testGetLayout() throws Exception {
        assertEquals(R.layout.access_point_view_full, AccessPointView.FULL.getLayout());
        assertEquals(R.layout.access_point_view_compact, AccessPointView.COMPACT.getLayout());
    }

    @Test
    public void testIsFull() throws Exception {
        assertTrue(AccessPointView.FULL.isFull());
        assertFalse(AccessPointView.COMPACT.isFull());
    }

    @Test
    public void testIsCompact() throws Exception {
        assertFalse(AccessPointView.FULL.isCompact());
        assertTrue(AccessPointView.COMPACT.isCompact());
    }

}