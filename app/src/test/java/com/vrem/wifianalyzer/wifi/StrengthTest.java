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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WifiManager.class)
public class StrengthTest {

    @Test
    public void testImageResource() throws Exception {
        assertEquals(R.drawable.ic_signal_wifi_0_bar_black_48dp, Strength.ZERO.imageResource());
        assertEquals(R.drawable.ic_signal_wifi_1_bar_black_48dp, Strength.ONE.imageResource());
        assertEquals(R.drawable.ic_signal_wifi_2_bar_black_48dp, Strength.TWO.imageResource());
        assertEquals(R.drawable.ic_signal_wifi_3_bar_black_48dp, Strength.THREE.imageResource());
        assertEquals(R.drawable.ic_signal_wifi_4_bar_black_48dp, Strength.FOUR.imageResource());
    }

    @Test
    public void testColorResource() throws Exception {
        assertEquals(R.color.error_color, Strength.ZERO.colorResource());
        assertEquals(R.color.warning_color, Strength.ONE.colorResource());
        assertEquals(R.color.warning_color, Strength.TWO.colorResource());
        assertEquals(R.color.success_color, Strength.THREE.colorResource());
        assertEquals(R.color.success_color, Strength.FOUR.colorResource());
    }

    @Test
    public void testWeak() throws Exception {
        assertTrue(Strength.ZERO.weak());
        assertFalse(Strength.ONE.weak());
        assertFalse(Strength.TWO.weak());
        assertFalse(Strength.THREE.weak());
        assertFalse(Strength.FOUR.weak());
    }

    @Test
    public void testFind() throws Exception {
        // setup
        int value = -55;
        Strength expected = Strength.TWO;
        mockStatic(WifiManager.class);
        when(WifiManager.calculateSignalLevel(value, Strength.values().length)).thenReturn(expected.ordinal());
        // execute
        Strength actual = Strength.calculate(value);
        // validate
        assertEquals(expected, actual);
        verifyStatic();
    }

}