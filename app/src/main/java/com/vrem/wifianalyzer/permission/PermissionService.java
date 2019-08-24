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

import android.app.Activity;

import androidx.annotation.NonNull;

public class PermissionService {
    private SystemPermission systemPermission;
    private ApplicationPermission applicationPermission;

    public PermissionService(@NonNull Activity activity) {
        this.applicationPermission = new ApplicationPermission(activity);
        this.systemPermission = new SystemPermission(activity);
    }

    public boolean isEnabled() {
        return isSystemEnabled() && isPermissionGranted();
    }

    public boolean isSystemEnabled() {
        return systemPermission.isEnabled();
    }

    public void check() {
        applicationPermission.check();
    }

    public boolean isGranted(int requestCode, int[] grantResults) {
        return applicationPermission.isGranted(requestCode, grantResults);
    }

    public boolean isPermissionGranted() {
        return applicationPermission.isGranted();
    }

    void setSystemPermission(SystemPermission systemPermission) {
        this.systemPermission = systemPermission;
    }

    void setApplicationPermission(ApplicationPermission applicationPermission) {
        this.applicationPermission = applicationPermission;
    }

}
