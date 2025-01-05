/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2025 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.navigation.options

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.navigation.options.OptionAction.Companion.findOptionAction
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.scanner.ScannerService
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class OptionActionTest {
    @Suppress("unused")
    private val mainActivity: MainActivity = RobolectricUtil.INSTANCE.activity
    private val scannerService: ScannerService = INSTANCE.scannerService
    private val settings: Settings = INSTANCE.settings

    @After
    fun tearDown() {
        verifyNoMoreInteractions(scannerService)
        verifyNoMoreInteractions(settings)
        INSTANCE.restore()
    }

    @Test
    fun scannerActionShouldToggleScannerService() {
        // execute
        scannerAction()
        // validate
        verify(scannerService).toggle()
    }

    @Test
    fun wiFiBandAction2ShouldSwitchToGHZ2() {
        // execute
        wiFiBandAction2()
        // validate
        verify(settings).wiFiBand(WiFiBand.GHZ2)
    }

    @Test
    fun wiFiBandAction5ShouldSwitchToGHZ5() {
        // execute
        wiFiBandAction5()
        // validate
        verify(settings).wiFiBand(WiFiBand.GHZ5)
    }

    @Test
    fun wiFiBandAction6ShouldSwitchToGHZ6() {
        // execute
        wiFiBandAction6()
        // validate
        verify(settings).wiFiBand(WiFiBand.GHZ6)
    }

    @Test
    fun filterActionShouldBuildAndShow() {
        filterAction()
    }

    @Test
    fun optionAction() {
        assertThat(OptionAction.entries).hasSize(6)
    }

    @Test
    fun getKey() {
        assertThat(OptionAction.NO_ACTION.key).isEqualTo(-1)
        assertThat(OptionAction.SCANNER.key).isEqualTo(R.id.action_scanner)
        assertThat(OptionAction.FILTER.key).isEqualTo(R.id.action_filter)
        assertThat(OptionAction.WIFI_BAND_2.key).isEqualTo(R.id.action_wifi_band_2ghz)
        assertThat(OptionAction.WIFI_BAND_5.key).isEqualTo(R.id.action_wifi_band_5ghz)
        assertThat(OptionAction.WIFI_BAND_6.key).isEqualTo(R.id.action_wifi_band_6ghz)
    }

    @Test
    fun getAction() {
        assertThat(OptionAction.NO_ACTION.action == noAction).isTrue()
        assertThat(OptionAction.SCANNER.action == scannerAction).isTrue()
        assertThat(OptionAction.FILTER.action == filterAction).isTrue()
        assertThat(OptionAction.WIFI_BAND_2.action == wiFiBandAction2).isTrue()
        assertThat(OptionAction.WIFI_BAND_5.action == wiFiBandAction5).isTrue()
        assertThat(OptionAction.WIFI_BAND_6.action == wiFiBandAction6).isTrue()
    }

    @Test
    fun getOptionAction() {
        assertThat(findOptionAction(OptionAction.NO_ACTION.key)).isEqualTo(OptionAction.NO_ACTION)
        assertThat(findOptionAction(OptionAction.SCANNER.key)).isEqualTo(OptionAction.SCANNER)
        assertThat(findOptionAction(OptionAction.FILTER.key)).isEqualTo(OptionAction.FILTER)
        assertThat(findOptionAction(OptionAction.WIFI_BAND_2.key)).isEqualTo(OptionAction.WIFI_BAND_2)
        assertThat(findOptionAction(OptionAction.WIFI_BAND_5.key)).isEqualTo(OptionAction.WIFI_BAND_5)
        assertThat(findOptionAction(OptionAction.WIFI_BAND_6.key)).isEqualTo(OptionAction.WIFI_BAND_6)
    }

    @Test
    fun getOptionActionInvalidKey() {
        assertThat(findOptionAction(-99)).isEqualTo(OptionAction.NO_ACTION)
        assertThat(findOptionAction(99)).isEqualTo(OptionAction.NO_ACTION)
    }
}