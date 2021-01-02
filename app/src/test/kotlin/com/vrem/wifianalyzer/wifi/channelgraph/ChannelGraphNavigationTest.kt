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
package com.vrem.wifianalyzer.wifi.channelgraph

import android.os.Build
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannelPair
import com.vrem.wifianalyzer.wifi.band.WiFiChannelsGHZ5
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class ChannelGraphNavigationTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val scanner = MainContextHelper.INSTANCE.scannerService
    private val settings = MainContextHelper.INSTANCE.settings
    private val configuration = MainContextHelper.INSTANCE.configuration
    private val layout: View = mock()
    private val views: MutableMap<WiFiChannelPair, View> = mutableMapOf()
    private val fixture = ChannelGraphNavigation(layout, mainActivity.applicationContext)

    @Before
    fun setUp() {
        navigationSet.keys.forEach { setUpExecute(it) }
        fixture.initialize()
    }

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testMapping() {
        assertEquals(WiFiChannelsGHZ5.SETS.size, navigationSet.size)
        WiFiChannelsGHZ5.SETS.forEach { Assert.assertNotNull(navigationSet[it]) }
    }

    @Test
    fun testInitialize() {
        navigationSet.values.forEach { verify(layout).findViewById<View>(it) }
        views.values.forEach { verify(it).setOnClickListener(ArgumentMatchers.any(View.OnClickListener::class.java)) }
    }

    @Test
    fun testUpdateWithGHZ2() {
        // setup
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        // execute
        fixture.update()
        // validate
        verify(layout).visibility = View.GONE
        verify(settings).wiFiBand()
    }

    @Test
    fun testUpdateWithGHZ5() {
        // setup
        val colorSelected = ContextCompat.getColor(mainActivity, R.color.selected)
        val colorNotSelected = ContextCompat.getColor(mainActivity, R.color.background)
        val selectedKey = WiFiBand.GHZ5.wiFiChannels.wiFiChannelPairs()[0]
        whenever(configuration.wiFiChannelPair).thenReturn(selectedKey)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ5)
        // execute
        fixture.update()
        // validate
        verify(layout).visibility = View.VISIBLE
        views.keys.forEach {
            val button = views[it] as Button
            verify(button).setBackgroundColor(if (selectedKey == it) colorSelected else colorNotSelected)
            verify(button).isSelected = selectedKey == it
        }
        navigationSet.values.forEach { verify(layout, times(2)).findViewById<View>(it) }
        verify(settings).wiFiBand()
        verify(configuration).wiFiChannelPair
    }

    @Test
    fun testSetOnClickListener() {
        // setup
        val expected = WiFiChannelsGHZ5.SET3
        // execute
        fixture.onClickListener(expected)
        // validate
        verify(configuration).wiFiChannelPair = expected
        verify(scanner).update()
    }

    private fun setUpExecute(key: WiFiChannelPair) {
        val id = navigationSet[key] as Int
        val button: Button = mock()
        views[key] = button
        whenever(layout.findViewById<View>(id)).thenReturn(button)
        whenever(button.text).thenReturn("ButtonName")
    }
}