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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CacheTest {
    @Mock
    private RemoteCall remoteCall;

    private Cache fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Cache.INSTANCE;
        fixture.setRemoteCall(remoteCall);
    }

    @After
    public void tearDown() throws Exception {
        fixture.clear();
    }

    @Test
    public void testFind() throws Exception {
        // execute
        VendorData actual = fixture.find(VendorDataHelper.MAC_IN_RANGE1);
        // validate
        assertEquals(VendorData.EMPTY, actual);
        assertSame(VendorData.EMPTY, actual);

        verify(remoteCall).execute(VendorDataHelper.MAC_IN_RANGE1);
    }

    @Test
    public void testFindWithMacAddressesBelongToSameCompany() throws Exception {
        // setup
        fixture.find(VendorDataHelper.MAC_IN_RANGE1);
        // execute
        VendorData actual = fixture.find(VendorDataHelper.MAC_IN_RANGE2);
        fixture.find(VendorDataHelper.MAC_IN_RANGE2.toLowerCase());
        // validate
        assertEquals(VendorData.EMPTY, actual);
        assertSame(VendorData.EMPTY, actual);

        verify(remoteCall, never()).execute(VendorDataHelper.MAC_IN_RANGE2);
        verify(remoteCall, never()).execute(VendorDataHelper.MAC_IN_RANGE2.toLowerCase());
    }

    @Test
    public void testFindWithDataAddedToCache() throws Exception {
        // setup
        VendorData expected = VendorDataHelper.make();
        fixture.add(VendorDataHelper.MAC_IN_RANGE1, expected);
        // execute
        VendorData actual = fixture.find(VendorDataHelper.MAC_IN_RANGE1);
        // validate
        assertEquals(expected, actual);
        assertSame(expected, actual);

        verify(remoteCall, never()).execute(VendorDataHelper.MAC_IN_RANGE1);
    }

    @Test
    public void testFindWithDataNotAddedToCacheForTheSameVendor() throws Exception {
        // setup
        VendorData expected = VendorDataHelper.make();
        fixture.add(VendorDataHelper.MAC_IN_RANGE1, expected);
        // execute
        VendorData actual = fixture.find(VendorDataHelper.MAC_IN_RANGE2);
        // validate
        assertEquals(expected, actual);
        assertSame(expected, actual);

        verify(remoteCall, never()).execute(VendorDataHelper.MAC_IN_RANGE2);
    }
}