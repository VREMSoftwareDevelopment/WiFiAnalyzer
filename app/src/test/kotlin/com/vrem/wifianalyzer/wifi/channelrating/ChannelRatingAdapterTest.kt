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
package com.vrem.wifianalyzer.wifi.channelrating

import android.os.Build
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannel
import com.vrem.wifianalyzer.wifi.model.*
import com.vrem.wifianalyzer.wifi.model.Strength.Companion.reverse
import com.vrem.wifianalyzer.wifi.predicate.Predicate
import com.vrem.wifianalyzer.wifi.predicate.predicate
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class ChannelRatingAdapterTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val settings = INSTANCE.settings
    private val channelRating: ChannelRating = mock()
    private val bestChannels = TextView(mainActivity)
    private val fixture = ChannelRatingAdapter(mainActivity, bestChannels, channelRating)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(settings)
        verifyNoMoreInteractions(channelRating)
        INSTANCE.restore()
    }

    @Test
    fun testGetView() {
        // setup
        val expectedSize = Strength.entries.size
        val expectedStrength = reverse(Strength.FOUR)
        val wiFiChannel = WiFiChannel(1, 2)
        fixture.add(wiFiChannel)
        whenever(channelRating.count(wiFiChannel)).thenReturn(5)
        whenever(channelRating.strength(wiFiChannel)).thenReturn(Strength.FOUR)
        val viewGroup = mainActivity.findViewById<ViewGroup>(android.R.id.content)
        // execute
        val actual = fixture.getView(0, null, viewGroup)
        // validate
        assertNotNull(actual)
        assertEquals("1", actual.findViewById<TextView>(R.id.channelNumber).text)
        assertEquals("5", actual.findViewById<TextView>(R.id.accessPointCount).text)
        val ratingBar = actual.findViewById<RatingBar>(R.id.channelRating)
        assertEquals(expectedSize, ratingBar.max)
        assertEquals(expectedSize, ratingBar.numStars)
        assertEquals(expectedStrength.ordinal + 1, ratingBar.rating.toInt())
        assertEquals("", bestChannels.text)
        verify(channelRating).count(wiFiChannel)
        verify(channelRating).strength(wiFiChannel)
    }

    @Test
    fun testUpdate() {
        // setup
        val expected = mainActivity.resources.getText(R.string.channel_rating_best_none).toString()
        val wiFiData = WiFiData(listOf(), WiFiConnection.EMPTY)
        val wiFiChannels = WiFiBand.GHZ5.wiFiChannels.availableChannels(Locale.US.country)
        val predicate: Predicate = WiFiBand.GHZ5.predicate()
        val wiFiDetails = wiFiData.wiFiDetails(predicate, SortBy.STRENGTH)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ5)
        whenever(settings.countryCode()).thenReturn(Locale.US.country)
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(expected, bestChannels.text)
        verify(channelRating).bestChannels(wiFiChannels)
        verify(channelRating).wiFiDetails(wiFiDetails)
        verify(settings).wiFiBand()
        verify(settings).countryCode()
    }

    @Test
    fun testBestChannelsGHZ2ErrorMessage() {
        // setup
        val resources = mainActivity.resources
        val expected = (resources.getText(R.string.channel_rating_best_none).toString()
            + resources.getText(R.string.channel_rating_best_alternative)
            + " " + resources.getString(WiFiBand.GHZ5.textResource))
        val wiFiChannels: List<WiFiChannel> = listOf()
        val channelAPCounts: List<ChannelAPCount> = listOf()
        whenever(channelRating.bestChannels(wiFiChannels)).thenReturn(channelAPCounts)
        // execute
        val actual = fixture.bestChannels(WiFiBand.GHZ2, wiFiChannels)
        // validate
        assertEquals(expected, actual.message)
        assertEquals(R.color.error, actual.color)
        verify(channelRating).bestChannels(wiFiChannels)
    }

    @Test
    fun testBestChannelsGHZ5WithErrorMessage() {
        // setup
        val expected = mainActivity.resources.getText(R.string.channel_rating_best_none).toString()
        val wiFiChannels: List<WiFiChannel> = listOf()
        val channelAPCounts: List<ChannelAPCount> = listOf()
        whenever(channelRating.bestChannels(wiFiChannels)).thenReturn(channelAPCounts)
        // execute
        val actual = fixture.bestChannels(WiFiBand.GHZ5, wiFiChannels)
        // validate
        assertEquals(expected, actual.message)
        assertEquals(R.color.error, actual.color)
        verify(channelRating).bestChannels(wiFiChannels)
    }

    @Test
    fun testBestChannelsGHZ5WithMaximumChannels() {
        // setup
        val expected = "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ..."
        val wiFiChannels: List<WiFiChannel> = listOf()
        val channelAPCounts = withMaximumChannelAPCounts()
        whenever(channelRating.bestChannels(wiFiChannels)).thenReturn(channelAPCounts)
        // execute
        val actual = fixture.bestChannels(WiFiBand.GHZ5, wiFiChannels)
        // validate
        assertEquals(expected, actual.message)
        assertEquals(R.color.success, actual.color)
        verify(channelRating).bestChannels(wiFiChannels)
    }

    @Test
    fun testBestChannelsGHZ5WithChannels() {
        // setup
        val expected = "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11"
        val wiFiChannels: List<WiFiChannel> = listOf()
        val channelAPCounts = withChannelAPCounts()
        whenever(channelRating.bestChannels(wiFiChannels)).thenReturn(channelAPCounts)
        // execute
        val actual = fixture.bestChannels(WiFiBand.GHZ5, wiFiChannels)
        // validate
        assertEquals(expected, actual.message)
        assertEquals(R.color.success, actual.color)
        verify(channelRating).bestChannels(wiFiChannels)
    }

    private fun withMaximumChannelAPCounts(): List<ChannelAPCount> =
        (0..11).map { ChannelAPCount(WiFiChannel(it + 1, it + 100), 0) }

    private fun withChannelAPCounts(): List<ChannelAPCount> =
        (0..10).map { ChannelAPCount(WiFiChannel(it + 1, it + 100), 0) }

}