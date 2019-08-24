/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer;

import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

import com.vrem.util.IntentUtils;
import com.vrem.wifianalyzer.ActivityUtils.WiFiBandToggle;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.settings.Settings;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(IntentUtils.class)
public class ActivityUtilsTest {

    @Mock
    private Window window;
    @Mock
    private ActionBar actionBar;
    @Mock
    private Toolbar toolbar;
    @Mock
    private Intent intent;

    private MainActivity mainActivity;
    private Settings settings;

    @Before
    public void setUp() {
        mockStatic(IntentUtils.class);

        mainActivity = MainContextHelper.INSTANCE.getMainActivity();
        settings = MainContextHelper.INSTANCE.getSettings();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
        verifyNoMoreInteractions(IntentUtils.class);
        verifyNoMoreInteractions(mainActivity);
        verifyNoMoreInteractions(toolbar);
        verifyNoMoreInteractions(actionBar);
        verifyNoMoreInteractions(window);
        verifyNoMoreInteractions(settings);
        verifyNoMoreInteractions(intent);
    }

    @Test
    public void testSetActionBarOptions() {
        // execute
        ActivityUtils.setActionBarOptions(actionBar);
        // validate
        verify(actionBar).setHomeButtonEnabled(true);
        verify(actionBar).setDisplayHomeAsUpEnabled(true);
    }

    @Test
    public void testSetActionBarOptionsWithNullActionBar() {
        // execute
        ActivityUtils.setActionBarOptions(null);
        // validate
        verify(actionBar, never()).setHomeButtonEnabled(true);
        verify(actionBar, never()).setDisplayHomeAsUpEnabled(true);
    }

    @Test
    public void testSetupToolbar() {
        // setup
        when(mainActivity.findViewById(R.id.toolbar)).thenReturn(toolbar);
        when(mainActivity.getSupportActionBar()).thenReturn(actionBar);
        // execute
        Toolbar actual = ActivityUtils.setupToolbar();
        // validate
        assertEquals(toolbar, actual);

        verify(mainActivity).findViewById(R.id.toolbar);
        verify(mainActivity).getSupportActionBar();

        verify(toolbar).setOnClickListener(any(WiFiBandToggle.class));
        verify(mainActivity).setSupportActionBar(toolbar);
        verify(actionBar).setHomeButtonEnabled(true);
        verify(actionBar).setDisplayHomeAsUpEnabled(true);
    }

    @Test
    public void testWiFiBandToggleOnClickToggles() {
        // setup
        when(mainActivity.getCurrentNavigationMenu()).thenReturn(NavigationMenu.CHANNEL_GRAPH);
        WiFiBandToggle wiFiBandToggle = new WiFiBandToggle(mainActivity);
        // execute
        wiFiBandToggle.onClick(null);
        // validate
        verify(settings).toggleWiFiBand();
        verify(mainActivity).getCurrentNavigationMenu();
    }

    @Test
    public void testWiFiBandToggleOnClickDoesNotToggles() {
        // setup
        when(mainActivity.getCurrentNavigationMenu()).thenReturn(NavigationMenu.ACCESS_POINTS);
        WiFiBandToggle wiFiBandToggle = new WiFiBandToggle(mainActivity);
        // execute
        wiFiBandToggle.onClick(null);
        // validate
        verify(settings, never()).toggleWiFiBand();
        verify(mainActivity).getCurrentNavigationMenu();
    }

    @Test
    public void testKeepScreenOnSwitchOn() {
        // setup
        when(settings.isKeepScreenOn()).thenReturn(true);
        when(mainActivity.getWindow()).thenReturn(window);
        // execute
        ActivityUtils.keepScreenOn();
        // validate
        verify(settings).isKeepScreenOn();
        verify(mainActivity).getWindow();
        verify(window).addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Test
    public void testKeepScreenOnSwitchOff() {
        // setup
        when(settings.isKeepScreenOn()).thenReturn(false);
        when(mainActivity.getWindow()).thenReturn(window);
        // execute
        ActivityUtils.keepScreenOn();
        // validate
        verify(settings).isKeepScreenOn();
        verify(mainActivity).getWindow();
        verify(window).clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Test
    public void testStartWiFiSettings() {
        // setup
        when(IntentUtils.makeIntent(android.provider.Settings.Panel.ACTION_WIFI)).thenReturn(intent);
        // execute
        ActivityUtils.startWiFiSettings();
        // validate
        verify(mainActivity).startActivityForResult(intent, 0);
        verifyStatic(IntentUtils.class);
        IntentUtils.makeIntent(android.provider.Settings.Panel.ACTION_WIFI);
    }

    @Test
    public void testStartLocationSettings() {
        // setup
        when(IntentUtils.makeIntent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)).thenReturn(intent);
        // execute
        ActivityUtils.startLocationSettings();
        // validate
        verify(mainActivity).startActivity(intent);
        verifyStatic(IntentUtils.class);
        IntentUtils.makeIntent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    }

}