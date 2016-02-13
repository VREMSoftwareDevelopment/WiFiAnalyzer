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
package com.vrem.wifianalyzer.wifi.channelgraph;

import android.support.annotation.NonNull;

import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.WiFiConstants;

import org.apache.commons.lang3.StringUtils;

class AxisLabel implements LabelFormatter {
    private final MainContext mainContext = MainContext.INSTANCE;
    private final Constraints constraints;

    AxisLabel(@NonNull Constraints constraints) {
        this.constraints = constraints;
    }

    @Override
    public String formatLabel(double value, boolean isValueX) {
        String result = StringUtils.EMPTY;
        if (isValueX) {
            int valueAsInt = (int) (value + 0.5);
            if (valueAsInt >= constraints.minX() && valueAsInt <= constraints.maxX()) {
                if (mainContext.isOrientationLandscape()) {
                    result += valueAsInt;
                } else {
                    if (valueAsInt <= 10) {
                        result += valueAsInt;
                    } else if (valueAsInt > 10 && valueAsInt % 2 == 0) {
                        result += valueAsInt;
                    }
                }
            }
        } else {
            int valueAsInt = (int) (value - 0.5);
            if (valueAsInt > WiFiConstants.MIN_Y) {
                result += (int) (value - 0.5);
            }
        }
        return result;
    }

    @Override
    public void setViewport(Viewport viewport) {
        // ignore
    }
}
