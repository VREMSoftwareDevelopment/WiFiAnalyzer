/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.accesspoint

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.vrem.annotation.OpenClass
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import java.lang.StringBuffer
import java.util.*

@OpenClass
class AccessPointDetail {
    private val vendorShortMax = 12
    private val vendorLongMax = 30

    fun makeView(convertView: View?,
                 parent: ViewGroup?,
                 wiFiDetail: WiFiDetail,
                 child: Boolean = false,
                 @LayoutRes layout: Int = MainContext.INSTANCE.settings.accessPointView().layout)
            : View {
        val view = convertView ?: MainContext.INSTANCE.layoutInflater.inflate(layout, parent, false)
        setViewCompact(view, wiFiDetail, child)
        setViewExtra(view, wiFiDetail)
        setViewVendorShort(view, wiFiDetail.wiFiAdditional)
        return view
    }

    fun makeViewDetailed(wiFiDetail: WiFiDetail): View {
        val view = MainContext.INSTANCE.layoutInflater.inflate(R.layout.access_point_view_popup, null)
        setViewCompact(view, wiFiDetail, false)
        setViewExtra(view, wiFiDetail)
        setViewCapabilitiesLong(view, wiFiDetail)
        setViewVendorLong(view, wiFiDetail.wiFiAdditional)
        setViewWiFiBand(view, wiFiDetail.wiFiSignal)
        setView80211mc(view, wiFiDetail.wiFiSignal)
        setViewWiFiStandard(view, wiFiDetail.wiFiSignal)
        setTimestamp(view, wiFiDetail.wiFiSignal)
        enableTextSelection(view)
        return view
    }

    private fun enableTextSelection(view: View) =
        view.findViewById<TextView>(R.id.ssid)?.let {
            it.setTextIsSelectable(true)
            view.findViewById<TextView>(R.id.vendorLong).setTextIsSelectable(true)
        }

    private fun setViewCompact(view: View, wiFiDetail: WiFiDetail, child: Boolean) =
        view.findViewById<TextView>(R.id.ssid)?.let {
            it.text = wiFiDetail.wiFiIdentifier.title
            val wiFiSignal = wiFiDetail.wiFiSignal
            view.findViewById<TextView>(R.id.channel).text = wiFiSignal.channelDisplay()
            view.findViewById<TextView>(R.id.primaryFrequency).text =
                "${wiFiSignal.primaryFrequency}${WiFiSignal.FREQUENCY_UNITS}"
            view.findViewById<TextView>(R.id.distance).text = wiFiSignal.distance
            view.findViewById<View>(R.id.tab).visibility = if (child) View.VISIBLE else View.GONE
            setSecurityImage(view, wiFiDetail)
            setLevelText(view, wiFiSignal)
        }

    private fun setSecurityImage(view: View, wiFiDetail: WiFiDetail) =
        view.findViewById<ImageView>(R.id.securityImage)?.let {
            val security = wiFiDetail.security
            it.tag = security.imageResource
            it.setImageResource(security.imageResource)
        }


    private fun setViewExtra(view: View, wiFiDetail: WiFiDetail) =
        view.findViewById<TextView>(R.id.channel_frequency_range)?.let {
            val wiFiSignal = wiFiDetail.wiFiSignal
            setLevelImage(view, wiFiSignal)
            setWiFiStandardImage(view, wiFiSignal)
            it.text = "${wiFiSignal.frequencyStart} - ${wiFiSignal.frequencyEnd}"
            view.findViewById<TextView>(R.id.width).text =
                "(${wiFiSignal.wiFiWidth.frequencyWidth}${WiFiSignal.FREQUENCY_UNITS})"
            view.findViewById<TextView>(R.id.capabilities).text = wiFiDetail.securities
                .toList()
                .joinToString(" ", "[", "]")
        }

