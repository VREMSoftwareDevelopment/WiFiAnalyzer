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

package com.vrem.wifianalyzer.wifi.filter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.annotation.NonNull;
import android.view.View;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.NavigationMenu;

public class Filter {

    private final AlertDialog alertDialog;

    private Filter(@NonNull AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    public static Filter build() {
        return new Filter(buildAlertDialog());
    }

    private static AlertDialog buildAlertDialog() {
        MainActivity mainActivity = MainContext.INSTANCE.getMainActivity();
        View view = mainActivity.getLayoutInflater().inflate(R.layout.filter_popup, null);
        return new AlertDialog
            .Builder(mainActivity)
            .setView(view)
            .setTitle(R.string.filter_title)
            .setIcon(R.drawable.ic_filter_list_grey_500_48dp)
            .setNegativeButton(R.string.filter_reset, new Reset())
            .setNeutralButton(R.string.filter_close, new Close())
            .setPositiveButton(R.string.filter_apply, new Apply())
            .create();
    }

    public void show() {
        try {
            if (!alertDialog.isShowing()) {
                alertDialog.show();
                addWiFiBandFilter();
                addSSIDFilter();
                addStrengthFilter();
                addSecurityFilter();
            }
        } catch (Exception e) {
            // ignore: unable to show filter
        }
    }

    AlertDialog getAlertDialog() {
        return alertDialog;
    }

    private void addSSIDFilter() {
        new SSIDFilter(MainContext.INSTANCE.getFilterAdapter().getSSIDAdapter(), alertDialog);
    }

    private void addWiFiBandFilter() {
        if (NavigationMenu.ACCESS_POINTS.equals(MainContext.INSTANCE.getMainActivity().getNavigationMenuView().getCurrentNavigationMenu())) {
            new WiFiBandFilter(MainContext.INSTANCE.getFilterAdapter().getWiFiBandAdapter(), alertDialog);
        }
    }

    private void addStrengthFilter() {
        new StrengthFilter(MainContext.INSTANCE.getFilterAdapter().getStrengthAdapter(), alertDialog);
    }

    private void addSecurityFilter() {
        new SecurityFilter(MainContext.INSTANCE.getFilterAdapter().getSecurityAdapter(), alertDialog);
    }

    private static class Close implements OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            MainContext.INSTANCE.getFilterAdapter().reload();
        }
    }

    private static class Apply implements OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            MainContext mainContext = MainContext.INSTANCE;
            mainContext.getFilterAdapter().save();
            mainContext.getMainActivity().update();
        }
    }

    private static class Reset implements OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            MainContext mainContext = MainContext.INSTANCE;
            mainContext.getFilterAdapter().reset();
            mainContext.getMainActivity().update();
        }
    }
}
