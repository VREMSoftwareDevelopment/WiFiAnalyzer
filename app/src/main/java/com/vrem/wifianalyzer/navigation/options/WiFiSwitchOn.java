/*
 * WiFi Analyzer
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

package com.vrem.wifianalyzer.navigation.options;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.Spanned;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;

class WiFiSwitchOn implements NavigationOption {

    @Override
    public void apply(@NonNull MainActivity mainActivity) {
        ActionBar actionBar = mainActivity.getSupportActionBar();
        if (actionBar != null) {
            int color = ContextCompat.getColor(mainActivity, R.color.connected);
            WiFiBand wiFiBand = MainContext.INSTANCE.getSettings().getWiFiBand();
            String subtitle = makeSubtitle(mainActivity, wiFiBand, color);
            actionBar.setSubtitle(fromHtml(subtitle));
        }
    }

    @NonNull
    String makeSubtitle(@NonNull MainActivity mainActivity, @NonNull WiFiBand wiFiBand, int color) {
        String subtitleText = makeSubtitleText("<font color='" + color + "'><strong>", "</strong></font>", "<small>", "</small>");
        if (WiFiBand.GHZ5.equals(wiFiBand)) {
            subtitleText = makeSubtitleText("<small>", "</small>", "<font color='" + color + "'><strong>", "</strong></font>");
        }
        return subtitleText;
    }

    @SuppressWarnings("deprecation")
    @NonNull
    Spanned fromHtml(@NonNull String subtitleText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(subtitleText, Html.FROM_HTML_MODE_LEGACY);
        }
        return Html.fromHtml(subtitleText);
    }

    @NonNull
    private String makeSubtitleText(@NonNull String tag1, @NonNull String tag2, @NonNull String tag3, @NonNull String tag4) {
        return tag1 + WiFiBand.GHZ2.getBand() + tag2 + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + tag3 + WiFiBand.GHZ5.getBand() + tag4;
    }

}
