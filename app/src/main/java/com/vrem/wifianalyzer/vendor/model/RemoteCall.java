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

import android.os.AsyncTask;

import com.vrem.wifianalyzer.MainContext;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

class RemoteCall extends AsyncTask<String, Void, RemoteResult> {
    static final String MAC_VENDOR_LOOKUP = "http://api.macvendors.com/%s";

    @Override
    protected RemoteResult doInBackground(String... params) {
        if (params == null || params.length < 1 || StringUtils.isBlank(params[0])) {
            return RemoteResult.EMPTY;
        }
        String macAddress = params[0];
        String request = String.format(MAC_VENDOR_LOOKUP, macAddress.substring(0, macAddress.length() / 2));
        BufferedReader bufferedReader = null;
        try {
            URLConnection urlConnection = getURLConnection(request);
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            String vendorName = VendorNameUtils.cleanVendorName(response.toString().trim());
            if (StringUtils.isNotBlank(vendorName)) {
                return new RemoteResult(macAddress, vendorName);
            }
            return RemoteResult.EMPTY;
        } catch (Exception e) {
            return RemoteResult.EMPTY;
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

    URLConnection getURLConnection(String request) throws IOException {
        return new URL(request).openConnection();
    }

    @Override
    protected void onPostExecute(RemoteResult result) {
        String macAddress = result.getMacAddress();
        String vendorName = result.getVendorName();
        if (StringUtils.isNotBlank(vendorName) && StringUtils.isNotBlank(macAddress)) {
            Database database = MainContext.INSTANCE.getDatabase();
            database.insert(macAddress, vendorName);
        }
    }
}
