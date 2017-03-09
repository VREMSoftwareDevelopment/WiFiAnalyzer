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

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.Assert.assertEquals;

public class MacToVendorITest {

    @Test
    public void testMacVendorsComShort() throws Exception {
        // setup
        String macAddress = "00:23:AB";
        String expected = "CISCO SYSTEMS, INC";
        String url = "http://api.macvendors.com/";
        // execute
        String actual = execute(url + macAddress);
        // validate
/*
        System.out.println(">>> Request:" + url + macAddress);
        System.out.println(">>> Response:" + actual);
*/
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
            return response.toString().toUpperCase();
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