/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi;

import com.vrem.wifianalyzer.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StrengthTest {

    @Test
    public void testGetImageResource() throws Exception {
        assertEquals(R.drawable.ic_signal_wifi_1_bar_red_900_48dp, Strength.ZERO.getImageResource());
        assertEquals(R.drawable.ic_signal_wifi_1_bar_yellow_900_48dp, Strength.ONE.getImageResource());
        assertEquals(R.drawable.ic_signal_wifi_2_bar_yellow_900_48dp, Strength.TWO.getImageResource());
        assertEquals(R.drawable.ic_signal_wifi_3_bar_green_900_48dp, Strength.THREE.getImageResource());
        assertEquals(R.drawable.ic_signal_wifi_4_bar_green_900_48dp, Strength.FOUR.getImageResource());
    }

    @Test
    public void testGetColorResource() throws Exception {
        assertEquals(R.color.wifi0, Strength.ZERO.getColorResource());
        assertEquals(R.color.wifi1, Strength.ONE.getColorResource());
        assertEquals(R.color.wifi2, Strength.TWO.getColorResource());
        assertEquals(R.color.wifi3, Strength.THREE.getColorResource());
        assertEquals(R.color.wifi4, Strength.FOUR.getColorResource());
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(Strength.ZERO, Strength.calculate(-100));
        assertEquals(Strength.ZERO, Strength.calculate(-89));

        assertEquals(Strength.ONE, Strength.calculate(-88));
        assertEquals(Strength.ONE, Strength.calculate(-78));

        assertEquals(Strength.TWO, Strength.calculate(-77));
        assertEquals(Strength.TWO, Strength.calculate(-67));

        assertEquals(Strength.THREE, Strength.calculate(-66));
        assertEquals(Strength.THREE, Strength.calculate(-56));

        assertEquals(Strength.FOUR, Strength.calculate(-55));
        assertEquals(Strength.FOUR, Strength.calculate(0));
    }
}