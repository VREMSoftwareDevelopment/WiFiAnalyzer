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

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.databinding.ChannelRatingDetailsBinding
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannel
import com.vrem.wifianalyzer.wifi.model.*
import com.vrem.wifianalyzer.wifi.model.Strength.Companion.reverse
import com.vrem.wifianalyzer.wifi.predicate.Predicate
import com.vrem.wifianalyzer.wifi.predicate.predicate
import com.vrem.wifianalyzer.wifi.scanner.UpdateNotifier

class ChannelRatingAdapter(
    context: Context,
    private val bestChannels: TextView,
    private val channelRating: ChannelRating = ChannelRating()
) :
    ArrayAdapter<WiFiChannel>(context, R.layout.channel_rating_details, mutableListOf()),
    UpdateNotifier {

    private val maxChannelsToDisplay = 11

    override fun update(wiFiData: WiFiData) {
        val settings = MainContext.INSTANCE.settings
        val wiFiBand = settings.wiFiBand()
        val countryCode = settings.countryCode()
        val wiFiChannels: List<WiFiChannel> = wiFiChannels(wiFiBand, countryCode)
        val predicate: Predicate = wiFiBand.predicate()
        val wiFiDetails: List<WiFiDetail> = wiFiData.wiFiDetails(predicate, SortBy.STRENGTH)
        channelRating.wiFiDetails(wiFiDetails)
        val bestChannel = bestChannels(wiFiBand, wiFiChannels)
        bestChannels.text = bestChannel.message
        bestChannels.setTextColor(ContextCompat.getColor(context, bestChannel.color))
        notifyDataSetChanged()
    }

    private fun wiFiChannels(wiFiBand: WiFiBand, countryCode: String): List<WiFiChannel> {
        val wiFiChannels: List<WiFiChannel> = wiFiBand.wiFiChannels.availableChannels(countryCode)
        clear()
        addAll(wiFiChannels)
        return wiFiChannels
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val binding = view?.let { ChannelRatingAdapterBinding(it) } ?: ChannelRatingAdapterBinding(create(parent))
        getItem(position)?.let {
            binding.channelNumber.text = it.channel.toString()
            binding.accessPointCount.text = channelRating.count(it).toString()
            ratingBar(it, binding.channelRating)
        }
        return binding.root
    }

    private fun ratingBar(wiFiChannel: WiFiChannel, ratingBar: RatingBar) {
        val strength = reverse(channelRating.strength(wiFiChannel))
        val size = Strength.entries.size
        ratingBar.max = size
        ratingBar.numStars = size
        ratingBar.rating = strength.ordinal + 1.toFloat()
        val color = ContextCompat.getColor(context, strength.colorResource)
        ratingBar.progressTintList = ColorStateList.valueOf(color)
    }

    internal fun bestChannels(wiFiBand: WiFiBand, wiFiChannels: List<WiFiChannel>): Message {
        val bestChannels: List<Int> = channelRating.bestChannels(wiFiChannels).map { it.wiFiChannel.channel }
        return if (bestChannels.isNotEmpty()) {
            Message(bestChannels.joinToString(separator = ", ", limit = maxChannelsToDisplay), R.color.success)
        } else {
            Message(errorMessage(wiFiBand), R.color.error)
        }
    }

    internal class Message(val message: String, @ColorRes val color: Int)

    private fun errorMessage(wiFiBand: WiFiBand): String = with(context.resources) {
        getText(R.string.channel_rating_best_none).toString() +
            if (WiFiBand.GHZ2 == wiFiBand) {
                getText(R.string.channel_rating_best_alternative).toString() +
                    " " + getString(WiFiBand.GHZ5.textResource)
            } else {
                String.EMPTY
            }
    }

    private fun create(parent: ViewGroup): ChannelRatingDetailsBinding =
        ChannelRatingDetailsBinding.inflate(MainContext.INSTANCE.layoutInflater, parent, false)

}
