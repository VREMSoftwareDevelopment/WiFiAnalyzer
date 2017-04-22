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

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.filters.StringInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.URLConnection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("AnonymousInnerClass")
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RemoteCallTest {
    private static final String MAC_ADDRESS = "00:23:AB:7B:58:99";
    private static final String VENDOR_NAME = "CISCO SYSTEMS, INC.";
    private static final String LONG_VENDOR_NAME = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";

    private Database database;
    private URLConnection urlConnection;
    private RemoteCall fixture;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();

        database = MainContextHelper.INSTANCE.getDatabase();

        urlConnection = mock(URLConnection.class);

        fixture = new RemoteCall() {
            @Override
            protected URLConnection getURLConnection(String request) throws IOException {
                String expectedRequest = String.format(MAC_VENDOR_LOOKUP, MAC_ADDRESS.substring(0, MAC_ADDRESS.length() / 2));
                assertEquals(expectedRequest, request);
                return urlConnection;
            }
        };
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testDoInBackground() throws Exception {
        // setup
        RemoteResult expected = new RemoteResult(MAC_ADDRESS, VendorNameUtils.cleanVendorName(VENDOR_NAME));
        when(urlConnection.getInputStream()).thenReturn(new StringInputStream(VENDOR_NAME));
        // execute
        RemoteResult actual = fixture.doInBackground(MAC_ADDRESS);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testDoInBackgroundWithNull() throws Exception {
        // execute
        RemoteResult actual = fixture.doInBackground((String) null);
        // validate
        assertEquals(RemoteResult.EMPTY, actual);
    }

    @Test
    public void testDoInBackgroundWithEmpty() throws Exception {
        // execute
        RemoteResult actual = fixture.doInBackground(StringUtils.EMPTY);
        // validate
        assertEquals(RemoteResult.EMPTY, actual);
    }

    @Test
    public void testDoInBackgroundWithException() throws Exception {
        // setup
        when(urlConnection.getInputStream()).thenThrow(new RuntimeException());
        // execute
        RemoteResult actual = fixture.doInBackground(MAC_ADDRESS);
        // validate
        assertEquals(RemoteResult.EMPTY, actual);
    }

    @Test
    public void testDoInBackgroundWithVeryLongResponse() throws Exception {
        // setup
        RemoteResult expected = new RemoteResult(MAC_ADDRESS, VendorNameUtils.cleanVendorName(LONG_VENDOR_NAME));
        when(urlConnection.getInputStream()).thenReturn(new StringInputStream(LONG_VENDOR_NAME));
        // execute
        RemoteResult actual = fixture.doInBackground(MAC_ADDRESS);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testOnPostExecute() throws Exception {
        // setup
        RemoteResult result = new RemoteResult(MAC_ADDRESS, VENDOR_NAME);
        // execute
        fixture.onPostExecute(result);
        // validate
        verify(database).insert(MAC_ADDRESS, VENDOR_NAME);
    }

    @Test
    public void testOnPostExecuteWithEmptyMacAddress() throws Exception {
        // setup
        RemoteResult result = new RemoteResult(StringUtils.EMPTY, VENDOR_NAME);
        // execute
        fixture.onPostExecute(result);
        // validate
        verify(database, never()).insert(StringUtils.EMPTY, VENDOR_NAME);
    }

    @Test
    public void testOnPostExecuteWithEmptyResult() throws Exception {
        // execute
        fixture.onPostExecute(RemoteResult.EMPTY);
        // validate
        verify(database, never()).insert(any(String.class), any(String.class));
    }

}