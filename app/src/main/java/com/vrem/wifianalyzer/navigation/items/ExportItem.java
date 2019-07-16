/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.navigation.items;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.export.Export;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

class ExportItem implements NavigationItem {
    private static final String TIME_STAMP_FORMAT = "yyyy/MM/dd-HH:mm:ss";
    private String exportData;
    private String timestamp;

    @Override
    public void activate(@NonNull MainActivity mainActivity, @NonNull MenuItem menuItem, @NonNull NavigationMenu navigationMenu) {
        timestamp = new SimpleDateFormat(TIME_STAMP_FORMAT, Locale.US).format(new Date());
        String title = getTitle(mainActivity, timestamp);
        List<WiFiDetail> wiFiDetails = getWiFiDetails();
        if (!dataAvailable(wiFiDetails)) {
            Toast.makeText(mainActivity, R.string.no_data, Toast.LENGTH_LONG).show();
            exportData = StringUtils.EMPTY;
            return;
        }
        exportData = new Export(wiFiDetails, timestamp).getData();
        Intent intent = createIntent(title, exportData);
        Intent chooser = createChooserIntent(intent, title);
        if (!exportAvailable(mainActivity, chooser)) {
            Toast.makeText(mainActivity, R.string.export_not_available, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            mainActivity.startActivity(chooser);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mainActivity, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean isRegistered() {
        return false;
    }

    @Override
    public int getVisibility() {
        return View.GONE;
    }

    private boolean exportAvailable(@NonNull MainActivity mainActivity, @NonNull Intent chooser) {
        return chooser.resolveActivity(mainActivity.getPackageManager()) != null;
    }

    private boolean dataAvailable(@NonNull List<WiFiDetail> wiFiDetails) {
        return !wiFiDetails.isEmpty();
    }

    @NonNull
    String getExportData() {
        return exportData;
    }

    @NonNull
    String getTimestamp() {
        return timestamp;
    }

    @NonNull
    private List<WiFiDetail> getWiFiDetails() {
        return MainContext.INSTANCE.getScannerService().getWiFiData().getWiFiDetails();
    }

    @NonNull
    private String getTitle(@NonNull MainActivity mainActivity, @NonNull String timestamp) {
        Resources resources = mainActivity.getResources();
        String title = resources.getString(R.string.action_access_points);
        return title + "-" + timestamp;
    }

    private Intent createIntent(String title, String data) {
        Intent intent = createSendIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        return intent;
    }

    Intent createSendIntent() {
        return new Intent(Intent.ACTION_SEND);
    }

    Intent createChooserIntent(@NonNull Intent intent, @NonNull String title) {
        return Intent.createChooser(intent, title);
    }

}
