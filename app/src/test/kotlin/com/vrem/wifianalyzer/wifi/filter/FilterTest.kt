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
package com.vrem.wifianalyzer.wifi.filter

import android.content.DialogInterface
import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.filter.Filter.Companion.build
import com.vrem.wifianalyzer.wifi.filter.adapter.FiltersAdapter
import com.vrem.wifianalyzer.wifi.model.Security
import com.vrem.wifianalyzer.wifi.model.Strength
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.CINNAMON_BUN])
class FilterTest {
    private val mainActivity = RobolectricUtil.INSTANCE.mainActivity
    private val fixture = build()

    @Before
    fun setUp() {
        RobolectricUtil.INSTANCE.clearLooper()
    }

    @After
    fun tearDown() {
        mainActivity.currentNavigationMenu(NavigationMenu.ACCESS_POINTS)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun alertDialog() {
        // Act
        val actual = fixture.alertDialog!!
        // Assert
        assertThat(actual.isShowing).isFalse
    }

    @Test
    fun show() {
        // Act
        fixture.show()
        // Assert
        assertThat(fixture.alertDialog!!.isShowing).isTrue
    }

    @Test
    fun title() {
        // Arrange
        val expected = mainActivity.getString(R.string.filter_title)
        val shadowAlertDialog = Shadows.shadowOf(fixture.alertDialog!!)
        // Act
        val actual = shadowAlertDialog.title
        // Assert
        assertThat(actual.toString()).isEqualTo(expected)
    }

    @Test
    fun positiveButton() {
        // Arrange
        val filtersAdapter = withFiltersAdapter()
        val mainActivity: MainActivity = mock()
        val fixture = build(filtersAdapter = filtersAdapter, mainActivity = mainActivity)
        fixture.show()
        val button = fixture.alertDialog!!.getButton(DialogInterface.BUTTON_POSITIVE)
        // Act
        button.performClick()
        // Assert
        RobolectricUtil.INSTANCE.clearLooper()
        assertThat(fixture.alertDialog.isShowing).isFalse
        verify(filtersAdapter).save()
        verify(mainActivity).update()
    }

    @Test
    fun negativeButton() {
        // Arrange
        val filtersAdapter = withFiltersAdapter()
        val mainActivity: MainActivity = mock()
        val fixture = build(filtersAdapter = filtersAdapter, mainActivity = mainActivity)
        fixture.show()
        val button = fixture.alertDialog!!.getButton(DialogInterface.BUTTON_NEGATIVE)
        // Act
        button.performClick()
        // Assert
        RobolectricUtil.INSTANCE.clearLooper()
        assertThat(fixture.alertDialog.isShowing).isFalse
        verify(filtersAdapter).reset()
        verify(mainActivity).update()
    }

    @Test
    fun neutralButton() {
        // Arrange
        val filtersAdapter = withFiltersAdapter()
        val mainActivity: MainActivity = mock()
        val fixture = build(filtersAdapter = filtersAdapter, mainActivity = mainActivity)
        fixture.show()
        val button = fixture.alertDialog!!.getButton(DialogInterface.BUTTON_NEUTRAL)
        // Act
        button.performClick()
        // Assert
        RobolectricUtil.INSTANCE.clearLooper()
        assertThat(fixture.alertDialog.isShowing).isFalse
        verify(filtersAdapter).reload()
        verify(mainActivity, never()).update()
    }

    @Test
    fun sSIDFilterViewIsVisible() {
        // Arrange
        fixture.show()
        // Act
        val actual = fixture.alertDialog!!.findViewById<View>(R.id.filterSSID).visibility
        // Assert
        assertThat(actual).isEqualTo(View.VISIBLE)
    }

    @Test
    fun wiFiBandFilterViewIsVisible() {
        // Arrange
        fixture.show()
        // Act
        val actual = fixture.alertDialog!!.findViewById<View>(R.id.filterWiFiBand).visibility
        // Assert
        assertThat(actual).isEqualTo(View.VISIBLE)
    }

    @Test
    fun wiFiBandFilterMapping() {
        // Arrange
        val expected = WiFiBand.entries.toSet()
        fixture.show()
        // Act
        val actual: Map<WiFiBand, Int> = fixture.wiFiBandFilter!!.ids
        // Assert
        assertThat(actual).hasSize(expected.size)
        expected.forEach { assertThat(actual[it]).isNotNull() }
    }

    @Test
    fun securityFilterViewIsVisible() {
        // Arrange
        fixture.show()
        // Act
        val actual = fixture.alertDialog!!.findViewById<View>(R.id.filterSecurity).visibility
        // Assert
        assertThat(actual).isEqualTo(View.VISIBLE)
    }

    @Test
    fun securityFilterMapping() {
        // Arrange
        val expected = Security.entries.toSet()
        fixture.show()
        // Act
        val actual: Map<Security, Int> = fixture.securityFilter!!.ids
        // Assert
        assertThat(actual).hasSize(expected.size)
        expected.forEach { assertThat(actual[it]).isNotNull() }
    }

    @Test
    fun strengthFilterViewIsVisible() {
        // Arrange
        fixture.show()
        // Act
        val actual = fixture.alertDialog!!.findViewById<View>(R.id.filterStrength).visibility
        // Assert
        assertThat(actual).isEqualTo(View.VISIBLE)
    }

    @Test
    fun strengthFilterMapping() {
        // Arrange
        val expected = Strength.entries.toSet()
        fixture.show()
        // Act
        val actual: Map<Strength, Int> = fixture.strengthFilter!!.ids
        // Assert
        assertThat(actual).hasSize(expected.size)
        expected.forEach { assertThat(actual[it]).isNotNull() }
    }

    @Test
    fun showWhenDialogIsNull() {
        // Arrange
        val fixture = Filter(null)
        // Act
        fixture.show()
        // Assert
        assertThat(fixture.wiFiBandFilter).isNull()
        assertThat(fixture.strengthFilter).isNull()
        assertThat(fixture.securityFilter).isNull()
    }

    @Test
    fun wiFiBandFilterViewIsGone() {
        // Arrange
        mainActivity.currentNavigationMenu(NavigationMenu.CHANNEL_RATING)
        fixture.show()
        // Act
        val actual = fixture.alertDialog!!.findViewById<View>(R.id.filterWiFiBand).visibility
        // Assert
        assertThat(actual).isEqualTo(View.GONE)
    }

    @Test
    fun showWhenAlreadyShowing() {
        // Arrange
        fixture.show()
        // Act
        fixture.show()
        // Assert
        assertThat(fixture.alertDialog!!.isShowing).isTrue
    }

    @Test
    fun buildReturnsNullDialogWhenActivityIsFinishing() {
        // Arrange
        val mainActivity = MainContextHelper.INSTANCE.mainActivity
        doReturn(true).whenever(mainActivity).isFinishing
        // Act
        val actual = build()
        // Assert
        assertThat(actual.alertDialog).isNull()
        verify(mainActivity).isFinishing
    }

    @Test
    fun buildUsesInjectedMainActivityRatherThanMainContext() {
        // Arrange
        val injected: MainActivity = mock()
        doReturn(true).whenever(injected).isFinishing
        // Act
        val actual = build(mainActivity = injected)
        // Assert
        assertThat(actual.alertDialog).isNull()
        verify(injected).isFinishing
    }

    private fun withFiltersAdapter(): FiltersAdapter {
        val filtersAdapter = spy(MainContext.INSTANCE.filtersAdapter)
        doNothing().whenever(filtersAdapter).save()
        doNothing().whenever(filtersAdapter).reset()
        doNothing().whenever(filtersAdapter).reload()
        return filtersAdapter
    }
}
