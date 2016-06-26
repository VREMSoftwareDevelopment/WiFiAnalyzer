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

import android.support.annotation.NonNull;
import android.view.View;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.scanner.UpdateNotifier;

import java.util.List;

public class ConnectionView implements UpdateNotifier {
    private final MainActivity mainActivity;
    private AccessPointsDetail accessPointsDetail;

    public ConnectionView(@NonNull MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        setAccessPointsDetail(new AccessPointsDetail());
        MainContext.INSTANCE.getScanner().addUpdateNotifier(this);
    }

    @Override
    public void update(@NonNull WiFiData wiFiData) {
        setConnectionVisibility(wiFiData);
        setNoDataVisibility(wiFiData);
    }

    private void setNoDataVisibility(@NonNull WiFiData wiFiData) {
        int noDataVisibility = View.GONE;
        int noDataGeoVisibility = View.GONE;
        if (mainActivity.getNavigationMenuView().getCurrentNavigationMenu().isWiFiBandSwitchable()) {
            Settings settings = MainContext.INSTANCE.getSettings();
            List<WiFiDetail> wiFiDetails = wiFiData.getWiFiDetails(settings.getWiFiBand(), settings.getSortBy());
            if (wiFiDetails.isEmpty()) {
                noDataVisibility = View.VISIBLE;
                if (wiFiData.getWiFiDetails().isEmpty()) {
                    noDataGeoVisibility = View.VISIBLE;
                }
            }
        }
        mainActivity.findViewById(R.id.nodata).setVisibility(noDataVisibility);
        mainActivity.findViewById(R.id.nodatageo).setVisibility(noDataGeoVisibility);
    }

    private void setConnectionVisibility(@NonNull WiFiData wiFiData) {
        WiFiDetail connection = wiFiData.getConnection();
        View connectionView = mainActivity.findViewById(R.id.connection);
        if (connection.getWiFiAdditional().isConnected()) {
            connectionView.setVisibility(View.VISIBLE);
            accessPointsDetail.setView(mainActivity.getResources(), connectionView, connection, false, MainContext.INSTANCE.getConfiguration().isLargeScreenLayout());
        } else {
            connectionView.setVisibility(View.GONE);
        }
    }

    protected void setAccessPointsDetail(@NonNull AccessPointsDetail accessPointsDetail) {
        this.accessPointsDetail = accessPointsDetail;
    }
}
