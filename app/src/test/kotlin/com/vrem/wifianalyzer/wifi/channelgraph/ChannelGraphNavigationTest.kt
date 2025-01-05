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
package com.vrem.wifianalyzer.wifi.channelgraph

import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.text.parseAsHtml
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

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
    fun updateWithGHZ2() {
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
    fun updateWithGHZ5() {
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
    fun updateWithGHZ6() {
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
    fun setOnClickListener() {
        // setup
        val expected = WiFiChannelsGHZ5.SET3
        // execute
        fixture.onClickListener(WiFiBand.GHZ5, expected)
        // validate
        verify(configuration).wiFiChannelPair(WiFiBand.GHZ5, expected)
        verify(scanner).update()
    }

    @Test
    fun navigationGHZ2Lines() {
        assertThat(navigationGHZ2Lines).isEmpty()
    }

    @Test
    fun navigationGHZ5Lines() {
        assertThat(navigationGHZ5Lines).hasSize(2)
        assertThat(navigationGHZ5Lines[R.id.graphNavigationLine1]).isNotEmpty()
        assertThat(navigationGHZ5Lines[R.id.graphNavigationLine2]).isEmpty()
    }

    @Test
    fun navigationGHZ6Lines() {
        assertThat(navigationGHZ6Lines).hasSize(2)
        assertThat(navigationGHZ6Lines[R.id.graphNavigationLine1]).isNotEmpty()
        assertThat(navigationGHZ6Lines[R.id.graphNavigationLine2]).isNotEmpty()
    }

    @Test
    fun navigationGHZ5Line1() {
        val line1 = navigationGHZ5Lines[R.id.graphNavigationLine1]!!
        assertThat(line1).hasSize(3)
        assertThat(WiFiChannelsGHZ5.SET1).isEqualTo(line1[R.id.graphNavigationSet1])
        assertThat(WiFiChannelsGHZ5.SET2).isEqualTo(line1[R.id.graphNavigationSet2])
        assertThat(WiFiChannelsGHZ5.SET3).isEqualTo(line1[R.id.graphNavigationSet3])
    }

    @Test
    fun navigationGHZ6Line1() {
        val line1 = navigationGHZ6Lines[R.id.graphNavigationLine1]!!
        assertThat(line1).hasSize(3)
        assertThat(WiFiChannelsGHZ6.SET1).isEqualTo(line1[R.id.graphNavigationSet1])
        assertThat(WiFiChannelsGHZ6.SET2).isEqualTo(line1[R.id.graphNavigationSet2])
        assertThat(WiFiChannelsGHZ6.SET3).isEqualTo(line1[R.id.graphNavigationSet3])
    }

    @Test
    fun navigationGHZ6Line2() {
        val line2 = navigationGHZ6Lines[R.id.graphNavigationLine2]!!
        assertThat(line2).hasSize(4)
        assertThat(WiFiChannelsGHZ6.SET4).isEqualTo(line2[R.id.graphNavigationSet4])
        assertThat(WiFiChannelsGHZ6.SET5).isEqualTo(line2[R.id.graphNavigationSet5])
        assertThat(WiFiChannelsGHZ6.SET6).isEqualTo(line2[R.id.graphNavigationSet6])
        assertThat(WiFiChannelsGHZ6.SET7).isEqualTo(line2[R.id.graphNavigationSet7])
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
