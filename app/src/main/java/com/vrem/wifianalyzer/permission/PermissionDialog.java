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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.view.View;

import com.vrem.util.BuildUtils;
import com.vrem.wifianalyzer.R;

import androidx.annotation.NonNull;

class PermissionDialog {
    private final Activity activity;

    PermissionDialog(@NonNull Activity activity) {
        this.activity = activity;
    }

    void show() {
        View view = activity.getLayoutInflater().inflate(R.layout.info_permission, null);
        new AlertDialog
            .Builder(activity)
            .setView(view)
            .setTitle(R.string.app_full_name)
            .setIcon(R.drawable.ic_app)
            .setPositiveButton(android.R.string.ok, new OkClick(activity))
            .setNegativeButton(android.R.string.cancel, new CancelClick(activity))
            .create()
            .show();
    }

    static class OkClick implements OnClickListener {
        private final Activity activity;

        OkClick(@NonNull Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            requestPermissionsAndroidM();
        }

        @TargetApi(Build.VERSION_CODES.M)
        private void requestPermissionsAndroidM() {
            if (BuildUtils.isMinVersionM()) {
                activity.requestPermissions(ApplicationPermission.PERMISSIONS, ApplicationPermission.REQUEST_CODE);
            }
        }

    }

    static class CancelClick implements OnClickListener {
        private final Activity activity;

        CancelClick(@NonNull Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            activity.finish();
        }
    }

}
