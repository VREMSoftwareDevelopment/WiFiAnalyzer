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
package com.vrem.wifianalyzer.wifi.channelgraph

import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.text.parseAsHtml
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.*
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class ChannelGraphNavigationTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val scanner = MainContextHelper.INSTANCE.scannerService
    private val settings = MainContextHelper.INSTANCE.settings
    private val configuration = MainContextHelper.INSTANCE.configuration
    private val view: View = mock()
    private val lines: MutableMap<Int, LinearLayout> = mutableMapOf()
    private val buttons: MutableMap<Int, Button> = mutableMapOf()
    private val fixture = ChannelGraphNavigation(view, mainActivity.applicationContext)
    private val colorSelected = ContextCompat.getColor(mainActivity, R.color.selected)
    private val colorNotSelected = ContextCompat.getColor(mainActivity, R.color.background)

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testUpdateWithGHZ2() {
        // setup
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(configuration.wiFiChannelPair(WiFiBand.GHZ2)).thenReturn(WiFiChannels.UNKNOWN)
        whenever(configuration.wiFiChannelPair(WiFiBand.GHZ2)).thenReturn(WiFiChannels.UNKNOWN)
        // execute
        fixture.update()
        // validate
        verify(view).visibility = View.GONE
        verify(settings).wiFiBand()
        verify(configuration).wiFiChannelPair(WiFiBand.GHZ2)
    }

    @Test
    fun testUpdateWithGHZ5() {
        // setup
        val selected = WiFiBand.GHZ5.wiFiChannels.wiFiChannelPairs()[0]
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ5)
        whenever(configuration.wiFiChannelPair(WiFiBand.GHZ5)).thenReturn(selected)
        whenLines(navigationGHZ5Lines)
        // execute
        fixture.update()
        // validate
        verifyLines(navigationGHZ5Lines, selected)
        verify(view).visibility = View.VISIBLE
        verify(settings).wiFiBand()
        verify(configuration).wiFiChannelPair(WiFiBand.GHZ5)
    }

    @Test
    fun testUpdateWithGHZ6() {
        // setup
        val selected = WiFiBand.GHZ6.wiFiChannels.wiFiChannelPairs()[0]
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ6)
        whenever(configuration.wiFiChannelPair(WiFiBand.GHZ6)).thenReturn(selected)
        whenLines(navigationGHZ6Lines)
        // execute
        fixture.update()
        // validate
        verifyLines(navigationGHZ6Lines, selected)
        verify(view).visibility = View.VISIBLE
        verify(settings).wiFiBand()
        verify(configuration).wiFiChannelPair(WiFiBand.GHZ6)
    }

    @Test
    fun testSetOnClickListener() {
        // setup
        val expected = WiFiChannelsGHZ5.SET3
        // execute
        fixture.onClickListener(WiFiBand.GHZ5, expected)
        // validate
        verify(configuration).wiFiChannelPair(WiFiBand.GHZ5, expected)
        verify(scanner).update()
    }

    @Test
    fun testNavigationGHZ2Lines() {
        assertEquals(0, navigationGHZ2Lines.size)
        assertTrue(navigationGHZ2Lines.isEmpty())
    }

    @Test
    fun testNavigationGHZ5Lines() {
        assertEquals(2, navigationGHZ5Lines.size)
        assertTrue(navigationGHZ5Lines[R.id.graphNavigationLine1]!!.isNotEmpty())
        assertTrue(navigationGHZ5Lines[R.id.graphNavigationLine2]!!.isEmpty())
    }

    @Test
    fun testNavigationGHZ6Lines() {
        assertEquals(2, navigationGHZ6Lines.size)
        assertTrue(navigationGHZ6Lines[R.id.graphNavigationLine1]!!.isNotEmpty())
        assertTrue(navigationGHZ6Lines[R.id.graphNavigationLine2]!!.isNotEmpty())
    }

    @Test
    fun testNavigationGHZ5Line1() {
        val line1 = navigationGHZ5Lines[R.id.graphNavigationLine1]!!
        assertEquals(3, line1.size)
        assertEquals(line1[R.id.graphNavigationSet1], WiFiChannelsGHZ5.SET1)
        assertEquals(line1[R.id.graphNavigationSet2], WiFiChannelsGHZ5.SET2)
        assertEquals(line1[R.id.graphNavigationSet3], WiFiChannelsGHZ5.SET3)
    }

    @Test
    fun testNavigationGHZ6Line1() {
        val line1 = navigationGHZ6Lines[R.id.graphNavigationLine1]!!
        assertEquals(3, line1.size)
        assertEquals(line1[R.id.graphNavigationSet1], WiFiChannelsGHZ6.SET1)
        assertEquals(line1[R.id.graphNavigationSet2], WiFiChannelsGHZ6.SET2)
        assertEquals(line1[R.id.graphNavigationSet3], WiFiChannelsGHZ6.SET3)
    }

    @Test
    fun testNavigationGHZ6Line2() {
        val line2 = navigationGHZ6Lines[R.id.graphNavigationLine2]!!
        assertEquals(4, line2.size)
        assertEquals(line2[R.id.graphNavigationSet4], WiFiChannelsGHZ6.SET4)
        assertEquals(line2[R.id.graphNavigationSet5], WiFiChannelsGHZ6.SET5)
        assertEquals(line2[R.id.graphNavigationSet6], WiFiChannelsGHZ6.SET6)
        assertEquals(line2[R.id.graphNavigationSet7], WiFiChannelsGHZ6.SET7)
    }

    private fun whenLines(navigationLines: NavigationLines) {
        navigationLines.entries.forEach { entry ->
            val linearLayout: LinearLayout = mock()
            lines[entry.key] = linearLayout
            whenever<View?>(view.findViewById(entry.key)).thenReturn(linearLayout)
            whenButtons(entry.value)
        }
    }

    private fun verifyLines(navigationLines: NavigationLines, selected: WiFiChannelPair) {
        navigationLines.entries.forEach { entry ->
            val linearLayout = lines[entry.key] as LinearLayout
            verify(view).findViewById<View>(entry.key)
            verify(linearLayout).visibility = visibility(entry.value)
            verifyButtons(entry.value, selected)
        }
    }

    private fun whenButtons(navigationSets: NavigationSets) {
        navigationSets.entries.forEach { entry ->
            val button: Button = mock()
            buttons[entry.key] = button
            whenever<View?>(view.findViewById(entry.key)).thenReturn(button)
        }
    }

    private fun verifyButtons(navigationSets: NavigationSets, selected: WiFiChannelPair) {
        navigationSets.entries.forEach { entry ->
            val button: Button = buttons[entry.key]!!
            val value: WiFiChannelPair = navigationSets[entry.key]!!
            val isSelected = selected == value
            val expectedColor = if (isSelected) colorSelected else colorNotSelected
            val expectedText =
                """<strong>${value.first.channel} &#8722 ${value.second.channel}</strong>""".parseAsHtml().toString()
            verify(button).setBackgroundColor(expectedColor)
            verify(button).isSelected = isSelected
            verify(button).text = expectedText
            verify(view).findViewById<View>(entry.key)
        }
    }

    private fun visibility(map: Map<Int, Any>) =
        if (map.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

}
