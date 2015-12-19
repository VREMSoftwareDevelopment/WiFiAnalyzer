/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.vendor;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;

import com.vrem.wifianalyzer.wifi.DetailsInfo;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Remote extends AsyncTask<List<DetailsInfo>, Void, Map<Integer, String>> {

    private static final String MAX_VENDOR_LOOKUP = "http://www.macvendorlookup.com/api/v2/%s";
    private List<DetailsInfo> detailsInfoList;

    private String getResult(String mac) {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        try {
            url = new URL(String.format(MAX_VENDOR_LOOKUP, mac.replace(":", "-")));
            httpURLConnection = (HttpURLConnection) url.openConnection();

            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = StringUtils.EMPTY;
            while ((tmp = bufferedReader.readLine()) != null) {
                json.append(tmp).append("\n");
            }
            bufferedReader.close();
            return json.toString();
        } catch (Exception e) {
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    protected Map<Integer, String> doInBackground(List<DetailsInfo> ... params) {
        Map<Integer, String> results = new HashMap<>();
        detailsInfoList = params[0];
        for (int i = 0; i < detailsInfoList.size(); i++) {
            results.put(new Integer(i), getResult(detailsInfoList.get(i).getBSSID()));
        }
        return results;
    }

    @Override
    protected void onPostExecute(Map<Integer, String> result) {
        for (Integer key : result.keySet()) {
            String value = result.get(key);
            Log.i(">>>", value);
        }
    }
}
