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
package com.vrem.wifianalyzer.wifi.channelrating

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.databinding.ChannelRatingBestBinding
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannel
import com.vrem.wifianalyzer.wifi.model.ChannelAPCount
import com.vrem.wifianalyzer.wifi.model.ChannelRating
import com.vrem.wifianalyzer.wifi.model.WiFiWidth
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters
import org.robolectric.annotation.Config

@RunWith(ParameterizedRobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class ChannelRatingAdapterParameterizedTest(
    val wiFiWidth: WiFiWidth,
) {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val settings = MainContextHelper.INSTANCE.settings
    private val channelRating: ChannelRating = mock()
    private val inflater: LayoutInflater = LayoutInflater.from(mainActivity)
    private val parent: ViewGroup = LinearLayout(mainActivity)
    private val binding: ChannelRatingBestBinding = ChannelRatingBestBinding.inflate(inflater, parent, false)
    private val fixture = ChannelRatingAdapter(mainActivity, binding, channelRating)

    private val bindingMap =
        mapOf(
            WiFiWidth.MHZ_20 to Pair(binding.channelRating20, binding.channelRatingRatingChannel20),
            WiFiWidth.MHZ_40 to Pair(binding.channelRating40, binding.channelRatingRatingChannel40),
            WiFiWidth.MHZ_80 to Pair(binding.channelRating80, binding.channelRatingRatingChannel80),
            WiFiWidth.MHZ_160 to Pair(binding.channelRating160, binding.channelRatingRatingChannel160),
            WiFiWidth.MHZ_320 to Pair(binding.channelRating320, binding.channelRatingRatingChannel320),
        )

    @After
    fun tearDown() {
        verifyNoMoreInteractions(settings)
        verifyNoMoreInteractions(channelRating)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun bestChannelsRatingGHZ2NotVisible() {
        // setup
        val wiFiChannels: List<WiFiChannel> = listOf()
        val channelAPCounts: List<ChannelAPCount> = listOf()
        doReturn(channelAPCounts).whenever(channelRating).bestChannels(WiFiBand.GHZ2, wiFiChannels)
        // execute
        fixture.bestChannels(WiFiBand.GHZ2, wiFiChannels)
        // validate
        bindingMap[wiFiWidth]?.let { (channelRatingView, channelRatingTextView) ->
            assertThat(channelRatingView.visibility).describedAs("$wiFiWidth").isEqualTo(View.GONE)
            assertThat(channelRatingTextView.text).describedAs("$wiFiWidth").isEmpty()
        }
        verify(channelRating).bestChannels(WiFiBand.GHZ2, wiFiChannels)
    }

    @Test
    fun bestChannelsRatingGHZ5NotVisible() {
        // setup
        val wiFiChannels: List<WiFiChannel> = listOf()
        val channelAPCounts: List<ChannelAPCount> = listOf()
        doReturn(channelAPCounts).whenever(channelRating).bestChannels(WiFiBand.GHZ5, wiFiChannels)
        // execute
        fixture.bestChannels(WiFiBand.GHZ5, wiFiChannels)
        // validate
        bindingMap[wiFiWidth]?.let { (channelRatingView, channelRatingTextView) ->
            assertThat(channelRatingView.visibility).describedAs("$wiFiWidth").isEqualTo(View.GONE)
            assertThat(channelRatingTextView.text).describedAs("$wiFiWidth").isEmpty()
        }
        verify(channelRating).bestChannels(WiFiBand.GHZ5, wiFiChannels)
    }

    @Test
    fun bestChannelsRatingGHZ5Visible() {
        // setup
        val wiFiChannels: List<WiFiChannel> = listOf()
        val channelAPCounts = withChannelAPCounts()
        val expected =
            channelAPCounts
                .filter { it.wiFiWidth == wiFiWidth }
                .map { it.wiFiChannel.channel }
                .joinToString(",")
        doReturn(channelAPCounts).whenever(channelRating).bestChannels(WiFiBand.GHZ5, wiFiChannels)
        // execute
        fixture.bestChannels(WiFiBand.GHZ5, wiFiChannels)
        // validate
        bindingMap[wiFiWidth]?.let { (channelRatingView, channelRatingTextView) ->
            assertThat(channelRatingView.visibility).describedAs("$wiFiWidth").isEqualTo(View.VISIBLE)
            assertThat(channelRatingTextView.text).describedAs("$wiFiWidth").isEqualTo(expected)
        }
        verify(channelRating).bestChannels(WiFiBand.GHZ5, wiFiChannels)
    }

    private fun withChannelAPCounts(): List<ChannelAPCount> =
        (1..9).map { channelAPCount(it, WiFiWidth.MHZ_20) } +
            (30..39).map { channelAPCount(it, WiFiWidth.MHZ_40) } +
            (40..49).map { channelAPCount(it, WiFiWidth.MHZ_80) } +
            (50..59).map { channelAPCount(it, WiFiWidth.MHZ_160) } +
            (60..69).map { channelAPCount(it, WiFiWidth.MHZ_320) }

    private fun channelAPCount(
        channel: Int,
        wiFiWidth: WiFiWidth,
    ): ChannelAPCount = ChannelAPCount(WiFiChannel(channel, channel + 100), wiFiWidth, 1)

    companion object {
        @JvmStatic
        @Parameters(name = "{index}: {0}")
        fun data() = WiFiWidth.entries
    }
}
