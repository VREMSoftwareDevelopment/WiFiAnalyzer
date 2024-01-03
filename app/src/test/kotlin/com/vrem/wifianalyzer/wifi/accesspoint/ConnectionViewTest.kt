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

import android.net.wifi.WifiInfo
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.model.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
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
    fun testConnectionGoneWithNoConnectionInformation() {
        // setup
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY))
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(View.GONE, mainActivity.findViewById<View>(R.id.connection).visibility)
        verifyConnectionInformation()
        verify(warningView).update(wiFiData)
    }

    @Test
    fun testConnectionGoneWithConnectionInformationAndHideType() {
        // setup
        val connection = withConnection(withWiFiAdditional())
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.HIDE)
        withConnectionInformation(connection)
        withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(View.GONE, mainActivity.findViewById<View>(R.id.connection).visibility)
        verifyConnectionInformation()
        verify(warningView).update(wiFiData)
    }

    @Test
    fun testConnectionVisibleWithConnectionInformation() {
        // setup
        val connection = withConnection(withWiFiAdditional())
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(connection)
        withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(View.VISIBLE, mainActivity.findViewById<View>(R.id.connection).visibility)
        verifyConnectionInformation()
        verify(warningView).update(wiFiData)
    }

    @Test
    fun testConnectionWithConnectionInformation() {
        // setup
        val wiFiAdditional = withWiFiAdditional()
        val connection = withConnection(wiFiAdditional)
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
        assertEquals(wiFiConnection.ipAddress, ipAddressView.text.toString())
        val linkSpeedView = view.findViewById<TextView>(R.id.linkSpeed)
        assertEquals(View.VISIBLE, linkSpeedView.visibility)
        assertEquals(wiFiConnection.linkSpeed.toString() + WifiInfo.LINK_SPEED_UNITS, linkSpeedView.text.toString())
        verify(warningView).update(wiFiData)
    }

    @Test
    fun testConnectionWithInvalidLinkSpeed() {
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
        assertEquals(View.GONE, linkSpeedView.visibility)
        verify(warningView).update(wiFiData)
    }

    @Test
    fun testViewCompactAddsPopup() {
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
    fun testWiFiSupportIsGoneWhenWiFiBandIsAvailable() {
        // setup
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY))
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(View.GONE, mainActivity.findViewById<View>(R.id.main_wifi_support).visibility)
        verify(settings).wiFiBand()
        verify(warningView).update(wiFiData)
    }

    @Test
    fun testWiFiSupportIsVisibleWhenWiFiBandIsNotAvailable() {
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
        assertEquals(View.VISIBLE, textView.visibility)
        assertEquals(expectedText, textView.text)
        verify(settings).wiFiBand()
        verify(wiFiManagerWrapper).is6GHzBandSupported()
        verify(warningView).update(wiFiData)
    }

    @Test
    fun testWiFiThrottlingIsGoneWhenWiFiThrottlingIsDisabled() {
        // setup
        whenever(wiFiManagerWrapper.isScanThrottleEnabled()).thenReturn(false)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY))
        // execute
        fixture.update(wiFiData)
        // validate
        val textView = mainActivity.findViewById<TextView>(R.id.main_wifi_throttling)
        assertEquals(View.GONE, textView.visibility)
        verify(wiFiManagerWrapper).isScanThrottleEnabled()
        verify(settings).wiFiBand()
        verify(warningView).update(wiFiData)
    }

    @Test
    fun testWiFiThrottlingIsVisibleWhenWiFiThrottlingIsEnabled() {
        // setup
        whenever(wiFiManagerWrapper.isScanThrottleEnabled()).thenReturn(true)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY))
        // execute
        fixture.update(wiFiData)
        // validate
        val textView = mainActivity.findViewById<TextView>(R.id.main_wifi_throttling)
        assertEquals(View.VISIBLE, textView.visibility)
        verify(wiFiManagerWrapper).isScanThrottleEnabled()
        verify(settings).wiFiBand()
        verify(warningView).update(wiFiData)
    }

    private fun withConnection(wiFiAdditional: WiFiAdditional): WiFiDetail =
        WiFiDetail(
            WiFiIdentifier(ssid, bssid),
            WiFiSecurity.EMPTY,
            WiFiSignal(2435, 2435, WiFiWidth.MHZ_20, -55, true),
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