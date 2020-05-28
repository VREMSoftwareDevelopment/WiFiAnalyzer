/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TextView
import com.vrem.util.EMPTY
import com.vrem.util.buildMinVersionL
import com.vrem.util.compatColor
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.databinding.ChannelRatingDetailsBinding
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannel
import com.vrem.wifianalyzer.wifi.model.*
import com.vrem.wifianalyzer.wifi.model.Strength.Companion.reverse
import com.vrem.wifianalyzer.wifi.predicate.WiFiBandPredicate
import com.vrem.wifianalyzer.wifi.scanner.UpdateNotifier
import org.apache.commons.collections4.Predicate

class ChannelRatingAdapter(
        context: Context,
        private val bestChannels: TextView,
        private val channelRating: ChannelRating = ChannelRating()) :
        ArrayAdapter<WiFiChannel>(context, R.layout.channel_rating_details, mutableListOf()),
        UpdateNotifier {

    private val maxChannelsToDisplay = 11

    override fun update(wiFiData: WiFiData) {
        val settings = MainContext.INSTANCE.settings
        val wiFiBand = settings.wiFiBand()
        val countryCode = settings.countryCode()
        val wiFiChannels: List<WiFiChannel> = wiFiChannels(wiFiBand, countryCode)
        val predicate: Predicate<WiFiDetail> = WiFiBandPredicate(wiFiBand)
        val wiFiDetails: List<WiFiDetail> = wiFiData.wiFiDetails(predicate, SortBy.STRENGTH)
        channelRating.wiFiDetails(wiFiDetails)
        bestChannels(wiFiBand, wiFiChannels)
        notifyDataSetChanged()
    }

    private fun wiFiChannels(wiFiBand: WiFiBand, countryCode: String): List<WiFiChannel> {
        val wiFiChannels: List<WiFiChannel> = wiFiBand.wiFiChannels.availableChannels(countryCode)
        clear()
        addAll(wiFiChannels)
        return wiFiChannels
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val binding = view?.let { Binding(it) } ?: Binding(create(parent))
        getItem(position)?.let {
            binding.channelNumber.text = it.channel.toString()
            binding.accessPointCount.text = channelRating.count(it).toString()
            ratingBar(it, binding.channelRating)
        }
        return binding.root
    }

    private fun ratingBar(wiFiChannel: WiFiChannel, ratingBar: RatingBar) {
        val strength = reverse(channelRating.strength(wiFiChannel))
        val size = Strength.values().size
        ratingBar.max = size
        ratingBar.numStars = size
        ratingBar.rating = strength.ordinal + 1.toFloat()
        val color = context.compatColor(strength.colorResource())
        ratingBarColor(ratingBar, color)
    }

    private fun ratingBarColor(ratingBar: RatingBar, color: Int): Unit =
            if (buildMinVersionL()) {
                ratingBar.progressTintList = ColorStateList.valueOf(color)
            } else {
                setRatingBarColorLegacy(ratingBar.progressDrawable, color)
            }

    @Suppress("deprecation")
    private fun setRatingBarColorLegacy(drawable: Drawable, color: Int) {
        try {
            val background = context.compatColor(R.color.background)
            val layerDrawable = drawable as LayerDrawable
            layerDrawable.getDrawable(0).setColorFilter(background, PorterDuff.Mode.SRC_ATOP)
            layerDrawable.getDrawable(1).setColorFilter(background, PorterDuff.Mode.SRC_ATOP)
            layerDrawable.getDrawable(2).setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        } catch (e: Exception) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    fun bestChannels(wiFiBand: WiFiBand, wiFiChannels: List<WiFiChannel>) {
        val result: List<Int> = channelRating.bestChannels(wiFiChannels).map { it.wiFiChannel.channel }
        if (result.isNotEmpty()) {
            bestChannels.text = result.joinToString(separator = ", ", limit = maxChannelsToDisplay)
            bestChannels.setTextColor(context.compatColor(R.color.success))
        } else {
            bestChannels.text = errorMessage(wiFiBand)
            bestChannels.setTextColor(context.compatColor(R.color.error))
        }
    }

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

    private inner class Binding {
        val root: View
        val channelNumber: TextView
        val accessPointCount: TextView
        val channelRating: RatingBar

        internal constructor(binding: ChannelRatingDetailsBinding) {
            root = binding.root
            channelNumber = binding.channelNumber
            accessPointCount = binding.accessPointCount
            channelRating = binding.channelRating
        }

        internal constructor(view: View) {
            root = view
            channelNumber = view.findViewById(R.id.channelNumber)
            accessPointCount = view.findViewById(R.id.accessPointCount)
            channelRating = view.findViewById(R.id.channelRating)
        }

    }
}
