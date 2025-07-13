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
import androidx.core.content.ContextCompat
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.databinding.ChannelRatingBestBinding
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
    val channelRatingBest: ChannelRatingBestBinding,
    val channelRating: ChannelRating = ChannelRating()
) :
    ArrayAdapter<WiFiChannel>(context, R.layout.channel_rating_details, mutableListOf()), UpdateNotifier {

    private val bindingMap = mapOf(
        WiFiWidth.MHZ_20 to Pair(channelRatingBest.channelRating20, channelRatingBest.channelRatingRatingChannel20),
        WiFiWidth.MHZ_40 to Pair(channelRatingBest.channelRating40, channelRatingBest.channelRatingRatingChannel40),
        WiFiWidth.MHZ_80 to Pair(channelRatingBest.channelRating80, channelRatingBest.channelRatingRatingChannel80),
        WiFiWidth.MHZ_160 to Pair(channelRatingBest.channelRating160, channelRatingBest.channelRatingRatingChannel160),
        WiFiWidth.MHZ_320 to Pair(channelRatingBest.channelRating320, channelRatingBest.channelRatingRatingChannel320)
    )

    override fun update(wiFiData: WiFiData) {
        val settings = MainContext.INSTANCE.settings
        val wiFiBand = settings.wiFiBand()
        val countryCode = settings.countryCode()
        val wiFiChannels: List<WiFiChannel> = wiFiChannels(wiFiBand, countryCode)
        val predicate: Predicate = wiFiBand.predicate()
        val wiFiDetails: List<WiFiDetail> = wiFiData.wiFiDetails(predicate, SortBy.STRENGTH)
        channelRating.wiFiDetails(wiFiDetails)
        bestChannels(wiFiBand, wiFiChannels)
        notifyDataSetChanged()
    }

    private fun wiFiChannels(wiFiBand: WiFiBand, countryCode: String): List<WiFiChannel> {
        val wiFiChannels: List<WiFiChannel> = wiFiBand.wiFiChannels.availableChannels(wiFiBand, countryCode)
        clear()
        addAll(wiFiChannels)
        return wiFiChannels
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val wiFiBand = MainContext.INSTANCE.settings.wiFiBand()
        val binding = view?.let { ChannelRatingAdapterBinding(it) } ?: ChannelRatingAdapterBinding(create(parent))
        getItem(position)?.let {
            val wiFiWidth = wiFiBand.wiFiChannels.wiFiWidthByChannel(it.channel)
            binding.channelRatingChannel.text = it.channel.toString()
            binding.channelRatingAPCount.text = channelRating.count(it).toString()
            binding.channelRatingWidth.text = ContextCompat.getString(context, wiFiWidth.textResource)
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

    internal fun bestChannels(wiFiBand: WiFiBand, wiFiChannels: List<WiFiChannel>) {
        val channels = channelRating.bestChannels(wiFiBand, wiFiChannels)
        val channelRatingMessage = channelRatingBest.channelRatingMessage
        if (channels.isEmpty()) {
            channelRatingMessage.text = errorMessage(wiFiBand)
            channelRatingMessage.setTextColor(ContextCompat.getColor(context, R.color.error))
        } else {
            channelRatingMessage.text = String.EMPTY
            channelRatingMessage.setTextColor(ContextCompat.getColor(context, R.color.success))
        }
        updateChannelRatings(channels)
    }

    private fun updateChannelRatings(channelAPCounts: List<ChannelAPCount>) {
        WiFiWidth.entries.forEach { wiFiWidth ->
            val channels = channelAPCounts
                .filter { it.wiFiWidth == wiFiWidth }
                .map { it.wiFiChannel.channel }
                .joinToString(",")
            val visibility = if (channels.isEmpty()) View.GONE else View.VISIBLE
            bindingMap[wiFiWidth]?.let { (channelRatingView, channelRatingTextView) ->
                channelRatingView.visibility = visibility
                channelRatingTextView.text = channels
            }
        }
    }

    private fun errorMessage(wiFiBand: WiFiBand): String = with(context.resources) {
        if (WiFiBand.GHZ2 == wiFiBand) {
            String.format(
                getString(R.string.channel_rating_best_alternative),
                getString(R.string.channel_rating_best_none),
                getString(WiFiBand.GHZ5.textResource)
            )
        } else {
            getString(R.string.channel_rating_best_none)
        }
    }

    private fun create(parent: ViewGroup): ChannelRatingDetailsBinding =
        ChannelRatingDetailsBinding.inflate(MainContext.INSTANCE.layoutInflater, parent, false)

}