    private fun setWiFiStandardImage(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<ImageView>(R.id.wiFiStandardImage)?.let {
            it.tag = wiFiSignal.wiFiStandard.imageResource
            it.setImageResource(wiFiSignal.wiFiStandard.imageResource)
        }

    private fun setLevelText(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<TextView>(R.id.level)?.let {
            it.text = "${wiFiSignal.level}dBm"
            it.setTextColor(ContextCompat.getColor(view.context, wiFiSignal.strength.colorResource))
        }

    private fun setLevelImage(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<ImageView>(R.id.levelImage)?.let {
            val strength = wiFiSignal.strength
            it.tag = strength.imageResource
            it.setImageResource(strength.imageResource)
            it.setColorFilter(ContextCompat.getColor(view.context, strength.colorResource))
        }

    private fun setViewVendorShort(view: View, wiFiAdditional: WiFiAdditional) =
        view.findViewById<TextView>(R.id.vendorShort)?.let {
            if (wiFiAdditional.vendorName.isBlank()) {
                it.visibility = View.GONE
            } else {
                it.visibility = View.VISIBLE
                it.text = wiFiAdditional.vendorName.take(vendorShortMax)
            }
        }

    private fun setViewCapabilitiesLong(view: View, wiFiDetail: WiFiDetail) =
        view.findViewById<TextView>(R.id.capabilitiesLong)?.let {
            it.text = wiFiDetail.capabilities
        }

    private fun setViewVendorLong(view: View, wiFiAdditional: WiFiAdditional) =
        view.findViewById<TextView>(R.id.vendorLong)?.let {
            if (wiFiAdditional.vendorName.isBlank()) {
                it.visibility = View.GONE
            } else {
                it.visibility = View.VISIBLE
                it.text = wiFiAdditional.vendorName.take(vendorLongMax)
            }
        }

    private fun setViewWiFiBand(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<TextView>(R.id.wiFiBand)?.setText(wiFiSignal.wiFiBand.textResource)

    private fun setViewWiFiStandard(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<TextView>(R.id.wiFiStandard)
            ?.setText(wiFiSignal.wiFiStandard.textResource)

    private fun setView80211mc(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<TextView>(R.id.flag80211mc)?.let {
            it.visibility = if (wiFiSignal.is80211mc) View.VISIBLE else View.GONE
        }

    companion object {
        val tSec  = 1000000L    // counts microseconds
        val tMin  = tSec*60
        val tHour = tMin*60
        val tDay  = tHour*24
    }

    private fun setTimestamp(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<TextView>(R.id.timestamp)?.let {
            var microseconds: Long = wiFiSignal.timestamp
            if (0L >= microseconds || 31536000000000 <= microseconds) {
                it.text = String.EMPTY
                it.visibility = View.GONE
            } else {
                val rounding : Long = when {
                    microseconds >= 1000*tDay -> tDay    //  1000d or over
                    microseconds >=   10*tDay -> tHour   //    10d ~ 99d23h
                    microseconds >=      tDay -> tMin    //     1d ~ 9d23h59m
                    microseconds >=     tHour -> tSec    //     1h ~ 23h59m59s
                    microseconds >=   10*tMin -> 100000L //    10m ~ 59m59.9s
                    microseconds >=      tMin ->  10000L //     1m ~ 9m59.99s
                    microseconds >=   10*tSec ->   1000L //    10s ~ 59.999s
                    microseconds >=      tSec ->    100L //     1s ~ 9.9999s
                    microseconds >=    100000 ->     10L //  100ms ~ 999.99ms
                    else                      ->      1L //   10ms ~ 99.999ms
                                                         // or 1μs ~ 9999μs
                }

                microseconds += rounding / 2
                microseconds -= microseconds % rounding

                val sb = StringBuffer()
                if (10000 > microseconds)
                    sb.append(microseconds).append("μs")
                else if (1000000 > microseconds)
                    sb.append(microseconds/1000.0).append("ms")
                else {
                    if (86400000000 <= microseconds) { sb.append(microseconds / 86400000000).append("d"); microseconds %=    86400000000; }
                    if ( 3600000000 <= microseconds) { sb.append(microseconds /  3600000000).append("h"); microseconds %=     3600000000; }
                    if (   60000000 <= microseconds) { sb.append(microseconds /    60000000).append("m"); microseconds %=       60000000; }
                    if (          1 <= microseconds) {
                        if (microseconds % 1000000L == 0L)
                            sb.append(microseconds / 1000000L)  // avoid trailing ".0"
                        else
                            sb.append(microseconds / 1000000.0)
                        sb.append("s")
                    }
                }
                it.text = sb.toString()
                it.visibility = View.VISIBLE
            }
        }

}
