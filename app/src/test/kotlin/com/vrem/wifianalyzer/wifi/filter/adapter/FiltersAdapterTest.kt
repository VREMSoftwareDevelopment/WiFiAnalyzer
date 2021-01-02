/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.model.Security
import com.vrem.wifianalyzer.wifi.model.Strength
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.Serializable

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class FiltersAdapterTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val ssids = setOf<String>()
    private val wiFiBands: Set<WiFiBand> = WiFiBand.values().toSet()
    private val strengths: Set<Strength> = Strength.values().toSet()
    private val securities: Set<Security> = Security.values().toSet()
    private val settings = INSTANCE.settings

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
        INSTANCE.restore()
    }

    @Test
    fun testIsActive() {
        // execute & validate
        assertFalse(fixture.isActive())
    }

    @Test
    fun testGetFilterAdapters() {
        // execute
        val actual: List<BasicFilterAdapter<out Serializable?>?> = fixture.filterAdapters(true)
        // validate
        assertEquals(4, actual.size)
    }

    @Test
    fun testGetFilterAdaptersWithNptAccessPoints() {
        // execute
        val actual: List<BasicFilterAdapter<out Serializable?>?> = fixture.filterAdapters(false)
        // validate
        assertEquals(3, actual.size)
    }

    @Test
    fun testIsActiveWhenStrengthFilterIsChanged() {
        // setup
        fixture.strengthAdapter().toggle(Strength.THREE)
        // execute & validate
        assertTrue(fixture.isActive())
    }

    @Test
    fun testIsActiveWhenWiFiBandFilterIsChanged() {
        // setup
        fixture.wiFiBandAdapter().toggle(WiFiBand.GHZ2)
        // execute & validate
        assertTrue(fixture.isActive())
    }

    @Test
    fun testReset() {
        // execute
        fixture.reset()
        // validate
        verify(settings).saveSSIDs(ssids)
        verify(settings).saveWiFiBands(wiFiBands)
        verify(settings).saveStrengths(strengths)
        verify(settings).saveSecurities(securities)
    }

    @Test
    fun testReload() {
        // execute
        fixture.reload()
        // validate
        verify(settings, times(2)).findSSIDs()
        verify(settings, times(2)).findWiFiBands()
        verify(settings, times(2)).findStrengths()
        verify(settings, times(2)).findSecurities()
    }

    @Test
    fun testSave() {
        // execute
        fixture.save()
        // validate
        verify(settings).saveSSIDs(ssids)
        verify(settings).saveWiFiBands(wiFiBands)
        verify(settings).saveStrengths(strengths)
        verify(settings).saveSecurities(securities)
    }
}