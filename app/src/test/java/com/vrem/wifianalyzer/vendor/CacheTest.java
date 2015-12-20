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

import org.apache.commons.lang3.StringUtils;
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
    static final String MAC_IN_RANGE1 = "00:23:AB:8C:DF:10";
    static final String MAC_IN_RANGE2 = "00:23:AB:00:DF:1C";

    static final String MAC_NOT_IN_RANGE1 = "00:23:AA:FF:FF:FF";
    static final String MAC_NOT_IN_RANGE2 = "00:23:AC:00:00:00";

    static final String MAC_ADDRESS = "0023AB";
    static final String COMPANY_NAME = "CISCO SYSTEMS, INC.";

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
        String actual = fixture.find(MAC_IN_RANGE1);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
        assertSame(StringUtils.EMPTY, actual);

        verify(remoteCall).execute(MAC_IN_RANGE1);
    }

    @Test
    public void testFindWithMacAddressesBelongToSameCompany() throws Exception {
        // setup
        fixture.find(MAC_IN_RANGE1);
        // execute
        String actual = fixture.find(MAC_IN_RANGE2);
        fixture.find(MAC_IN_RANGE2.toLowerCase());
        // validate
        assertEquals(StringUtils.EMPTY, actual);
        assertSame(StringUtils.EMPTY, actual);

        verify(remoteCall, never()).execute(MAC_IN_RANGE2);
        verify(remoteCall, never()).execute(MAC_IN_RANGE2.toLowerCase());
    }

    @Test
    public void testFindWithDataAddedToCache() throws Exception {
        // setup
        fixture.add(MAC_IN_RANGE1, COMPANY_NAME);
        // execute
        String actual = fixture.find(MAC_IN_RANGE1);
        // validate
        assertEquals(COMPANY_NAME, actual);
        assertSame(COMPANY_NAME, actual);

        verify(remoteCall, never()).execute(MAC_IN_RANGE1);
    }

    @Test
    public void testFindWithDataNotAddedToCacheForTheSameVendor() throws Exception {
        // setup
        fixture.add(MAC_IN_RANGE1, COMPANY_NAME);
        // execute
        String actual = fixture.find(MAC_IN_RANGE2);
        // validate
        assertEquals(COMPANY_NAME, actual);
        assertSame(COMPANY_NAME, actual);

        verify(remoteCall, never()).execute(MAC_IN_RANGE2);
    }
}