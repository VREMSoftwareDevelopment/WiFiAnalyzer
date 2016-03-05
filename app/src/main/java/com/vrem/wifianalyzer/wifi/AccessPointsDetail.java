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

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.model.Security;
import com.vrem.wifianalyzer.wifi.model.Strength;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.apache.commons.lang3.StringUtils;

public class AccessPointsDetail {
    public void setView(@NonNull Resources resources, @NonNull View view, @NonNull WiFiDetail wiFiDetail, boolean child) {
        ((TextView) view.findViewById(R.id.ssid)).setText(wiFiDetail.getTitle());

        TextView textIPAddress = (TextView) view.findViewById(R.id.ipAddress);
        String ipAddress = wiFiDetail.getWiFiAdditional().getIPAddress();
        if (StringUtils.isBlank(ipAddress)) {
            textIPAddress.setVisibility(View.GONE);
        } else {
            textIPAddress.setVisibility(View.VISIBLE);
            textIPAddress.setText(ipAddress);
        }

        ImageView configuredImage = (ImageView) view.findViewById(R.id.configuredImage);
        if (wiFiDetail.getWiFiAdditional().isConfiguredNetwork()) {
            configuredImage.setVisibility(View.VISIBLE);
            configuredImage.setColorFilter(resources.getColor(R.color.connected));
        } else {
            configuredImage.setVisibility(View.GONE);
        }

        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        Strength strength = wiFiSignal.getStrength();
        ImageView imageView = (ImageView) view.findViewById(R.id.levelImage);
        imageView.setImageResource(strength.imageResource());
        imageView.setColorFilter(resources.getColor(strength.colorResource()));

        Security security = wiFiDetail.getSecurity();
        ImageView securityImage = (ImageView) view.findViewById(R.id.securityImage);
        securityImage.setImageResource(security.imageResource());
        securityImage.setColorFilter(resources.getColor(R.color.icons_color));

        TextView textLevel = (TextView) view.findViewById(R.id.level);
        textLevel.setText(String.format("%ddBm", wiFiSignal.getLevel()));
        textLevel.setTextColor(resources.getColor(strength.colorResource()));

        ((TextView) view.findViewById(R.id.channel)).setText(String.format("%d", wiFiSignal.getChannel()));
        ((TextView) view.findViewById(R.id.frequency)).setText(String.format("%dMHz", wiFiSignal.getFrequency()));
        ((TextView) view.findViewById(R.id.distance)).setText(String.format("%6.2fm", wiFiSignal.getDistance()));
        ((TextView) view.findViewById(R.id.capabilities)).setText(wiFiDetail.getCapabilities());

        TextView textVendor = ((TextView) view.findViewById(R.id.vendor));
        String vendor = wiFiDetail.getWiFiAdditional().getVendorName();
        if (StringUtils.isBlank(vendor)) {
            textVendor.setVisibility(View.GONE);
        } else {
            textVendor.setVisibility(View.VISIBLE);
            textVendor.setText(vendor);
        }

        if (child) {
            view.findViewById(R.id.tab).setVisibility(View.VISIBLE);
            view.setBackgroundColor(resources.getColor(R.color.shadow_mid_color));
        } else {
            view.findViewById(R.id.tab).setVisibility(View.GONE);
        }

    }
}
