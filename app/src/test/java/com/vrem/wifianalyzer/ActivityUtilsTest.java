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

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.vrem.util.ConfigurationUtils;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConfigurationUtils.class)
public class ActivityUtilsTest {

    @Mock
    private Window window;
    @Mock
    private ActionBar actionBar;

    private Settings settings;

    @Before
    public void setUp() {
        mockStatic(ConfigurationUtils.class);

        settings = MainContextHelper.INSTANCE.getSettings();
    }

    @After
    public void tearDown() {
        verifyStatic(ConfigurationUtils.class);
        MainContextHelper.INSTANCE.restore();
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
        MainActivity mainActivity = mock(MainActivity.class);
        Toolbar toolbar = mock(Toolbar.class);
        ActionBar actionBar = mock(ActionBar.class);

        when(mainActivity.findViewById(R.id.toolbar)).thenReturn(toolbar);
        when(mainActivity.getSupportActionBar()).thenReturn(actionBar);
        // execute
        Toolbar actual = ActivityUtils.setupToolbar(mainActivity);
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
        MainActivity mainActivity = mock(MainActivity.class);
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
        MainActivity mainActivity = mock(MainActivity.class);
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
        MainActivity mainActivity = mock(MainActivity.class);
        when(settings.isKeepScreenOn()).thenReturn(true);
        when(mainActivity.getWindow()).thenReturn(window);
        // execute
        ActivityUtils.keepScreenOn(mainActivity);
        // validate
        verify(settings).isKeepScreenOn();
        verify(mainActivity).getWindow();
        verify(window).addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Test
    public void testKeepScreenOnSwitchOff() {
        // setup
        MainActivity mainActivity = mock(MainActivity.class);
        when(settings.isKeepScreenOn()).thenReturn(false);
        when(mainActivity.getWindow()).thenReturn(window);
        // execute
        ActivityUtils.keepScreenOn(mainActivity);
        // validate
        verify(settings).isKeepScreenOn();
        verify(mainActivity).getWindow();
        verify(window).clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

}