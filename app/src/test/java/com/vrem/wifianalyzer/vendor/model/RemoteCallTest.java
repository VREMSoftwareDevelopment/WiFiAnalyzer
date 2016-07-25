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

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.Logger;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.filters.StringInputStream;
import org.json.JSONException;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RemoteCallTest {
    private static final String MAC_ADDRESS = "00:23:AB:7B:58:99";
    private static final String VENDOR_NAME = "CISCO SYSTEMS, INC.";

    private Database database;
    private Logger logger;
    private URLConnection urlConnection;
    private RemoteCall fixture;

    @Before
    public void setUp() throws Exception {
        RobolectricUtil.INSTANCE.getMainActivity();

        database = MainContextHelper.INSTANCE.getDatabase();
        logger = MainContextHelper.INSTANCE.getLogger();

        urlConnection = mock(URLConnection.class);

        fixture = new RemoteCall() {
            @Override
            protected URLConnection getURLConnection(String request) throws IOException {
                String expectedRequest = String.format(MAX_VENDOR_LOOKUP, MAC_ADDRESS);
                assertEquals(expectedRequest, request);
                return urlConnection;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testDoInBackground() throws Exception {
        // setup
        String expected = new RemoteResult(MAC_ADDRESS, VENDOR_NAME).toJson();
        when(urlConnection.getInputStream()).thenReturn(new StringInputStream(VENDOR_NAME));
        // execute
        String actual = fixture.doInBackground(MAC_ADDRESS);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testDoInBackgroundWithNull() throws Exception {
        // execute
        String actual = fixture.doInBackground((String) null);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testDoInBackgroundWithEmpty() throws Exception {
        // execute
        String actual = fixture.doInBackground(StringUtils.EMPTY);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testDoInBackgroundWithException() throws Exception {
        // setup
        when(urlConnection.getInputStream()).thenThrow(new RuntimeException());
        // execute
        String actual = fixture.doInBackground(MAC_ADDRESS);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testOnPostExecute() throws Exception {
        // setup
        String result = new RemoteResult(MAC_ADDRESS, VENDOR_NAME).toJson();
        // execute
        fixture.onPostExecute(result);
        // validate
        verify(logger).info(fixture, MAC_ADDRESS + " " + VENDOR_NAME);
        verify(database).insert(MAC_ADDRESS, VENDOR_NAME);
    }

    @Test
    public void testOnPostExecuteWithEmptyMacAddress() throws Exception {
        // setup
        String result = new RemoteResult(StringUtils.EMPTY, VENDOR_NAME).toJson();
        // execute
        fixture.onPostExecute(result);
        // validate
        verify(logger, never()).info(fixture, StringUtils.EMPTY + " " + VENDOR_NAME);
        verify(database, never()).insert(StringUtils.EMPTY, VENDOR_NAME);
    }

    @Test
    public void testOnPostExecuteWithEmptyResult() throws Exception {
        // execute
        fixture.onPostExecute(StringUtils.EMPTY);
        // validate
        verify(database, never()).insert(any(String.class), any(String.class));
    }

    @Test
    public void testOnPostExecuteWithJSONException() throws Exception {
        // setup
        String result = "Not a JSON";
        // execute
        fixture.onPostExecute(result);
        // validate
        verify(logger).error(eq(fixture), eq(result), any(JSONException.class));
        verify(database, never()).insert(any(String.class), any(String.class));
    }
}