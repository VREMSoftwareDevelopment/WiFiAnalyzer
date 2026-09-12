/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.detailview.WiFiDetailPopup
import com.vrem.wifianalyzer.wifi.detailview.WiFiDetailView
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional
import com.vrem.wifianalyzer.wifi.model.WiFiConnection
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import com.vrem.wifianalyzer.wifi.model.WiFiSecurity
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import com.vrem.wifianalyzer.wifi.model.WiFiWidth
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class ConnectionViewTest {
    private val ssid = "SSID"
    private val bssid = "BSSID"
    private val ipAddress = "IP-ADDRESS"
    private val mainActivity = RobolectricUtil.INSTANCE.mainActivity
    private val settings: Settings = mock()
    private val wiFiManagerWrapper = MainContextHelper.INSTANCE.wiFiManagerWrapper
    private val wiFiData: WiFiData = mock()
    private val wiFiDetailView: WiFiDetailView = mock()
    private val wiFiDetailPopup: WiFiDetailPopup = mock()
    private val warningView: WarningView = mock()
    private val fixture = ConnectionView(mainActivity, wiFiDetailView, wiFiDetailPopup, warningView, settings)
    private lateinit var detailParent: ViewGroup
    private var detailConvertView: View? = null

    @After
    fun tearDown() {
        verifyNoMoreInteractions(warningView, settings, wiFiManagerWrapper, wiFiData, wiFiDetailView, wiFiDetailPopup)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun connectionGoneWithNoConnectionInformation() {
        // Arrange
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY))
        // Act
        fixture.update(wiFiData)
        // Assert
        assertThat(mainActivity.findViewById<View>(R.id.connection).visibility).isEqualTo(View.GONE)
        verifyUpdate()
    }

    @Test
    fun connectionGoneWithConnectionInformationAndHideType() {
        // Arrange
        val connection = withConnection(withWiFiAdditional())
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.HIDE)
        withConnectionInformation(connection)
        val view = withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        // Act
        fixture.update(wiFiData)
        // Assert
        assertThat(mainActivity.findViewById<View>(R.id.connection).visibility).isEqualTo(View.GONE)
        verifyUpdate()
    }

    @Test
    fun connectionVisibleWithConnectionInformation() {
        // Arrange
        val connection = withConnection(withWiFiAdditional())
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(connection)
        val view = withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        // Act
        fixture.update(wiFiData)
        // Assert
        assertThat(mainActivity.findViewById<View>(R.id.connection).visibility).isEqualTo(View.VISIBLE)
        verify(wiFiDetailPopup).attachToRow(view, connection)
        verifyAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        verifyUpdate()
    }

    @Test
    fun connectionWithConnectionInformation() {
        // Arrange
        val wiFiAdditional = withWiFiAdditional()
        val connection = withConnection(wiFiAdditional)
        val expectedText = mainActivity.getString(R.string.current_connection)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(connection)
        val detailView = withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        // Act
        fixture.update(wiFiData)
        // Assert
        val wiFiConnection = wiFiAdditional.wiFiConnection
        val view = mainActivity.findViewById<View>(R.id.connection)
        val ipAddressView = view.findViewById<TextView>(R.id.ipAddress)
        assertThat(ipAddressView.text.toString()).isEqualTo(wiFiConnection.ipAddress)
        val linkSpeedView = view.findViewById<TextView>(R.id.linkSpeed)
        assertThat(linkSpeedView.visibility).isEqualTo(View.VISIBLE)
        assertThat(linkSpeedView.text.toString()).isEqualTo(
            wiFiConnection.linkSpeed.toString() + WifiInfo.LINK_SPEED_UNITS,
        )
        assertThat(view.findViewById<TextView>(R.id.currentConnection).text.toString()).isEqualTo(expectedText)
        verify(wiFiDetailPopup).attachToRow(detailView, connection)
        verifyAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        verifyUpdate()
    }

    @Test
    fun connectionWithInvalidLinkSpeed() {
        // Arrange
        val wiFiIdentifier = WiFiIdentifier(ssid, bssid)
        val wiFiConnection = WiFiConnection(wiFiIdentifier, ipAddress, WiFiConnection.LINK_SPEED_INVALID)
        val connection = withConnection(WiFiAdditional(String.EMPTY, wiFiConnection))
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(connection)
        val detailView = withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        // Act
        fixture.update(wiFiData)
        // Assert
        val view = mainActivity.findViewById<View>(R.id.connection)
        val linkSpeedView = view.findViewById<TextView>(R.id.linkSpeed)
        assertThat(linkSpeedView.visibility).isEqualTo(View.GONE)
        verify(wiFiDetailPopup).attachToRow(detailView, connection)
        verifyAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        verifyUpdate()
    }

    @Test
    fun viewCompactAddsPopup() {
        // Arrange
        val connection = withConnection(withWiFiAdditional())
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPACT)
        withConnectionInformation(connection)
        val view = withAccessPointDetailView(connection, ConnectionViewType.COMPACT.layout)
        // Act
        fixture.update(wiFiData)
        // Assert
        verify(wiFiDetailPopup).attachToRow(view, connection)
        verifyAccessPointDetailView(connection, ConnectionViewType.COMPACT.layout)
        verifyUpdate()
    }

    @Test
    fun wiFiSupportIsGoneWhenWiFiBandIsAvailable() {
        // Arrange
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY))
        // Act
        fixture.update(wiFiData)
        // Assert
        assertThat(mainActivity.findViewById<View>(R.id.main_wifi_support).visibility).isEqualTo(View.GONE)
        verifyUpdate()
    }

    @Test
    fun wiFiSupportIsVisibleWhenWiFiBandIsNotAvailable() {
        // Arrange
        val expectedText = mainActivity.getString(WiFiBand.GHZ6.textResource)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ6)
        whenever(wiFiManagerWrapper.is6GHzBandSupported()).thenReturn(false)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY))
        // Act
        fixture.update(wiFiData)
        // Assert
        val textView = mainActivity.findViewById<TextView>(R.id.main_wifi_support)
        assertThat(textView.visibility).isEqualTo(View.VISIBLE)
        assertThat(textView.text).isEqualTo(expectedText)
        verify(wiFiManagerWrapper).is6GHzBandSupported()
        verifyUpdate()
    }

    private fun withConnection(wiFiAdditional: WiFiAdditional): WiFiDetail =
        WiFiDetail(
            WiFiIdentifier(ssid, bssid),
            WiFiSecurity.EMPTY,
            WiFiSignal(2435, 2435, WiFiWidth.MHZ_20, -55),
            wiFiAdditional,
        )

    private fun withWiFiAdditional(): WiFiAdditional =
        WiFiAdditional(wiFiConnection = WiFiConnection(WiFiIdentifier(ssid, bssid), ipAddress, 11))

    private fun withAccessPointDetailView(
        connection: WiFiDetail,
        @LayoutRes layout: Int,
    ): View {
        detailParent = mainActivity.findViewById<View>(R.id.connection).findViewById(R.id.connectionDetail)
        detailConvertView = detailParent.getChildAt(0)
        val view = mainActivity.layoutInflater.inflate(layout, detailParent, false)
        whenever(wiFiDetailView.makeView(detailConvertView, detailParent, connection, layout = layout)).thenReturn(view)
        return view
    }

    private fun verifyAccessPointDetailView(
        connection: WiFiDetail,
        @LayoutRes layout: Int,
    ) {
        verify(wiFiDetailView).makeView(detailConvertView, detailParent, connection, layout = layout)
    }

    private fun withConnectionInformation(connection: WiFiDetail) {
        whenever(wiFiData.connection()).thenReturn(connection)
    }

    private fun verifyUpdate() {
        verify(wiFiData).connection()
        verify(settings).connectionViewType()
        verify(settings).wiFiBand()
        verify(warningView).update(wiFiData)
    }
}
