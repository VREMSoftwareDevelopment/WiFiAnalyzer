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

package com.vrem.wifianalyzer.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import com.vrem.util.BuildUtils;

import androidx.annotation.NonNull;

class ApplicationPermission {
    static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    static final int REQUEST_CODE = 0x123450;

    private final Activity activity;
    private final PermissionDialog permissionDialog;

    ApplicationPermission(@NonNull Activity activity) {
        this(activity, new PermissionDialog(activity));
    }

    ApplicationPermission(@NonNull Activity activity, @NonNull PermissionDialog permissionDialog) {
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

    boolean isGranted() {
        return !BuildUtils.isMinVersionM() || isGrantedAndroidM();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isGrantedAndroidM() {
        return activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

}
