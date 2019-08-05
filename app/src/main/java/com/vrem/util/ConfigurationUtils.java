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

package com.vrem.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

import androidx.annotation.NonNull;

public class ConfigurationUtils {
    private ConfigurationUtils() {
        throw new IllegalStateException("Utility class");
    }

    @NonNull
    public static Context createContext(@NonNull Context context, @NonNull Locale newLocale) {
        return
            BuildUtils.isMinVersionN()
                ? createContextAndroidN(context, newLocale)
                : createContextLegacy(context, newLocale);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @NonNull
    private static Context createContextAndroidN(@NonNull Context context, @NonNull Locale newLocale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(newLocale);
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    @NonNull
    private static Context createContextLegacy(@NonNull Context context, @NonNull Locale newLocale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = newLocale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

}
