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
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;

public class TextUtils {
    private static final String SEPARATOR = " ";

    private TextUtils() {
        throw new IllegalStateException("Utility class");
    }

    @NonNull
    public static Set<String> split(String source) {
        return StringUtils.isBlank(source)
            ? new HashSet<>()
            : new HashSet<>(Arrays.asList(trim(source).split(SEPARATOR)));
    }

    @NonNull
    public static String join(Set<String> source) {
        return source == null
            ? StringUtils.EMPTY
            : trim(android.text.TextUtils.join(SEPARATOR, source.toArray()));
    }

    @NonNull
    public static String trim(String source) {
        return StringUtils.isBlank(source)
            ? StringUtils.EMPTY
            : source.trim().replaceAll(" +", " ");
    }

    @NonNull
    public static String textToHtml(@NonNull String text, int color, boolean small) {
        return "<font color='" + color + "'><" + (small ? "small" : "strong") +
            ">" + text + "</" + (small ? "small" : "strong") + "></font>";
    }

    @NonNull
    public static Spanned fromHtml(@NonNull String text) {
        return BuildUtils.isMinVersionN() ? fromHtmlAndroidN(text) : fromHtmlLegacy(text);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @NonNull
    private static Spanned fromHtmlAndroidN(@NonNull String text) {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
    }

    @SuppressWarnings("deprecation")
    @NonNull
    private static Spanned fromHtmlLegacy(@NonNull String text) {
        return Html.fromHtml(text);
    }

}
