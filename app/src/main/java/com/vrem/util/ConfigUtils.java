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

package com.vrem.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.Locale;

public class ConfigUtils {
    @NonNull
    public static String getDefaultCountryCode(@NonNull Context context) {
        return getDefault(context).getCountry();
    }

    @NonNull
    public static String getDefaultLanguageTag(@NonNull Context context) {
        return LocaleUtils.toLanguageTag(getDefault(context));
    }

    @SuppressWarnings("deprecation")
    @NonNull
    public static Context createContext(@NonNull Context context, @NonNull Locale newLocale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        Context nextContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale);
            nextContext = context.createConfigurationContext(configuration);
        } else {
            configuration.locale = newLocale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return new ContextWrapper(nextContext);
    }

    @NonNull
    private static Locale getDefault(@NonNull Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        return getConfigLocale(configuration);
    }

    @SuppressWarnings("deprecation")
    @NonNull
    static Locale getConfigLocale(@NonNull Configuration config) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return config.getLocales().get(0);
        }
        return config.locale;
    }
}
