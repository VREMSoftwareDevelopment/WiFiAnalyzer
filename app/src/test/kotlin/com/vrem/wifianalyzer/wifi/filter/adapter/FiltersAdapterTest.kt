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
package com.vrem.wifianalyzer.wifi.filter.adapter

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.model.Security
import com.vrem.wifianalyzer.wifi.model.Strength
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config
import java.io.Serializable

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class FiltersAdapterTest {
    @Suppress("unused")
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val ssids = setOf<String>()
    private val wiFiBands: Set<WiFiBand> = WiFiBand.entries.toSet()
    private val strengths: Set<Strength> = Strength.entries.toSet()
    private val securities: Set<Security> = Security.entries.toSet()
    private val settings = MainContextHelper.INSTANCE.settings

    private lateinit var fixture: FiltersAdapter

    @Before
    fun setUp() {
        whenever(settings.findSSIDs()).thenReturn(ssids)
        whenever(settings.findWiFiBands()).thenReturn(wiFiBands)
        whenever(settings.findStrengths()).thenReturn(strengths)
        whenever(settings.findSecurities()).thenReturn(securities)

        fixture = FiltersAdapter(settings)

        verify(settings).findSSIDs()
        verify(settings).findWiFiBands()
        verify(settings).findStrengths()
        verify(settings).findSecurities()
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(settings)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun isActive() {
        // execute & validate
        assertThat(fixture.isActive()).isFalse()
    }

    @Test
    fun getFilterAdapters() {
        // execute
        val actual: List<BasicFilterAdapter<out Serializable?>?> = fixture.filterAdapters(true)
        // validate
        assertThat(actual).hasSize(4)
    }

    @Test
    fun getFilterAdaptersWithNptAccessPoints() {
        // execute
        val actual: List<BasicFilterAdapter<out Serializable?>?> = fixture.filterAdapters(false)
        // validate
        assertThat(actual).hasSize(3)
    }

    @Test
    fun isActiveWhenStrengthFilterIsChanged() {
        // setup
        fixture.strengthAdapter().toggle(Strength.THREE)
        // execute & validate
        assertThat(fixture.isActive()).isTrue()
    }

    @Test
    fun isActiveWhenWiFiBandFilterIsChanged() {
        // setup
        fixture.wiFiBandAdapter().toggle(WiFiBand.GHZ2)
        // execute & validate
        assertThat(fixture.isActive()).isTrue()
    }

    @Test
    fun reset() {
        // execute
        fixture.reset()
        // validate
        verify(settings).saveSSIDs(ssids)
        verify(settings).saveWiFiBands(wiFiBands)
        verify(settings).saveStrengths(strengths)
        verify(settings).saveSecurities(securities)
    }

    @Test
    fun reload() {
        // execute
        fixture.reload()
        // validate
        verify(settings, times(2)).findSSIDs()
        verify(settings, times(2)).findWiFiBands()
        verify(settings, times(2)).findStrengths()
        verify(settings, times(2)).findSecurities()
    }

    @Test
    fun save() {
        // execute
        fixture.save()
        // validate
        verify(settings).saveSSIDs(ssids)
        verify(settings).saveWiFiBands(wiFiBands)
        verify(settings).saveStrengths(strengths)
        verify(settings).saveSecurities(securities)
    }
}