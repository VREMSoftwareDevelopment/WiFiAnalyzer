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

import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.databinding.ChannelRatingDetailsBinding

internal class ChannelRatingAdapterBinding {
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
