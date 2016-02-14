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
package com.vrem.wifianalyzer.wifi.graph.time;

import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.WiFiConstants;

import org.apache.commons.lang3.StringUtils;

class TimeGraphAxisLabel implements LabelFormatter {
    private final MainContext mainContext = MainContext.INSTANCE;

    TimeGraphAxisLabel() {
    }

    @Override
    public String formatLabel(double value, boolean isValueX) {
        String result = StringUtils.EMPTY;
        if (isValueX) {
            if (value > 0) {
                result += ((int) (value + 0.5) * mainContext.getSettings().getScanInterval());
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
