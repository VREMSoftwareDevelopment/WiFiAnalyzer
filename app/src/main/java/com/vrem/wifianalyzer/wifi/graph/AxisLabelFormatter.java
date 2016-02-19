/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.vrem.wifianalyzer.wifi.graph;

import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.vrem.wifianalyzer.wifi.WiFiConstants;

import org.apache.commons.lang3.StringUtils;

public abstract class AxisLabelFormatter implements LabelFormatter {
    protected abstract String getXAxisValue(int valueAsInt);

    @Override
    public String formatLabel(double value, boolean isValueX) {
        int valueAsInt = (int) (value + (value < 0 ? -0.5 : 0.5));
        if (isValueX) {
            if (valueAsInt >= 0) {
                return getXAxisValue(valueAsInt);
            }
        } else {
            if (valueAsInt > WiFiConstants.MIN_Y) {
                return "" + valueAsInt;
            }
        }
        return StringUtils.EMPTY;
    }

    @Override
    public void setViewport(Viewport viewport) {
        // ignore
    }
}
