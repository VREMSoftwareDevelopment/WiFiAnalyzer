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

import android.content.res.Resources;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

public class FileUtils {
    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    @NonNull
    public static String readFile(@NonNull Resources resources, @RawRes int id) {
        try (InputStream inputStream = resources.openRawResource(id)) {
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            int count = inputStream.read(bytes);
            if (count != size) {
                return StringUtils.EMPTY;
            }
            return new String(bytes);
        } catch (Exception e) {
            // file is corrupted
            return StringUtils.EMPTY;
        }
    }
}
