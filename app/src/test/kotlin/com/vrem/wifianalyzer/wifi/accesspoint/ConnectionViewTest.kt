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

import android.net.wifi.WifiInfo
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class ConnectionViewTest {
    private val ssid = "SSID"
    private val bssid = "BSSID"
    private val ipAddress = "IP-ADDRESS"
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val settings = MainContextHelper.INSTANCE.settings
    private val wiFiManagerWrapper = MainContextHelper.INSTANCE.wiFiManagerWrapper
    private val wiFiData: WiFiData = mock()
    private val accessPointDetail: AccessPointDetail = mock()
    private val accessPointPopup: AccessPointPopup = mock()
    private val warningView: WarningView = mock()
    private val fixture = ConnectionView(mainActivity, accessPointDetail, accessPointPopup, warningView)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(warningView)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun connectionGoneWithNoConnectionInformation() {
        // setup
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY))
        // execute
        fixture.update(wiFiData)
        // validate
        assertThat(mainActivity.findViewById<View>(R.id.connection).visibility).isEqualTo(View.GONE)
        verifyConnectionInformation()
        verify(warningView).update(wiFiData)
    }

    @Test
    fun connectionGoneWithConnectionInformationAndHideType() {
        // setup
        val connection = withConnection(withWiFiAdditional())
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.HIDE)
        withConnectionInformation(connection)
        withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        // execute
        fixture.update(wiFiData)
        // validate
        assertThat(mainActivity.findViewById<View>(R.id.connection).visibility).isEqualTo(View.GONE)
        verifyConnectionInformation()
        verify(warningView).update(wiFiData)
    }

    @Test
    fun connectionVisibleWithConnectionInformation() {
        // setup
        val connection = withConnection(withWiFiAdditional())
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(connection)
        withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        // execute
        fixture.update(wiFiData)
        // validate
        assertThat(mainActivity.findViewById<View>(R.id.connection).visibility).isEqualTo(View.VISIBLE)
        verifyConnectionInformation()
        verify(warningView).update(wiFiData)
    }

    @Test
    fun connectionWithConnectionInformation() {
        // setup
        val wiFiAdditional = withWiFiAdditional()
        val connection = withConnection(wiFiAdditional)
        val expectedText = mainActivity.getString(R.string.current_connection)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(connection)
        withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        // execute
        fixture.update(wiFiData)
        // validate
        val wiFiConnection = wiFiAdditional.wiFiConnection
        val view = mainActivity.findViewById<View>(R.id.connection)
        val ipAddressView = view.findViewById<TextView>(R.id.ipAddress)
        assertThat(ipAddressView.text.toString()).isEqualTo(wiFiConnection.ipAddress)
        val linkSpeedView = view.findViewById<TextView>(R.id.linkSpeed)
        assertThat(linkSpeedView.visibility).isEqualTo(View.VISIBLE)
        assertThat(linkSpeedView.text.toString()).isEqualTo(wiFiConnection.linkSpeed.toString() + WifiInfo.LINK_SPEED_UNITS)
        assertThat(view.findViewById<TextView>(R.id.currentConnection).text.toString()).isEqualTo(expectedText)
        verify(warningView).update(wiFiData)
    }

    @Test
    fun connectionWithInvalidLinkSpeed() {
        // setup
        val wiFiIdentifier = WiFiIdentifier(ssid, bssid)
        val wiFiConnection = WiFiConnection(wiFiIdentifier, ipAddress, WiFiConnection.LINK_SPEED_INVALID)
        val connection = withConnection(WiFiAdditional(String.EMPTY, wiFiConnection))
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(connection)
        withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        // execute
        fixture.update(wiFiData)
        // validate
        val view = mainActivity.findViewById<View>(R.id.connection)
        val linkSpeedView = view.findViewById<TextView>(R.id.linkSpeed)
        assertThat(linkSpeedView.visibility).isEqualTo(View.GONE)
        verify(warningView).update(wiFiData)
    }

    @Test
    fun viewCompactAddsPopup() {
        // setup
        val connection = withConnection(withWiFiAdditional())
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPACT)
        withConnectionInformation(connection)
        val view = withAccessPointDetailView(connection, ConnectionViewType.COMPACT.layout)
        // execute
        fixture.update(wiFiData)
        // validate
        verify(accessPointPopup).attach(view.findViewById(R.id.attachPopup), connection)
        verify(accessPointPopup).attach(view.findViewById(R.id.ssid), connection)
        verify(warningView).update(wiFiData)
    }

    @Test
    fun wiFiSupportIsGoneWhenWiFiBandIsAvailable() {
        // setup
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY))
        // execute
        fixture.update(wiFiData)
        // validate
        assertThat(mainActivity.findViewById<View>(R.id.main_wifi_support).visibility).isEqualTo(View.GONE)
        verify(settings).wiFiBand()
        verify(warningView).update(wiFiData)
    }

    @Test
    fun wiFiSupportIsVisibleWhenWiFiBandIsNotAvailable() {
        // setup
        val expectedText = mainActivity.resources.getString(WiFiBand.GHZ6.textResource)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ6)
        whenever(wiFiManagerWrapper.is6GHzBandSupported()).thenReturn(false)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY))
        // execute
        fixture.update(wiFiData)
        // validate
        val textView = mainActivity.findViewById<TextView>(R.id.main_wifi_support)
        assertThat(textView.visibility).isEqualTo(View.VISIBLE)
        assertThat(textView.text).isEqualTo(expectedText)
        verify(settings).wiFiBand()
        verify(wiFiManagerWrapper).is6GHzBandSupported()
        verify(warningView).update(wiFiData)
    }

    private fun withConnection(wiFiAdditional: WiFiAdditional): WiFiDetail =
        WiFiDetail(
            WiFiIdentifier(ssid, bssid),
            WiFiSecurity.EMPTY,
            WiFiSignal(2435, 2435, WiFiWidth.MHZ_20, -55),
            wiFiAdditional
        )

    private fun withWiFiAdditional(): WiFiAdditional =
        WiFiAdditional(wiFiConnection = WiFiConnection(WiFiIdentifier(ssid, bssid), ipAddress, 11))

    private fun withAccessPointDetailView(connection: WiFiDetail, @LayoutRes layout: Int): View {
        val parent = mainActivity.findViewById<View>(R.id.connection).findViewById<ViewGroup>(R.id.connectionDetail)
        val view = mainActivity.layoutInflater.inflate(layout, parent, false)
        whenever(accessPointDetail.makeView(null, parent, connection, layout = layout)).thenReturn(view)
        whenever(accessPointDetail.makeView(parent.getChildAt(0), parent, connection, layout = layout)).thenReturn(view)
        return view
    }

    private fun withConnectionInformation(connection: WiFiDetail) {
        whenever(wiFiData.connection()).thenReturn(connection)
    }

    private fun verifyConnectionInformation() {
        verify(wiFiData).connection()
    }
}
