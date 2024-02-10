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
import com.vrem.wifianalyzer.wifi.model.FastRoaming
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiSecurity
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OpenClass
class AccessPointDetail {
    private val vendorShortMax = 12
    private val vendorLongMax = 30

    fun makeView(
        convertView: View?,
        parent: ViewGroup?,
        wiFiDetail: WiFiDetail,
        child: Boolean = false,
        @LayoutRes layout: Int = MainContext.INSTANCE.settings.accessPointView().layout
    )
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
        setViewCapabilitiesLong(view, wiFiDetail.wiFiSecurity)
        setViewSecurityTypes(view, wiFiDetail.wiFiSecurity)
        setViewVendorLong(view, wiFiDetail.wiFiAdditional)
        setViewWiFiBand(view, wiFiDetail.wiFiSignal)
        setView80211mc(view, wiFiDetail.wiFiSignal)
        setViewWiFiStandard(view, wiFiDetail.wiFiSignal)
        setViewFastRoaming(view, wiFiDetail.wiFiSignal)
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
            val security = wiFiDetail.wiFiSecurity.security
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
            view.findViewById<TextView>(R.id.capabilities).text = wiFiDetail.wiFiSecurity.securities
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

    private fun setViewCapabilitiesLong(view: View, wiFiSecurity: WiFiSecurity) =
        view.findViewById<TextView>(R.id.capabilitiesLong)?.let {
            it.text = wiFiSecurity.capabilities
        }

    private fun setViewSecurityTypes(view: View, wiFiSecurity: WiFiSecurity) =
        view.findViewById<TextView>(R.id.securityTypes)?.let {
            it.text = wiFiSecurity.wiFiSecurityTypes
                .map { securityType -> view.context.getString(securityType.textResource) }
                .filter { text -> text.isNotBlank() }
                .toList()
                .joinToString(" ", "[", "]")
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


    private fun setViewFastRoaming(view: View, wiFiSignal: WiFiSignal) {
        view.findViewById<TextView>(R.id.fastRoaming)?.let {
            if (wiFiSignal.fastRoaming.isEmpty()) {
                it.setText(R.string.unsupported_802_11_k_v_r)
            } else if (wiFiSignal.fastRoaming.contains(FastRoaming.REQUIRE_ANDROID_R)) {
                it.setText(R.string.require_android_11_802_11_k_v_r)
            } else {
                it.text = wiFiSignal.fastRoaming.joinToString("") { fastRoaming ->
                    "[${fastRoaming.protocol}]"
                }
            }
        }
    }
    private fun setViewWiFiStandard(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<TextView>(R.id.wiFiStandard)
            ?.setText(wiFiSignal.wiFiStandard.textResource)

    private fun setView80211mc(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<TextView>(R.id.flag80211mc)?.let {
            it.visibility = if (wiFiSignal.is80211mc) View.VISIBLE else View.GONE
        }

    private fun setTimestamp(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<TextView>(R.id.timestamp)?.let {
            val milliseconds: Long = wiFiSignal.timestamp / 1000
            if (0L == milliseconds) {
                it.text = String.EMPTY
                it.visibility = View.GONE
            } else {
                val simpleDateFormat = SimpleDateFormat(TIME_STAMP_FORMAT, Locale.US)
                simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
                it.text = simpleDateFormat.format(Date(milliseconds))
                it.visibility = View.VISIBLE
            }
        }

    companion object {
        private const val TIME_STAMP_FORMAT = "H:mm:ss.SSS"
    }

}