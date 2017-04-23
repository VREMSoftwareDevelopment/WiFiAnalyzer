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

package com.vrem.wifianalyzer.wifi.graphutils;

public interface GraphConstants {
    float AXIS_TEXT_SIZE_ADJUSTMENT = 0.90f;
    float TEXT_SIZE_ADJUSTMENT = 0.80f;

    int MAX_SCAN_COUNT = 200;
    int MAX_Y = 0;
    int MAX_Y_DEFAULT = -20;
    int MIN_Y = -100;
    int MIN_Y_OFFSET = -1;
    int MIN_Y_HALF = MIN_Y / 2;
    int NUM_X_CHANNEL = 18;
    int NUM_X_TIME = 21;
    int MAX_NOTSEEN_COUNT = 20;
    int THICKNESS_INVISIBLE = 0;
    int THICKNESS_REGULAR = 5;
    int THICKNESS_CONNECTED = THICKNESS_REGULAR * 2;
    int TYPE1 = 1147798476;
    int TYPE2 = 535509942;
    int TYPE3 = 1256180258;
    int TYPE4 = 1546740952;
}
