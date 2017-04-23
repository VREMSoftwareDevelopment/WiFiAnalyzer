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

package com.vrem.wifianalyzer.wifi.accesspoint;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.model.Security;
import com.vrem.wifianalyzer.wifi.model.Strength;
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class AccessPointDetail {
    private static final int VENDOR_SHORT_MAX = 12;
    private static final int VENDOR_LONG_MAX = 30;

    View makeView(View convertView, ViewGroup parent, @NonNull WiFiDetail wiFiDetail, boolean isChild) {
        AccessPointViewType accessPointViewType = MainContext.INSTANCE.getSettings().getAccessPointView();
        return makeView(convertView, parent, wiFiDetail, isChild, accessPointViewType);
    }

    View makeView(View convertView, ViewGroup parent, @NonNull WiFiDetail wiFiDetail, boolean isChild, @NonNull AccessPointViewType accessPointViewType) {
        MainActivity mainActivity = MainContext.INSTANCE.getMainActivity();
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = mainActivity.getLayoutInflater();
            view = layoutInflater.inflate(accessPointViewType.getLayout(), parent, false);
        }
        setViewCompact(mainActivity, view, wiFiDetail, isChild);
        if (view.findViewById(R.id.capabilities) != null) {
            setViewExtra(mainActivity, view, wiFiDetail);
            setViewVendorShort(view, wiFiDetail.getWiFiAdditional());
        }

        return view;
    }

    public View makeViewPopup(@NonNull WiFiDetail wiFiDetail) {
        MainActivity mainActivity = MainContext.INSTANCE.getMainActivity();
        View view = mainActivity.getLayoutInflater().inflate(R.layout.access_point_view_popup, null);

        setViewCompact(mainActivity, view, wiFiDetail, false);
        setViewExtra(mainActivity, view, wiFiDetail);
        setViewVendorLong(view, wiFiDetail.getWiFiAdditional());

        return view;
    }

    private void setViewCompact(@NonNull Context context, @NonNull View view, @NonNull WiFiDetail wiFiDetail, boolean isChild) {
        ((TextView) view.findViewById(R.id.ssid)).setText(wiFiDetail.getTitle());

        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        Strength strength = wiFiSignal.getStrength();

        Security security = wiFiDetail.getSecurity();
        ImageView securityImage = (ImageView) view.findViewById(R.id.securityImage);
        securityImage.setImageResource(security.getImageResource());
        securityImage.setColorFilter(ContextCompat.getColor(context, R.color.icons_color));

        TextView textLevel = (TextView) view.findViewById(R.id.level);
        textLevel.setText(String.format(Locale.ENGLISH, "%ddBm", wiFiSignal.getLevel()));
        textLevel.setTextColor(ContextCompat.getColor(context, strength.colorResource()));

        ((TextView) view.findViewById(R.id.channel))
            .setText(wiFiSignal.getChannelDisplay());
        ((TextView) view.findViewById(R.id.primaryFrequency))
            .setText(String.format(Locale.ENGLISH, "%d%s",
                wiFiSignal.getPrimaryFrequency(), WiFiSignal.FREQUENCY_UNITS));
        ((TextView) view.findViewById(R.id.distance))
            .setText(String.format(Locale.ENGLISH, "%5.1fm", wiFiSignal.getDistance()));

        if (isChild) {
            view.findViewById(R.id.tab).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.tab).setVisibility(View.GONE);
        }
    }

    private void setViewExtra(@NonNull Context context, @NonNull View view, @NonNull WiFiDetail wiFiDetail) {
        ImageView configuredImage = (ImageView) view.findViewById(R.id.configuredImage);
        WiFiAdditional wiFiAdditional = wiFiDetail.getWiFiAdditional();
        if (wiFiAdditional.isConfiguredNetwork()) {
            configuredImage.setVisibility(View.VISIBLE);
            configuredImage.setColorFilter(ContextCompat.getColor(context, R.color.connected));
        } else {
            configuredImage.setVisibility(View.GONE);
        }

        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        Strength strength = wiFiSignal.getStrength();
        ImageView imageView = (ImageView) view.findViewById(R.id.levelImage);
        imageView.setImageResource(strength.imageResource());
        imageView.setColorFilter(ContextCompat.getColor(context, strength.colorResource()));

        ((TextView) view.findViewById(R.id.channel_frequency_range))
            .setText(wiFiSignal.getFrequencyStart() + " - " + wiFiSignal.getFrequencyEnd());
        ((TextView) view.findViewById(R.id.width))
            .setText("(" + wiFiSignal.getWiFiWidth().getFrequencyWidth() + WiFiSignal.FREQUENCY_UNITS + ")");
        ((TextView) view.findViewById(R.id.capabilities))
            .setText(wiFiDetail.getCapabilities());
    }

    private void setViewVendorShort(@NonNull View view, @NonNull WiFiAdditional wiFiAdditional) {
        TextView textVendorShort = ((TextView) view.findViewById(R.id.vendorShort));
        String vendor = wiFiAdditional.getVendorName();
        if (StringUtils.isBlank(vendor)) {
            textVendorShort.setVisibility(View.GONE);
        } else {
            textVendorShort.setVisibility(View.VISIBLE);
            textVendorShort.setText(vendor.substring(0, Math.min(VENDOR_SHORT_MAX, vendor.length())));
        }
    }

    private void setViewVendorLong(@NonNull View view, @NonNull WiFiAdditional wiFiAdditional) {
        TextView textVendor = ((TextView) view.findViewById(R.id.vendorLong));
        String vendor = wiFiAdditional.getVendorName();
        if (StringUtils.isBlank(vendor)) {
            textVendor.setVisibility(View.GONE);
        } else {
            textVendor.setVisibility(View.VISIBLE);
            textVendor.setText(vendor.substring(0, Math.min(VENDOR_LONG_MAX, vendor.length())));
        }
    }

}
