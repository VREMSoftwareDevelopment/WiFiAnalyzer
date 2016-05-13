/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.vendor.model;

import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.MainContextHelper;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VendorServiceTest {
    private static final String MAC_IN_RANGE1 = "00:23:AB:8C:DF:10";
    private static final String MAC_IN_RANGE2 = "00:23:AB:00:DF:1C";
    private static final String VENDOR_NAME = "CISCO SYSTEMS, INC.";
    private static final String EXPECTED_VENDOR_NAME = "CISCO SYSTEMS INC";

    @Mock private RemoteCall remoteCall;

    private Database database;
    private VendorService fixture;

    @Before
    public void setUp() throws Exception {
        database = MainContextHelper.INSTANCE.getDatabase();
        fixture = new VendorService();
        fixture.setRemoteCall(remoteCall);
    }

    @After
    public void tearDown() throws Exception {
        fixture.clear();
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testFindWithNameFound() throws Exception {
        // setup
        when(database.find(MAC_IN_RANGE1)).thenReturn(VENDOR_NAME);
        // execute
        String actual = fixture.findVendorName(MAC_IN_RANGE1);
        // validate
        assertEquals(EXPECTED_VENDOR_NAME, actual);
        verify(database).find(MAC_IN_RANGE1);
    }

    @Test
    public void testFindWithNameNotFound() throws Exception {
        // execute
        String actual = fixture.findVendorName(MAC_IN_RANGE1);
        when(database.find(MAC_IN_RANGE1)).thenReturn(null);
        // validate
        verify(database).find(MAC_IN_RANGE1);
        verify(remoteCall).execute(MAC_IN_RANGE1);

        assertEquals(StringUtils.EMPTY, actual);
        assertSame(StringUtils.EMPTY, actual);
    }

    @Test
    public void testFindWithMacAddressesBelongToSameVendor() throws Exception {
        // setup
        fixture.findVendorName(MAC_IN_RANGE1);
        // execute
        String actual = fixture.findVendorName(MAC_IN_RANGE2);
        fixture.findVendorName(MAC_IN_RANGE2.toLowerCase());
        // validate
        verify(database).find(MAC_IN_RANGE1);
        verify(remoteCall).execute(MAC_IN_RANGE1);
        verify(remoteCall, never()).execute(MAC_IN_RANGE2);
        verify(remoteCall, never()).execute(MAC_IN_RANGE2.toLowerCase());

        assertEquals(StringUtils.EMPTY, actual);
        assertSame(StringUtils.EMPTY, actual);
    }

    @Test
    public void testFindAll() throws Exception {
        // setup
        List<VendorData> vendorDatas = withVendorDatas();
        when(database.findAll()).thenReturn(vendorDatas);
        // execute
        SortedMap<String, List<String>> actual = fixture.findAll();
        // validate
        verify(database).findAll();

        assertEquals(3, actual.size());
        assertEquals(1, actual.get(vendorDatas.get(0).getName()).size());
        assertEquals(3, actual.get(vendorDatas.get(1).getName()).size());
        assertEquals(1, actual.get(vendorDatas.get(4).getName()).size());

        List<String> macs = actual.get(vendorDatas.get(1).getName());
        assertEquals(vendorDatas.get(3).getMac(), macs.get(0));
        assertEquals(vendorDatas.get(1).getMac(), macs.get(1));
        assertEquals(vendorDatas.get(2).getMac(), macs.get(2));
    }

    @NonNull
    private List<VendorData> withVendorDatas() {
        return Arrays.asList(
                new VendorData(3, "NAME3", "Mac3"),
                new VendorData(4, "NAME1", "Mac1-2"),
                new VendorData(1, "NAME1", "Mac1-3"),
                new VendorData(2, "NAME1", "Mac1-1"),
                new VendorData(5, "NAME2", "Mac2"));
    }

}