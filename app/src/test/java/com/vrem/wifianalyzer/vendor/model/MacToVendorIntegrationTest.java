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

import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MacToVendorIntegrationTest {
    private static final String MAC_ADDRESS = "00:23:AB:8C:DF:10";

    @Ignore
    @Test
    public void testMacVendorLookup() throws Exception {
        String request = String.format("http://www.macvendorlookup.com/api/v2/%s", MAC_ADDRESS.replace(":", "-"));
        System.out.println();
        System.out.println(">>> Request:" + request);
        String response = execute(request);
        System.out.println(">>> Response:" + response);
    }

    @Ignore
    @Test
    public void testMacVendorsCom() throws Exception {
        String request = String.format("http://api.macvendors.com/%s", MAC_ADDRESS);
        System.out.println(">>> Request:" + request);
        String response = execute(request);
        System.out.println(">>> Response:" + response);
    }

    @Ignore
    @Test
    public void testMacVendorsCo() throws Exception {
        String request = String.format("http://macvendors.co/api/%s", MAC_ADDRESS);
        System.out.println(">>> Request:" + request);
        String response = execute(request);
        System.out.println(">>> Response:" + response);
    }

    private String execute(@NonNull String request) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            URLConnection urlConnection = new URL(request).openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line).append("\n");
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