/*
 * WiFiAnalyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

    @Mock
    private RemoteCall remoteCall;

    private Database database;
    private VendorService fixture;

    @Before
    public void setUp() {
        database = MainContextHelper.INSTANCE.getDatabase();
        fixture = new VendorService();
        fixture.setRemoteCall(remoteCall);
    }

    @After
    public void tearDown() {
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
        List<VendorData> vendorData = withVendorData();
        when(database.findAll()).thenReturn(vendorData);
        // execute
        SortedMap<String, List<String>> actual = fixture.findAll();
        // validate
        verify(database).findAll();

        assertEquals(3, actual.size());
        assertEquals(1, actual.get(VendorNameUtils.cleanVendorName(vendorData.get(0).getName())).size());
        assertEquals(3, actual.get(VendorNameUtils.cleanVendorName(vendorData.get(1).getName())).size());
        assertEquals(1, actual.get(VendorNameUtils.cleanVendorName(vendorData.get(4).getName())).size());

        List<String> macs = actual.get(VendorNameUtils.cleanVendorName(vendorData.get(1).getName()));
        assertEquals(vendorData.get(3).getMac(), macs.get(0));
        assertEquals(vendorData.get(1).getMac(), macs.get(1));
        assertEquals(vendorData.get(2).getMac(), macs.get(2));
    }

    @NonNull
    private List<VendorData> withVendorData() {
        return Arrays.asList(
            new VendorData(3, VENDOR_NAME + " 3", "Mac3"),
            new VendorData(4, VENDOR_NAME + " 1", "Mac1-2"),
            new VendorData(1, VENDOR_NAME + " 1", "Mac1-3"),
            new VendorData(2, VENDOR_NAME + " 1", "Mac1-1"),
            new VendorData(5, VENDOR_NAME + " 2", "Mac2"));
    }

}