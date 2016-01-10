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
package com.vrem.wifianalyzer.wifi;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetails;

public class ConnectionView implements UpdateNotifier {
    private final Activity activity;
    private final MainContext mainContext = MainContext.INSTANCE;

    public ConnectionView(@NonNull Activity activity) {
        this.activity = activity;
        mainContext.getScanner().addUpdateNotifier(this);
    }

    @Override
    public void update(WiFiData wifiData) {
        View view = activity.findViewById(R.id.connection);

        WiFiDetails connection = wifiData.getConnection();
        if (connection != null && connection.isConnected()) {
            view.setVisibility(View.VISIBLE);
            AccessPointsDetails.setView(activity.getResources(), view, connection);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
