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
package com.vrem.wifianalyzer.wifi.accesspoint

import android.net.wifi.WifiInfo
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.model.WiFiConnection
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.scanner.UpdateNotifier

class ConnectionView(
    private val mainActivity: MainActivity,
    private val accessPointDetail: AccessPointDetail = AccessPointDetail(),
    private val accessPointPopup: AccessPointPopup = AccessPointPopup(),
    private val warningView: WarningView = WarningView(mainActivity)
) : UpdateNotifier {

    override fun update(wiFiData: WiFiData) {
        val mainContext = MainContext.INSTANCE
        displayConnection(wiFiData, mainContext.settings)
        displayWiFiSupport(mainContext.settings)
        warningView.update(wiFiData)
    }

    private fun displayWiFiSupport(settings: Settings) {
        val wiFiBand = settings.wiFiBand()
        val visibility = if (wiFiBand.available()) View.GONE else View.VISIBLE
        val textView = mainActivity.findViewById<TextView>(R.id.main_wifi_support)
        textView.visibility = visibility
        textView.text = mainActivity.resources.getString(wiFiBand.textResource)
    }

    private fun displayConnection(wiFiData: WiFiData, settings: Settings) {
        val connectionViewType = settings.connectionViewType()
        val connection = wiFiData.connection()
        val connectionView = mainActivity.findViewById<View>(R.id.connection)
        val wiFiConnection = connection.wiFiAdditional.wiFiConnection
        if (connectionViewType.hide || !wiFiConnection.connected) {
            connectionView.visibility = View.GONE
        } else {
            connectionView.visibility = View.VISIBLE
            val parent = connectionView.findViewById<ViewGroup>(R.id.connectionDetail)
            val view =
                accessPointDetail.makeView(parent.getChildAt(0), parent, connection, layout = connectionViewType.layout)
            if (parent.childCount == 0) {
                parent.addView(view)
            }
            setViewConnection(connectionView, wiFiConnection)
            attachPopup(view, connection)
        }
    }

    private fun setViewConnection(connectionView: View, wiFiConnection: WiFiConnection) {
        val ipAddress = wiFiConnection.ipAddress
        connectionView.findViewById<TextView>(R.id.ipAddress).text = ipAddress
        val textLinkSpeed = connectionView.findViewById<TextView>(R.id.linkSpeed)
        val linkSpeed = wiFiConnection.linkSpeed
        if (linkSpeed == WiFiConnection.LINK_SPEED_INVALID) {
            textLinkSpeed.visibility = View.GONE
        } else {
            textLinkSpeed.visibility = View.VISIBLE
            textLinkSpeed.text = "$linkSpeed${WifiInfo.LINK_SPEED_UNITS}"
        }
    }

    private fun attachPopup(view: View, wiFiDetail: WiFiDetail) {
        view.findViewById<View>(R.id.attachPopup)?.let {
            accessPointPopup.attach(it, wiFiDetail)
            accessPointPopup.attach(view.findViewById(R.id.ssid), wiFiDetail)
        }
    }

}