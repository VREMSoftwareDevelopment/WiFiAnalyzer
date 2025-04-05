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
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.databinding.ChannelRatingBestBinding
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannel
import com.vrem.wifianalyzer.wifi.model.*
import com.vrem.wifianalyzer.wifi.model.Strength.Companion.reverse
import com.vrem.wifianalyzer.wifi.predicate.Predicate
import com.vrem.wifianalyzer.wifi.predicate.predicate
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config
import java.util.Locale

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
class ChannelRatingAdapterTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val settings = MainContextHelper.INSTANCE.settings
    private val channelRating: ChannelRating = mock()
    private val inflater: LayoutInflater = LayoutInflater.from(mainActivity)
    private val parent: ViewGroup = LinearLayout(mainActivity)
    private val binding: ChannelRatingBestBinding = ChannelRatingBestBinding.inflate(inflater, parent, false)
    private val fixture = ChannelRatingAdapter(mainActivity, binding, channelRating)

    private val bindingMap = mapOf(
        WiFiWidth.MHZ_20 to Pair(binding.channelRating20, binding.channelRatingRatingChannel20),
        WiFiWidth.MHZ_40 to Pair(binding.channelRating40, binding.channelRatingRatingChannel40),
        WiFiWidth.MHZ_80 to Pair(binding.channelRating80, binding.channelRatingRatingChannel80),
        WiFiWidth.MHZ_160 to Pair(binding.channelRating160, binding.channelRatingRatingChannel160),
        WiFiWidth.MHZ_320 to Pair(binding.channelRating320, binding.channelRatingRatingChannel320)
    )

    @After
    fun tearDown() {
        verifyNoMoreInteractions(settings)
        verifyNoMoreInteractions(channelRating)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun getView() {
        // setup
        val expectedSize = Strength.entries.size
        val expectedStrength = reverse(Strength.FOUR)
        val wiFiChannel = WiFiChannel(1, 2)
        fixture.add(wiFiChannel)
        val wiFiBand = WiFiBand.GHZ5
        doReturn(wiFiBand).whenever(settings).wiFiBand()
        doReturn(5).whenever(channelRating).count(wiFiChannel)
        doReturn(Strength.FOUR).whenever(channelRating).strength(wiFiChannel)
        val viewGroup = mainActivity.findViewById<ViewGroup>(android.R.id.content)
        // execute
        val actual = fixture.getView(0, null, viewGroup)
        // validate
        assertThat(actual).isNotNull()
        assertThat(actual.findViewById<TextView>(R.id.channelRatingChannel).text).isEqualTo("1")
        assertThat(actual.findViewById<TextView>(R.id.channelRatingWidth).text).isEqualTo("20 MHz")
        assertThat(actual.findViewById<TextView>(R.id.channelRatingAPCount).text).isEqualTo("5")
        val ratingBar = actual.findViewById<RatingBar>(R.id.channelRating)
        assertThat(ratingBar.max).isEqualTo(expectedSize)
        assertThat(ratingBar.numStars).isEqualTo(expectedSize)
        assertThat(ratingBar.rating.toInt()).isEqualTo(expectedStrength.ordinal + 1)
        verify(settings).wiFiBand()
        verify(channelRating).count(wiFiChannel)
        verify(channelRating).strength(wiFiChannel)
    }

    @Test
    fun update() {
        // setup
        val expected = mainActivity.resources.getText(R.string.channel_rating_best_none).toString()
        val wiFiData = WiFiData(listOf(), WiFiConnection.EMPTY)
        val wiFiBand = WiFiBand.GHZ5
        val wiFiChannels = wiFiBand.wiFiChannels.availableChannels(wiFiBand, Locale.US.country)
        val predicate: Predicate = wiFiBand.predicate()
        val wiFiDetails = wiFiData.wiFiDetails(predicate, SortBy.STRENGTH)
        doReturn(wiFiBand).whenever(settings).wiFiBand()
        doReturn(Locale.US.country).whenever(settings).countryCode()
        // execute
        fixture.update(wiFiData)
        // validate
        assertThat(binding.channelRatingMessage.text).isEqualTo(expected)
        verify(channelRating).bestChannels(wiFiBand, wiFiChannels)
        verify(channelRating).wiFiDetails(wiFiDetails)
        verify(settings).wiFiBand()
        verify(settings).countryCode()
    }

    @Test
    fun bestChannelsGHZ2WithErrorMessage() {
        // setup
        val resources = mainActivity.resources
        val expected = (resources.getText(R.string.channel_rating_best_none).toString()
            + resources.getText(R.string.channel_rating_best_alternative)
            + " " + resources.getString(WiFiBand.GHZ5.textResource))
        val expectedColor = ContextCompat.getColor(mainActivity, R.color.error)
        val wiFiChannels: List<WiFiChannel> = listOf()
        val channelAPCounts: List<ChannelAPCount> = listOf()
        doReturn(channelAPCounts).whenever(channelRating).bestChannels(WiFiBand.GHZ2, wiFiChannels)
        // execute
        fixture.bestChannels(WiFiBand.GHZ2, wiFiChannels)
        // validate
        assertThat(binding.channelRatingMessage.text).isEqualTo(expected)
        assertThat(binding.channelRatingMessage.textColors.defaultColor).isEqualTo(expectedColor)
        WiFiWidth.entries.forEach { wiFiWidth ->
            bindingMap[wiFiWidth]?.let { (channelRatingView, channelRatingTextView) ->
                assertThat(channelRatingView.visibility).describedAs("$wiFiWidth").isEqualTo(View.GONE)
                assertThat(channelRatingTextView.text).describedAs("$wiFiWidth").isEmpty()
            }
        }
        verify(channelRating).bestChannels(WiFiBand.GHZ2, wiFiChannels)
    }

    @Test
    fun bestChannelsGHZ5WithErrorMessage() {
        // setup
        val expected = mainActivity.resources.getText(R.string.channel_rating_best_none).toString()
        val expectedColor = ContextCompat.getColor(mainActivity, R.color.error)
        val wiFiChannels: List<WiFiChannel> = listOf()
        val channelAPCounts: List<ChannelAPCount> = listOf()
        doReturn(channelAPCounts).whenever(channelRating).bestChannels(WiFiBand.GHZ5, wiFiChannels)
        // execute
        fixture.bestChannels(WiFiBand.GHZ5, wiFiChannels)
        // validate
        assertThat(binding.channelRatingMessage.text).isEqualTo(expected)
        assertThat(binding.channelRatingMessage.textColors.defaultColor).isEqualTo(expectedColor)
        WiFiWidth.entries.forEach { wiFiWidth ->
            bindingMap[wiFiWidth]?.let { (channelRatingView, channelRatingTextView) ->
                assertThat(channelRatingView.visibility).describedAs("$wiFiWidth").isEqualTo(View.GONE)
                assertThat(channelRatingTextView.text).describedAs("$wiFiWidth").isEmpty()
            }
        }
        verify(channelRating).bestChannels(WiFiBand.GHZ5, wiFiChannels)
    }

    @Test
    fun bestChannelsGHZ5WithSuccessMessage() {
        // setup
        val wiFiChannels: List<WiFiChannel> = listOf()
        val channelAPCounts = withChannelAPCounts()
        val expectedColor = ContextCompat.getColor(mainActivity, R.color.success)
        doReturn(channelAPCounts).whenever(channelRating).bestChannels(WiFiBand.GHZ5, wiFiChannels)
        // execute
        fixture.bestChannels(WiFiBand.GHZ5, wiFiChannels)
        // validate
        assertThat(binding.channelRatingMessage.text).isEqualTo(String.EMPTY)
        assertThat(binding.channelRatingMessage.textColors.defaultColor).isEqualTo(expectedColor)
        WiFiWidth.entries.forEach { wiFiWidth ->
            val expected = channelAPCounts.filter { it.wiFiWidth == wiFiWidth }
                .map { it.wiFiChannel.channel }
                .joinToString(",")
            bindingMap[wiFiWidth]?.let { (channelRatingView, channelRatingTextView) ->
                assertThat(channelRatingView.visibility).describedAs("$wiFiWidth").isEqualTo(View.VISIBLE)
                assertThat(channelRatingTextView.text).describedAs("$wiFiWidth").isEqualTo(expected)
            }
        }
        verify(channelRating).bestChannels(WiFiBand.GHZ5, wiFiChannels)
    }

    private fun withChannelAPCounts(): List<ChannelAPCount> =
        (1..9).map { channelAPCount(it, WiFiWidth.MHZ_20) } +
            (30..39).map { channelAPCount(it, WiFiWidth.MHZ_40) } +
            (40..49).map { channelAPCount(it, WiFiWidth.MHZ_80) } +
            (50..59).map { channelAPCount(it, WiFiWidth.MHZ_160) } +
            (60..69).map { channelAPCount(it, WiFiWidth.MHZ_320) }

    private fun channelAPCount(channel: Int, wiFiWidth: WiFiWidth): ChannelAPCount =
        ChannelAPCount(WiFiChannel(channel, channel + 100), wiFiWidth, 1)

}