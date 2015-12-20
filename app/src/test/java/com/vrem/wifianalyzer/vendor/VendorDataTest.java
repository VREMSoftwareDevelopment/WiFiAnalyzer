/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.vrem.wifianalyzer.vendor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class VendorDataTest {
    private VendorData fixture;

    @Before
    public void setUp() throws Exception {
        fixture = VendorDataHelper.make();
    }

    @Test
    public void testGetMacStart() throws Exception {
        assertEquals(VendorDataHelper.MAC_START, fixture.getMacAddressStart());
    }

    @Test
    public void testGetMacEnd() throws Exception {
        assertEquals(VendorDataHelper.MAC_END, fixture.getMacAddressEnd());
    }

    @Test
    public void testGetCompanyName() throws Exception {
        assertEquals(VendorDataHelper.COMPANY_NAME, fixture.getCompanyName());
    }

    @Test
    public void testCompareTo() throws Exception {
        VendorData other = VendorDataHelper.make();
        assertEquals(0, fixture.compareTo(other));
    }

    @Test
    public void testEqualsTrue() throws Exception {
        VendorData other = VendorDataHelper.make();
        assertEquals(fixture, other);
        assertNotSame(fixture, other);
    }

    @Test
    public void testHashCode() throws Exception {
        VendorData other = VendorDataHelper.make();
        assertEquals(fixture.hashCode(), other.hashCode());
    }

    @Test
    public void testInRange() throws Exception {
        assertTrue(fixture.inRange(VendorDataHelper.MAC_START));
        assertTrue(fixture.inRange(VendorDataHelper.MAC_IN_RANGE1));
        assertTrue(fixture.inRange(VendorDataHelper.MAC_IN_RANGE2));
        assertTrue(fixture.inRange(VendorDataHelper.MAC_END));

        assertFalse(fixture.inRange(VendorDataHelper.MAC_START_NOT_IN_RANGE));
        assertFalse(fixture.inRange(VendorDataHelper.MAC_END_NOT_IN_RANGE));
    }


}