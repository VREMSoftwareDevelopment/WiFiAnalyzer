/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2022 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.navigation.items

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.MenuItem
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.navigation.NavigationMenu
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.S])
class PortAuthorityItemTest {
    private val portAuthority = "com.aaronjwood.portauthority"
    private val portAuthorityFree = "$portAuthority.free"
    private val portAuthorityDonate = "$portAuthority.donate"

    private val mainActivity: MainActivity = mock()
    private val context: Context = mock()
    private val intent: Intent = mock()
    private val menuItem: MenuItem = mock()
    private val packageManager: PackageManager = mock()
    private val fixture: PortAuthorityItem = spy(PortAuthorityItem())

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(menuItem)
        verifyNoMoreInteractions(context)
        verifyNoMoreInteractions(intent)
        verifyNoMoreInteractions(packageManager)
    }

    @Test
    fun testActivateWhenPortAuthorityDonate() {
        // setup
        whenever(mainActivity.applicationContext).thenReturn(context)
        whenever(context.packageManager).thenReturn(packageManager)
        whenever(packageManager.getLaunchIntentForPackage(portAuthorityDonate)).thenReturn(intent)
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.PORT_AUTHORITY)
        // validate
        verify(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        verify(context).startActivity(intent)
        verify(mainActivity).applicationContext
        verify(context).packageManager
        verify(packageManager).getLaunchIntentForPackage(portAuthorityDonate)
    }

    @Test
    fun testActivateWhenPortAuthorityFree() {
        // setup
        whenever(mainActivity.applicationContext).thenReturn(context)
        whenever(context.packageManager).thenReturn(packageManager)
        whenever(packageManager.getLaunchIntentForPackage(portAuthorityDonate)).thenReturn(null)
        whenever(packageManager.getLaunchIntentForPackage(portAuthorityFree)).thenReturn(intent)
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.PORT_AUTHORITY)
        // validate
        verify(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        verify(context).startActivity(intent)
        verify(mainActivity).applicationContext
        verify(context).packageManager
        verify(packageManager).getLaunchIntentForPackage(portAuthorityDonate)
        verify(packageManager).getLaunchIntentForPackage(portAuthorityFree)
    }

    @Test
    fun testActivateWhenPortAuthority() {
        // setup
        whenever(mainActivity.applicationContext).thenReturn(context)
        whenever(context.packageManager).thenReturn(packageManager)
        whenever(packageManager.getLaunchIntentForPackage(portAuthorityDonate)).thenReturn(null)
        whenever(packageManager.getLaunchIntentForPackage(portAuthorityFree)).thenReturn(null)
        whenever(packageManager.getLaunchIntentForPackage(portAuthority)).thenReturn(intent)
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.PORT_AUTHORITY)
        // validate
        verify(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        verify(context).startActivity(intent)
        verify(mainActivity).applicationContext
        verify(context).packageManager
        verify(packageManager).getLaunchIntentForPackage(portAuthorityDonate)
        verify(packageManager).getLaunchIntentForPackage(portAuthorityFree)
        verify(packageManager).getLaunchIntentForPackage(portAuthority)
    }

    @Test
    fun testActivateWhenNoPortAuthority() {
        // setup
        whenever(mainActivity.applicationContext).thenReturn(context)
        whenever(context.packageManager).thenReturn(packageManager)
        whenever(packageManager.getLaunchIntentForPackage(portAuthorityDonate)).thenReturn(null)
        whenever(packageManager.getLaunchIntentForPackage(portAuthorityFree)).thenReturn(null)
        whenever(packageManager.getLaunchIntentForPackage(portAuthority)).thenReturn(null)
        doReturn(intent).whenever(fixture).redirectToStore()
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.PORT_AUTHORITY)
        // validate
        verify(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        verify(context).startActivity(intent)
        verify(mainActivity).applicationContext
        verify(context).packageManager
        verify(packageManager).getLaunchIntentForPackage(portAuthorityDonate)
        verify(packageManager).getLaunchIntentForPackage(portAuthorityFree)
        verify(packageManager).getLaunchIntentForPackage(portAuthority)
        verify(fixture).redirectToStore()
    }

    @Test
    fun testRegistered() {
        // setup
        // execute
        val actual: Boolean = fixture.registered
        // validate
        assertFalse(actual)
    }

    @Test
    fun testVisibility() {
        // setup
        // execute
        val actual: Int = fixture.visibility
        // validate
        assertEquals(View.GONE, actual)
    }
}