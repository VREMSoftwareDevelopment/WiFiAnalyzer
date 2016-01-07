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
package com.vrem.wifianalyzer.wifi.model;

import org.junit.Test;

import java.text.DecimalFormat;

import static org.junit.Assert.assertEquals;

public class DistanceTest {

    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Test
    public void testCalculate() throws Exception {
        validate(2437, -36, "0.62");
        validate(2437, -42, "1.23");
        validate(2432, -88, "246.34");
        validate(2412, -91, "350.85");
    }

    private void validate(int frequency, int level, String expected) {
        assertEquals(expected, decimalFormat.format(Distance.calculate(frequency, level)));
    }
}