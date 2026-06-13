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
package com.vrem.wifianalyzer.wifi.detailview

import android.content.DialogInterface
import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import com.vrem.wifianalyzer.wifi.model.WiFiSecurity
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import com.vrem.wifianalyzer.wifi.model.WiFiWidth
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class WiFiDetailPopupTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val fixture = WiFiDetailPopup()

    @Test
    fun showOpensPopup() {
        // Arrange
        val view = mainActivity.layoutInflater.inflate(R.layout.wifi_detail_view_popup, null)
        // Act
        val actual = fixture.show(view)
        // Assert
        assertThat(actual).isNotNull()
        assertThat(actual.isShowing).isTrue
    }

    @Test
    fun popupIsClosedOnPositiveButtonClick() {
        // Arrange
        val view = mainActivity.layoutInflater.inflate(R.layout.wifi_detail_view_popup, null)
        val alertDialog = fixture.show(view)
        val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        // Act
        button.performClick()
        // Assert
        RobolectricUtil.INSTANCE.clearLooper()
        assertThat(alertDialog.isShowing).isFalse
    }

    @Test
    fun popupPositiveButtonIsNotVisible() {
        // Arrange
        val view = mainActivity.layoutInflater.inflate(R.layout.wifi_detail_view_popup, null)
        val alertDialog = fixture.show(view)
        // Act
        val actual = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        // Assert
        assertThat(actual.visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun popupNegativeButtonIsNotVisible() {
        // Arrange
        val view = mainActivity.layoutInflater.inflate(R.layout.wifi_detail_view_popup, null)
        val alertDialog = fixture.show(view)
        // Act
        val actual = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        // Assert
        assertThat(actual.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun popupNeutralButtonIsNotVisible() {
        // Arrange
        val view = mainActivity.layoutInflater.inflate(R.layout.wifi_detail_view_popup, null)
        val alertDialog = fixture.show(view)
        // Act
        val actual = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL)
        // Assert
        assertThat(actual.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun showSequenceSingleViewDelegatesToShow() {
        // Arrange
        val view = mainActivity.layoutInflater.inflate(R.layout.wifi_detail_view_popup, null)
        // Act
        val actual = fixture.showSequence(listOf(view))
        // Assert
        assertThat(actual).isNotNull()
        assertThat(actual.isShowing).isTrue
        assertThat(actual.getButton(DialogInterface.BUTTON_POSITIVE).visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun showSequenceMultipleViewsShowsNextAndClose() {
        // Arrange
        val view1 = mainActivity.layoutInflater.inflate(R.layout.wifi_detail_view_popup, null)
        val view2 = mainActivity.layoutInflater.inflate(R.layout.wifi_detail_view_popup, null)
        // Act
        val actual = fixture.showSequence(listOf(view1, view2))
        // Assert
        assertThat(actual).isNotNull()
        assertThat(actual.isShowing).isTrue
        assertThat(actual.getButton(DialogInterface.BUTTON_POSITIVE).visibility).isEqualTo(View.VISIBLE)
        assertThat(actual.getButton(DialogInterface.BUTTON_NEGATIVE).visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun showSequenceNextButtonAdvancesToNextPopup() {
        // Arrange
        val view1 = mainActivity.layoutInflater.inflate(R.layout.wifi_detail_view_popup, null)
        val view2 = mainActivity.layoutInflater.inflate(R.layout.wifi_detail_view_popup, null)
        val firstDialog = fixture.showSequence(listOf(view1, view2))
        // Act
        firstDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        RobolectricUtil.INSTANCE.clearLooper()
        // Assert
        assertThat(firstDialog.isShowing).isFalse
    }

    @Test
    fun showSequenceLastPopupHasCloseOnly() {
        // Arrange
        val view1 = mainActivity.layoutInflater.inflate(R.layout.wifi_detail_view_popup, null)
        val view2 = mainActivity.layoutInflater.inflate(R.layout.wifi_detail_view_popup, null)
        val firstDialog = fixture.showSequence(listOf(view1, view2))
        firstDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        RobolectricUtil.INSTANCE.clearLooper()
        // Act
        val actual = ShadowAlertDialog.getLatestAlertDialog()
        // Assert
        assertThat(actual).isNotNull()
        assertThat(actual!!.isShowing).isTrue
        assertThat(actual.getButton(DialogInterface.BUTTON_POSITIVE).visibility).isEqualTo(View.GONE)
        assertThat(actual.getButton(DialogInterface.BUTTON_NEGATIVE).visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun attach() {
        // Arrange
        val wiFiDetail = withWiFiDetail()
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_compact, null)
        // Act
        fixture.attach(view, wiFiDetail)
        // Assert
        assertThat(view.performClick()).isTrue
    }

    @Test
    fun attachToRowAttachesListenerToAttachPopupAndSsid() {
        // Arrange
        val wiFiDetail = withWiFiDetail()
        val row = mainActivity.layoutInflater.inflate(R.layout.access_point_view_compact, null)
        // Act
        fixture.attachToRow(row, wiFiDetail)
        // Assert
        assertThat(row.findViewById<View>(R.id.attachPopup).performClick()).isTrue
        assertThat(row.findViewById<View>(R.id.ssid).performClick()).isTrue
    }

    private fun withWiFiDetail(): WiFiDetail =
        WiFiDetail(
            WiFiIdentifier("SSID", "BSSID"),
            WiFiSecurity("capabilities"),
            WiFiSignal(1, 1, WiFiWidth.MHZ_40, 2),
        )
}
