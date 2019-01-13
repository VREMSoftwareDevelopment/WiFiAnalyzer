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

package com.vrem.wifianalyzer.wifi.graphutils;

public class GraphConstants {
    public static final float AXIS_TEXT_SIZE_ADJUSTMENT = 0.90f;
    public static final float TEXT_SIZE_ADJUSTMENT = 0.80f;
    public static final int MAX_SCAN_COUNT = 200;
    public static final int MAX_Y = 0;
    public static final int MAX_Y_DEFAULT = -20;
    public static final int MIN_Y = -100;
    public static final int MIN_Y_OFFSET = -1;
    public static final int MIN_Y_HALF = MIN_Y / 2;
    public static final int NUM_X_CHANNEL = 18;
    public static final int NUM_X_TIME = 21;
    public static final int MAX_NOTSEEN_COUNT = 20;
    public static final int THICKNESS_INVISIBLE = 0;
    public static final int THICKNESS_REGULAR = 5;
    public static final int THICKNESS_CONNECTED = THICKNESS_REGULAR * 2;
    public static final int TYPE1 = 1147798476;
    public static final int TYPE2 = 535509942;
    public static final int TYPE3 = 1256180258;
    public static final int TYPE4 = 1546740952;

    private GraphConstants() {
        throw new IllegalStateException("Utility class");
    }
}
