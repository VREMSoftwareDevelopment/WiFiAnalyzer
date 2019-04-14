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

package com.vrem.wifianalyzer.wifi.model;

import com.vrem.wifianalyzer.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StrengthTest {

    @Test
    public void testStrength() {
        assertEquals(5, Strength.values().length);
    }

    @Test
    public void testImageResource() {
        assertEquals(R.drawable.ic_signal_wifi_0_bar, Strength.ZERO.imageResource());
        assertEquals(R.drawable.ic_signal_wifi_1_bar, Strength.ONE.imageResource());
        assertEquals(R.drawable.ic_signal_wifi_2_bar, Strength.TWO.imageResource());
        assertEquals(R.drawable.ic_signal_wifi_3_bar, Strength.THREE.imageResource());
        assertEquals(R.drawable.ic_signal_wifi_4_bar, Strength.FOUR.imageResource());
    }

    @Test
    public void testColorResource() {
        assertEquals(R.color.error, Strength.ZERO.colorResource());
        assertEquals(R.color.warning, Strength.ONE.colorResource());
        assertEquals(R.color.warning, Strength.TWO.colorResource());
        assertEquals(R.color.success, Strength.THREE.colorResource());
        assertEquals(R.color.success, Strength.FOUR.colorResource());
    }

    @Test
    public void testColorResourceDefault() {
        assertEquals(R.color.regular, Strength.ZERO.colorResourceDefault());
        assertEquals(R.color.regular, Strength.ONE.colorResourceDefault());
        assertEquals(R.color.regular, Strength.TWO.colorResourceDefault());
        assertEquals(R.color.regular, Strength.THREE.colorResourceDefault());
        assertEquals(R.color.regular, Strength.FOUR.colorResourceDefault());
    }

    @Test
    public void testWeak() {
        assertTrue(Strength.ZERO.weak());
        assertFalse(Strength.ONE.weak());
        assertFalse(Strength.TWO.weak());
        assertFalse(Strength.THREE.weak());
        assertFalse(Strength.FOUR.weak());
    }

    @Test
    public void testCalculate() {
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

    @Test
    public void testReverse() {
        assertEquals(Strength.FOUR, Strength.reverse(Strength.ZERO));
        assertEquals(Strength.THREE, Strength.reverse(Strength.ONE));
        assertEquals(Strength.TWO, Strength.reverse(Strength.TWO));
        assertEquals(Strength.ONE, Strength.reverse(Strength.THREE));
        assertEquals(Strength.ZERO, Strength.reverse(Strength.FOUR));
    }

}