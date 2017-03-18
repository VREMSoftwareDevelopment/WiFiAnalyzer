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

import com.jjoe64.graphview.series.DataPoint;

import org.mockito.ArgumentMatcher;

public class DataPointEquals extends ArgumentMatcher<DataPoint> {

    private final DataPoint expected;

    public DataPointEquals(DataPoint expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object actual) {
        return expected.getX() == ((DataPoint) actual).getX() && expected.getY() == ((DataPoint) actual).getY();
    }
}
