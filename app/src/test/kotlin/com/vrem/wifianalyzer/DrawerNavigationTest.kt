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
package com.vrem.wifianalyzer

import android.content.res.Configuration
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class DrawerNavigationTest {
    private val mainActivity: MainActivity = mock()
    private val toolbar: Toolbar = mock()
    private val configuration: Configuration = mock()
    private val drawerLayout: DrawerLayout = mock()
    private val actionBarDrawerToggle: ActionBarDrawerToggle = mock()
    private val fixture = spy(DrawerNavigation(mainActivity, toolbar))

    @Before
    fun setUp() {
        doReturn(actionBarDrawerToggle).whenever(fixture).createDrawerToggle(drawerLayout)
        whenever<Any>(mainActivity.findViewById(R.id.drawer_layout)).thenReturn(drawerLayout)
        fixture.create()
    }

    @After
    fun tearDown() {
        verify(mainActivity).findViewById<View>(R.id.drawer_layout)
        verify(fixture).createDrawerToggle(drawerLayout)
        verify(drawerLayout).addDrawerListener(actionBarDrawerToggle)
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(toolbar)
        verifyNoMoreInteractions(configuration)
        verifyNoMoreInteractions(drawerLayout)
        verifyNoMoreInteractions(actionBarDrawerToggle)
    }

    @Test
    fun create() {
        // validate
        verify(actionBarDrawerToggle).syncState()
    }

    @Test
    fun syncState() {
        // execute
        fixture.syncState()
        // validate
        verify(actionBarDrawerToggle, times(2)).syncState()
    }

    @Test
    fun onConfigurationChanged() {
        // execute
        fixture.onConfigurationChanged(configuration)
        // validate
        verify(actionBarDrawerToggle).onConfigurationChanged(configuration)
        verify(actionBarDrawerToggle).syncState()
    }
}