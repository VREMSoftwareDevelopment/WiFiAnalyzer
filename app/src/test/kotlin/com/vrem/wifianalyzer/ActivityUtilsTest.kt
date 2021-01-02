/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer

import android.content.Intent
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.navigation.NavigationMenu
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class ActivityUtilsTest {
    private val window: Window = mock()
    private val actionBar: ActionBar = mock()
    private val toolbar: Toolbar = mock()
    private val intent: Intent = mock()
    private val mainActivity = MainContextHelper.INSTANCE.mainActivity
    private val settings = MainContextHelper.INSTANCE.settings

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(toolbar)
        verifyNoMoreInteractions(actionBar)
        verifyNoMoreInteractions(window)
        verifyNoMoreInteractions(settings)
        verifyNoMoreInteractions(intent)
    }

    @Test
    fun testSetupToolbar() {
        // setup
        whenever<Any>(mainActivity.findViewById(R.id.toolbar)).thenReturn(toolbar)
        whenever(mainActivity.supportActionBar).thenReturn(actionBar)
        // execute
        val actual = mainActivity.setupToolbar()
        // validate
        assertEquals(toolbar, actual)
        verify(mainActivity).findViewById<View>(R.id.toolbar)
        verify(mainActivity).supportActionBar
        verify(toolbar).setOnClickListener(any())
        verify(mainActivity).setSupportActionBar(toolbar)
        verify(actionBar).setHomeButtonEnabled(true)
        verify(actionBar).setDisplayHomeAsUpEnabled(true)
    }

    @Test
    fun testWiFiBandToggleOnClickToggles() {
        // setup
        whenever(mainActivity.currentNavigationMenu()).thenReturn(NavigationMenu.CHANNEL_GRAPH)
        // execute
        mainActivity.toggleWiFiBand()
        // validate
        verify(settings).toggleWiFiBand()
        verify(mainActivity).currentNavigationMenu()
    }

    @Test
    fun testWiFiBandToggleOnClickDoesNotToggles() {
        // setup
        whenever(mainActivity.currentNavigationMenu()).thenReturn(NavigationMenu.ACCESS_POINTS)
        // execute
        mainActivity.toggleWiFiBand()
        // validate
        verify(settings, never()).toggleWiFiBand()
        verify(mainActivity).currentNavigationMenu()
    }

    @Test
    fun testKeepScreenOnSwitchOn() {
        // setup
        whenever(settings.keepScreenOn()).thenReturn(true)
        whenever(mainActivity.window).thenReturn(window)
        // execute
        mainActivity.keepScreenOn()
        // validate
        verify(settings).keepScreenOn()
        verify(mainActivity).window
        verify(window).addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    @Test
    fun testKeepScreenOnSwitchOff() {
        // setup
        whenever(settings.keepScreenOn()).thenReturn(false)
        whenever(mainActivity.window).thenReturn(window)
        // execute
        mainActivity.keepScreenOn()
        // validate
        verify(settings).keepScreenOn()
        verify(mainActivity).window
        verify(window).clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    @Test
    fun testStartWiFiSettings() {
        // execute
        mainActivity.startWiFiSettings()
        // validate
        verify(mainActivity).startActivityForResult(any(), eq(0))
    }

    @Test
    fun testStartLocationSettings() {
        // execute
        mainActivity.startLocationSettings()
        // validate
        verify(mainActivity).startActivity(any())
    }
}