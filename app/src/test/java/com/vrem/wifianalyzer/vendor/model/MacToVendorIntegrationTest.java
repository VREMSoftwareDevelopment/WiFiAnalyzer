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

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.Assert.assertEquals;

public class MacToVendorIntegrationTest {

    @Test
    public void testMacVendorsCom() throws Exception {
        // setup
        String expected = "CISCO SYSTEMS, INC";
        String macAddress = "00:23:AB:8C:DF:10";
        String url = "http://api.macvendors.com/";
        // execute
        String actual = execute(url + macAddress);
        // validate
        System.out.println(">>> Request:" + url + macAddress);
        System.out.println(">>> Response:" + actual);
        assertEquals(expected, actual);
    }

    private String execute(@NonNull String request) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            URLConnection urlConnection = new URL(request).openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

}