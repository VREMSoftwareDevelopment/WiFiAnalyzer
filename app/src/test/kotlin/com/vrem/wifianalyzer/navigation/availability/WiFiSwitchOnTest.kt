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
package com.vrem.wifianalyzer.navigation.availability

import android.os.Build
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.util.fromHtml
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.navigation.options.OptionMenu
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
@LooperMode(LooperMode.Mode.PAUSED)
class WiFiSwitchOnTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val optionMenu = mock(OptionMenu::class.java)
    private val menu = mock(Menu::class.java)
    private val menuItem = mock(MenuItem::class.java)
    private val actionBar = mock(ActionBar::class.java)
    private val fixture = WiFiSwitchOn()

    @Test
    fun testApplySetSubtitle() {
        // setup
        val expected = withExpectedSubtitle()
        // execute
        fixture.apply(mainActivity)
        // validate
        val actual = mainActivity.supportActionBar!!.subtitle
        assertTrue(expected.isNotEmpty())
        assertEquals(expected.length.toLong(), actual!!.length.toLong())
        for (i in expected.indices) {
            assertEquals("" + i + ":" + expected[i] + ":" + actual[i], expected[i].toLong(), actual[i].toLong())
        }
    }

    private fun withExpectedSubtitle(): CharSequence {
        val wiFiBand = MainContext.INSTANCE.settings.wiFiBand()
        val resources = mainActivity.resources
        val wiFiBand2 = resources.getString(WiFiBand.GHZ2.textResource)
        val wiFiBand5 = resources.getString(WiFiBand.GHZ5.textResource)
        val colorSelected = ContextCompat.getColor(mainActivity, R.color.selected)
        val colorNotSelected = ContextCompat.getColor(mainActivity, R.color.regular)
        val subtitle = fixture.makeSubtitle(WiFiBand.GHZ2 == wiFiBand, wiFiBand2, wiFiBand5, colorSelected, colorNotSelected)
        return fromHtml(subtitle)
    }

    @Test
    fun testApplyWithNoActionBarDoesNotSetSubtitle() {
        // setup
        val mainActivity = mock(MainActivity::class.java)
        whenever(mainActivity.supportActionBar).thenReturn(null)
        // execute
        fixture.apply(mainActivity)
        // validate
        verify(mainActivity).supportActionBar
        verify(actionBar, never()).subtitle = ArgumentMatchers.anyString()
    }

    @Test
    fun testMakeSubtitleGHZ2() {
        // setup
        val color1 = 10
        val color2 = 20
        val text1 = "text1"
        val text2 = "text2"
        val expected = ("<font color='" + color1 + "'><strong>" + text1
                + "</strong></font>" + WiFiSwitchOn.SPACER
                + "<font color='" + color2 + "'><small>" + text2 + "</small></font>")
        // execute
        val actual = fixture.makeSubtitle(true, text1, text2, color1, color2)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testMakeSubtitleGHZ5() {
        // setup
        val color1 = 10
        val color2 = 20
        val text1 = "text1"
        val text2 = "text2"
        val expected = ("<font color='" + color2 + "'><small>" + text1
                + "</small></font>" + WiFiSwitchOn.SPACER
                + "<font color='" + color1 + "'><strong>" + text2 + "</strong></font>")
        // execute
        val actual = fixture.makeSubtitle(false, text1, text2, color1, color2)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testApplyWithWiFiBandVisible() {
        // execute
        fixture.apply(mainActivity)
        // validate
        mainActivity.optionMenu.menu?.findItem(R.id.action_wifi_band)?.isVisible?.let { assertTrue(it) }
    }

    @Test
    fun testApplyWithNoOptionMenuDoesNotSetWiFiBandVisible() {
        // setup
        val mainActivity = mock(MainActivity::class.java)
        whenever(mainActivity.optionMenu).thenReturn(null)
        // execute
        fixture.apply(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verifyMenu()
    }

    @Test
    fun testApplyWithNoMenuDoesNotSetWiFiBandVisible() {
        // setup
        val mainActivity = mock(MainActivity::class.java)
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(null)
        // execute
        fixture.apply(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verifyMenu()
    }

    private fun verifyMenu() {
        verify(menu, never()).findItem(R.id.action_wifi_band)
        verify(menuItem, never()).isVisible = ArgumentMatchers.anyBoolean()
    }
}