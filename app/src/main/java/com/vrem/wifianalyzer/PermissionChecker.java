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

package com.vrem.wifianalyzer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.vrem.util.BuildUtils;

class PermissionChecker {
    static final String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION};
    static final int REQUEST_CODE = 0x123450;

    private final Activity activity;
    private final PermissionDialog permissionDialog;

    PermissionChecker(@NonNull Activity activity) {
        this(activity, new PermissionDialog(activity));
    }

    PermissionChecker(@NonNull Activity activity, @NonNull PermissionDialog permissionDialog) {
        this.activity = activity;
        this.permissionDialog = permissionDialog;
    }

    void check() {
        if (isGranted()) {
            return;
        }
        if (activity.isFinishing()) {
            return;
        }
        permissionDialog.show();
    }

    boolean isGranted(int requestCode, @NonNull int[] grantResults) {
        return requestCode == REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isGranted() {
        return isGranted(Manifest.permission.ACCESS_COARSE_LOCATION) || isGranted(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private boolean isGranted(String accessCoarseLocation) {
        if (BuildUtils.isMinVersionM()) {
            return activity.checkSelfPermission(accessCoarseLocation) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

}
