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

package com.vrem.wifianalyzer.wifi;

import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.scanner.UpdateNotifier;

import java.util.List;

public class ConnectionView implements UpdateNotifier {
    private final MainActivity mainActivity;
    private AccessPointDetail accessPointDetail;

    public ConnectionView(@NonNull MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        setAccessPointDetail(new AccessPointDetail());
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
        mainActivity.findViewById(R.id.nodatageourl).setVisibility(noDataGeoVisibility);
    }

    private void setConnectionVisibility(@NonNull WiFiData wiFiData) {
        WiFiDetail connection = wiFiData.getConnection();
        ViewGroup connectionView = (ViewGroup) mainActivity.findViewById(R.id.connection);
        WiFiConnection wiFiConnection = connection.getWiFiAdditional().getWiFiConnection();
        if (wiFiConnection.isConnected()) {
            connectionView.setVisibility(View.VISIBLE);
            setDetailView(connectionView);
            setViewDetails(connection, connectionView);
            setViewConnection(connectionView, wiFiConnection);
        } else {
            connectionView.setVisibility(View.GONE);
        }
    }

    private void setViewDetails(WiFiDetail connection, ViewGroup connectionView) {
        MainContext mainContext = MainContext.INSTANCE;
        APView apView = mainContext.getSettings().getAPView();
        Resources resources = mainContext.getResources();
        accessPointDetail.setView(apView.isCompact(), resources, connectionView, connection, false);
    }

    private void setDetailView(ViewGroup connectionView) {
        View view = connectionView.findViewById(R.id.connectionDetail);
        if (view != null) {
            MainContext mainContext = MainContext.INSTANCE;
            LayoutInflater layoutInflater = mainContext.getLayoutInflater();
            APView apView = mainContext.getSettings().getAPView();
            View newView = layoutInflater.inflate(apView.getLayout(), connectionView, false);
            int index = connectionView.indexOfChild(view);
            if (index > -1) {
                connectionView.removeView(view);
                connectionView.addView(newView, index);
            }
        }
    }

    private void setViewConnection(View connectionView, WiFiConnection wiFiConnection) {
        String ipAddress = wiFiConnection.getIpAddress();
        ((TextView) connectionView.findViewById(R.id.ipAddress)).setText(ipAddress);

        TextView textLinkSpeed = (TextView) connectionView.findViewById(R.id.linkSpeed);
        int linkSpeed = wiFiConnection.getLinkSpeed();
        if (linkSpeed == WiFiConnection.LINK_SPEED_INVALID) {
            textLinkSpeed.setVisibility(View.GONE);
        } else {
            textLinkSpeed.setVisibility(View.VISIBLE);
            textLinkSpeed.setText(linkSpeed + WifiInfo.LINK_SPEED_UNITS);
        }
    }

    void setAccessPointDetail(@NonNull AccessPointDetail accessPointDetail) {
        this.accessPointDetail = accessPointDetail;
    }
}
