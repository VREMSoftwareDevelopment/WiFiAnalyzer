/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.vrem.wifianalyzer;

import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.navigation.NavigationMenuView;
import com.vrem.wifianalyzer.settings.ThemeStyle;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    private MainActivity fixture;

    @Before
    public void setUp() throws Exception {
        fixture = RobolectricUtil.INSTANCE.getMainActivity();
    }

    @After
    public void tearDown() throws Exception {
        fixture.getNavigationMenuView().setCurrentNavigationMenu(NavigationMenu.ACCESS_POINTS);
    }

    @Test
    public void testMainActivity() throws Exception {
        assertEquals(WiFiBand.GHZ2.getBand(), fixture.getSupportActionBar().getSubtitle());
        assertTrue(MainContext.INSTANCE.getScanner().isRunning());
    }

    @Test
    public void testClickingOnToolbarTogglesWiFiBand() throws Exception {
        assertEquals(NavigationMenu.ACCESS_POINTS, fixture.getNavigationMenuView().getCurrentNavigationMenu());

        assertEquals(WiFiBand.GHZ2.getBand(), fixture.getSupportActionBar().getSubtitle());
        fixture.findViewById(R.id.toolbar).performClick();
        assertEquals(WiFiBand.GHZ5.getBand(), fixture.getSupportActionBar().getSubtitle());
        fixture.findViewById(R.id.toolbar).performClick();
        assertEquals(WiFiBand.GHZ2.getBand(), fixture.getSupportActionBar().getSubtitle());
    }

    @Test
    public void testClickingOnToolbarDoesNotTogglesWiFiBand() throws Exception {
        NavigationMenuView navigationMenuView = fixture.getNavigationMenuView();
        navigationMenuView.setCurrentNavigationMenu(NavigationMenu.VENDOR_LIST);

        assertEquals(WiFiBand.GHZ2.getBand(), fixture.getSupportActionBar().getSubtitle());
        fixture.findViewById(R.id.toolbar).performClick();
        assertEquals(WiFiBand.GHZ2.getBand(), fixture.getSupportActionBar().getSubtitle());
    }

    @Test
    public void testOnPause() throws Exception {
        fixture.onPause();
        assertFalse(MainContext.INSTANCE.getScanner().isRunning());
        fixture.onResume();
        assertTrue(MainContext.INSTANCE.getScanner().isRunning());
    }

    @Test
    public void testOnBackPressed() throws Exception {
        // setup
        // execute
        fixture.onBackPressed();
        // validate
    }

    @Test
    public void testShouldNotReloadWithNoChanges() throws Exception {
        // setup
        ThemeStyle currentThemeStyle = fixture.getCurrentThemeStyle();
        // execute && validate
        assertFalse(fixture.shouldReload());
        assertEquals(currentThemeStyle, fixture.getCurrentThemeStyle());
    }

    @Test
    public void testShouldReloadWithThemeChange() throws Exception {
        // setup
        ThemeStyle expected = fixture.getCurrentThemeStyle();
        fixture.setCurrentThemeStyle(ThemeStyle.LIGHT.equals(expected) ? ThemeStyle.DARK : ThemeStyle.LIGHT);
        // execute && validate
        assertTrue(fixture.shouldReload());
        assertEquals(expected, fixture.getCurrentThemeStyle());
    }

}