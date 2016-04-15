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
import com.vrem.wifianalyzer.RobolectricUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.filters.StringInputStream;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
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

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class RemoteCallTest {
    private final static String MAC_ADDRESS = "00:23:AB:7B:58:99";
    private static final String VENDOR_NAME = "CISCO SYSTEMS, INC.";

    private Database database;
    private Logger logger;
    private URLConnection urlConnection;
    private RemoteCall fixture;

    @Before
    public void setUp() throws Exception {
        RobolectricUtil.INSTANCE.getMainActivity();

        database = mock(Database.class);
        logger = mock(Logger.class);
        urlConnection = mock(URLConnection.class);


        fixture = new RemoteCall() {
            @Override
            protected URLConnection getURLConnection(String request) throws IOException {
                String expectedRequest = String.format(MAX_VENDOR_LOOKUP, MAC_ADDRESS.replace(":", "-"));
                assertEquals(expectedRequest, request);
                return urlConnection;
            }
        };
        fixture.setDatabase(database);
        fixture.setLogger(logger);
    }

    @Test
    public void testDoInBackground() throws Exception {
        // setup
        String expected = "This\nis\nexpected\nresults\n";
        when(urlConnection.getInputStream()).thenReturn(new StringInputStream(expected));
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
        String macAddress = MAC_ADDRESS.replace(":", "");
        String result = "[{\"startHex\":\"" + macAddress + "\",\"company\":\"" + VENDOR_NAME + "\"}]";
        // execute
        fixture.onPostExecute(result);
        // validate
        verify(logger).info(fixture, macAddress + " " + VENDOR_NAME);
        verify(database).insert(macAddress, VENDOR_NAME);
    }

    @Test
    public void testOnPostExecuteWithEmptyMacAddress() throws Exception {
        // setup
        String macAddress = StringUtils.EMPTY;
        String result = "[{\"startHex\":\"" + macAddress + "\",\"company\":\"" + VENDOR_NAME + "\"}]";
        // execute
        fixture.onPostExecute(result);
        // validate
        verify(logger, never()).info(fixture, macAddress + " " + VENDOR_NAME);
        verify(database, never()).insert(macAddress, VENDOR_NAME);
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
        String result = "{\"name\":\"123-XYZ\"}";
        // execute
        fixture.onPostExecute(result);
        // validate
        verify(logger).error(eq(fixture), eq(result), any(JSONException.class));
        verify(database, never()).insert(any(String.class), any(String.class));
    }
}