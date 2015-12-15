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

import android.net.wifi.WifiManager;

import com.vrem.wifianalyzer.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WifiManager.class)
public class StrengthTest {

    @Test
    public void testGetImageResource() throws Exception {
        assertEquals(R.drawable.ic_signal_wifi_1_bar_black_48dp, Strength.ZERO.getImageResource());
        assertEquals(R.drawable.ic_signal_wifi_1_bar_black_48dp, Strength.ONE.getImageResource());
        assertEquals(R.drawable.ic_signal_wifi_2_bar_black_48dp, Strength.TWO.getImageResource());
        assertEquals(R.drawable.ic_signal_wifi_3_bar_black_48dp, Strength.THREE.getImageResource());
        assertEquals(R.drawable.ic_signal_wifi_4_bar_black_48dp, Strength.FOUR.getImageResource());
    }

    @Test
    public void testGetColorResource() throws Exception {
        assertEquals(R.color.error_color, Strength.ZERO.getColorResource());
        assertEquals(R.color.warning_color, Strength.ONE.getColorResource());
        assertEquals(R.color.warning_color, Strength.TWO.getColorResource());
        assertEquals(R.color.success_color, Strength.THREE.getColorResource());
        assertEquals(R.color.success_color, Strength.FOUR.getColorResource());
    }

    @Test
    public void testFind() throws Exception {
        // setup
        int value = -55;
        Strength expected = Strength.TWO;
        PowerMockito.mockStatic(WifiManager.class);
        // expected
        Mockito.when(WifiManager.calculateSignalLevel(value, Strength.values().length)).thenReturn(expected.ordinal());
        // execute
        Strength actual = Strength.calculate(value);
        // validate
        assertEquals(expected, actual);
        PowerMockito.verifyStatic();
    }
}