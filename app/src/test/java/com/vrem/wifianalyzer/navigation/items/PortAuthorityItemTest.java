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

package com.vrem.wifianalyzer.navigation.items;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.MenuItem;
import android.view.View;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.navigation.NavigationMenu;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PortAuthorityItemTest {
    @Mock
    private MainActivity mainActivity;
    @Mock
    private Context context;
    @Mock
    private Intent intent;
    @Mock
    private MenuItem menuItem;
    @Mock
    private PackageManager packageManager;

    private PortAuthorityItem fixture;

    @Before
    public void setUp() {
        fixture = spy(new PortAuthorityItem());
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mainActivity);
        verifyNoMoreInteractions(menuItem);
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(intent);
        verifyNoMoreInteractions(packageManager);
    }

    @Test
    public void testActivateWhenPortAuthorityDonate() {
        // setup
        when(mainActivity.getApplicationContext()).thenReturn(context);
        when(context.getPackageManager()).thenReturn(packageManager);
        when(packageManager.getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_DONATE)).thenReturn(intent);
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.PORT_AUTHORITY);
        // validate
        verify(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        verify(context).startActivity(intent);
        verify(mainActivity).getApplicationContext();
        verify(context).getPackageManager();
        verify(packageManager).getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_DONATE);
    }

    @Test
    public void testActivateWhenPortAuthorityFree() {
        // setup
        when(mainActivity.getApplicationContext()).thenReturn(context);
        when(context.getPackageManager()).thenReturn(packageManager);
        when(packageManager.getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_DONATE)).thenReturn(null);
        when(packageManager.getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_FREE)).thenReturn(intent);
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.PORT_AUTHORITY);
        // validate
        verify(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        verify(context).startActivity(intent);
        verify(mainActivity).getApplicationContext();
        verify(context).getPackageManager();
        verify(packageManager).getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_DONATE);
        verify(packageManager).getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_FREE);
    }

    @Test
    public void testActivateWhenNoPortAuthority() {
        // setup
        when(mainActivity.getApplicationContext()).thenReturn(context);
        when(context.getPackageManager()).thenReturn(packageManager);
        when(packageManager.getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_DONATE)).thenReturn(null);
        when(packageManager.getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_FREE)).thenReturn(null);
        doReturn(intent).when(fixture).redirectToPlayStore();
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.PORT_AUTHORITY);
        // validate
        verify(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        verify(context).startActivity(intent);
        verify(mainActivity).getApplicationContext();
        verify(context).getPackageManager();
        verify(packageManager).getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_DONATE);
        verify(packageManager).getLaunchIntentForPackage(PortAuthorityItem.PORT_AUTHORITY_FREE);
        verify(fixture).redirectToPlayStore();
    }

    @Test
    public void isRegistered() {
        // setup
        // execute
        boolean actual = fixture.isRegistered();
        // validate
        assertFalse(actual);
    }

    @Test
    public void getVisibility() {
        // setup
        // execute
        int actual = fixture.getVisibility();
        // validate
        assertEquals(View.GONE, actual);
    }
}