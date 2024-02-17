/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.model

import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.wifi.model.Strength.Companion.calculate
import com.vrem.wifianalyzer.wifi.model.Strength.Companion.reverse
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StrengthTest {
    @Test
    fun strength() {
        assertThat(Strength.entries).hasSize(5)
    }

    @Test
    fun imageResource() {
        assertThat(Strength.ZERO.imageResource).isEqualTo(R.drawable.ic_signal_wifi_0_bar)
        assertThat(Strength.ONE.imageResource).isEqualTo(R.drawable.ic_signal_wifi_1_bar)
        assertThat(Strength.TWO.imageResource).isEqualTo(R.drawable.ic_signal_wifi_2_bar)
        assertThat(Strength.THREE.imageResource).isEqualTo(R.drawable.ic_signal_wifi_3_bar)
        assertThat(Strength.FOUR.imageResource).isEqualTo(R.drawable.ic_signal_wifi_4_bar)
    }

    @Test
    fun colorResource() {
        assertThat(Strength.ZERO.colorResource).isEqualTo(R.color.error)
        assertThat(Strength.ONE.colorResource).isEqualTo(R.color.warning)
        assertThat(Strength.TWO.colorResource).isEqualTo(R.color.warning)
        assertThat(Strength.THREE.colorResource).isEqualTo(R.color.success)
        assertThat(Strength.FOUR.colorResource).isEqualTo(R.color.success)
    }

    @Test
    fun weak() {
        assertThat(Strength.ZERO.weak()).isTrue()
        assertThat(Strength.ONE.weak()).isFalse()
        assertThat(Strength.TWO.weak()).isFalse()
        assertThat(Strength.THREE.weak()).isFalse()
        assertThat(Strength.FOUR.weak()).isFalse()
    }

    @Test
    fun calculate() {
        assertThat(calculate(-89)).isEqualTo(Strength.ZERO)
        assertThat(calculate(-88)).isEqualTo(Strength.ONE)
        assertThat(calculate(-78)).isEqualTo(Strength.ONE)
        assertThat(calculate(-77)).isEqualTo(Strength.TWO)
        assertThat(calculate(-67)).isEqualTo(Strength.TWO)
        assertThat(calculate(-66)).isEqualTo(Strength.THREE)
        assertThat(calculate(-56)).isEqualTo(Strength.THREE)
        assertThat(calculate(-55)).isEqualTo(Strength.FOUR)
        assertThat(calculate(0)).isEqualTo(Strength.FOUR)
    }

    @Test
    fun reverse() {
        assertThat(reverse(Strength.ZERO)).isEqualTo(Strength.FOUR)
        assertThat(reverse(Strength.ONE)).isEqualTo(Strength.THREE)
        assertThat(reverse(Strength.TWO)).isEqualTo(Strength.TWO)
        assertThat(reverse(Strength.THREE)).isEqualTo(Strength.ONE)
        assertThat(reverse(Strength.FOUR)).isEqualTo(Strength.ZERO)
    }
}