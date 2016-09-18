/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.wifianalyzer.Logger;
import com.vrem.wifianalyzer.MainContext;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

class RemoteCall extends AsyncTask<String, Void, String> {
    static final String MAC_VENDOR_LOOKUP = "http://api.macvendors.com/%s";

    @Override
    protected String doInBackground(String... params) {
        if (params == null || params.length < 1 || StringUtils.isBlank(params[0])) {
            return StringUtils.EMPTY;
        }
        String macAddress = params[0];
        String request = String.format(MAC_VENDOR_LOOKUP, macAddress);
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
                return new RemoteResult(macAddress, vendorName).toJson();
            }
            return StringUtils.EMPTY;
        } catch (Exception e) {
            return StringUtils.EMPTY;
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
    protected void onPostExecute(String result) {
        if (StringUtils.isNotBlank(result)) {
            Logger logger = MainContext.INSTANCE.getLogger();
            try {
                RemoteResult remoteResult = new RemoteResult(result);
                String macAddress = remoteResult.getMacAddress();
                String vendorName = remoteResult.getVendorName();
                if (StringUtils.isNotBlank(vendorName) && StringUtils.isNotBlank(macAddress)) {
                    logger.info(this, macAddress + " " + vendorName);
                    Database database = MainContext.INSTANCE.getDatabase();
                    database.insert(macAddress, vendorName);
                }
            } catch (JSONException e) {
                logger.error(this, result, e);
            }
        }
    }
}
