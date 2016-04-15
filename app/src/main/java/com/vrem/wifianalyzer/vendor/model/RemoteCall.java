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

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.Logger;
import com.vrem.wifianalyzer.MainContext;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

class RemoteCall extends AsyncTask<String, Void, String> {
    private Logger logger;
    private Database database;

    protected static final String MAX_VENDOR_LOOKUP = "https://www.macvendorlookup.com/api/v2/%s";

    protected String doInBackground(String... params) {
        if (params == null || params.length < 1 || StringUtils.isBlank(params[0])) {
            return StringUtils.EMPTY;
        }
        String macAddress = params[0];
        String request = String.format(MAX_VENDOR_LOOKUP, macAddress.replace(":", "-"));
        BufferedReader bufferedReader = null;
        try {
            URLConnection urlConnection = getURLConnection(request);
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString();
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

    protected URLConnection getURLConnection(String request) throws IOException {
        return new URL(request).openConnection();
    }

    @Override
    protected void onPostExecute(String result) {
        if (StringUtils.isNotBlank(result)) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                if (jsonArray.length() > 0) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String macAddress = getValue(jsonObject, "startHex");
                    String vendorName = getValue(jsonObject, "company");
                    if (StringUtils.isNotBlank(macAddress)) {
                        getLogger().info(this, macAddress + " " + vendorName);
                        getDatabase().insert(macAddress, vendorName);
                    }
                }
            } catch (JSONException e) {
                getLogger().error(this, result, e);
            }
        }
    }

    private String getValue(@NonNull JSONObject jsonObject, @NonNull String key) {
        try {
            String result = jsonObject.getString(key);
            return result == null ? StringUtils.EMPTY : result;
        } catch (JSONException e) {
            return StringUtils.EMPTY;
        }
    }

    // injectors start
    private Logger getLogger() {
        if (logger == null) {
            logger = MainContext.INSTANCE.getLogger();
        }
        return logger;
    }

    protected void setLogger(@NonNull Logger logger) {
        this.logger = logger;
    }

    private Database getDatabase() {
        if (database == null) {
            database = MainContext.INSTANCE.getDatabase();
        }
        return database;
    }

    protected void setDatabase(@NonNull Database database) {
        this.database = database;
    }
    // injectors end
}
