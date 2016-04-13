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

import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.settings.ThemeStyle;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    private MainActivity fixture;
    private Scanner scanner;

    @Before
    public void setUp() throws Exception {
        fixture = RobolectricUtil.INSTANCE.getMainActivity();

        scanner = mock(Scanner.class);
        MainContext.INSTANCE.setScanner(scanner);
    }

    @After
    public void tearDown() throws Exception {
        RobolectricUtil.INSTANCE.restore();
    }

    @Test
    public void testOnSharedPreferenceChanged() throws Exception {
        // setup
        assertEquals(WiFiBand.GHZ2.getBand(), fixture.getSupportActionBar().getSubtitle());
        MainContext.INSTANCE.getSettings().toggleWiFiBand();
        // execute
        fixture.onSharedPreferenceChanged(null, null);
        // validate
        assertEquals(WiFiBand.GHZ5.getBand(), fixture.getSupportActionBar().getSubtitle());
        verify(scanner, atLeastOnce()).update();
    }

    @Test
    public void testOnSharedPreferenceChangedThemeChange() throws Exception {
        // setup
        assertEquals(ThemeStyle.DARK, fixture.getCurrentThemeStyle());
        Settings settings = mock(Settings.class);
        MainContext.INSTANCE.setSettings(settings);
        when(settings.getThemeStyle()).thenReturn(ThemeStyle.LIGHT);
        // execute
        fixture.onSharedPreferenceChanged(null, null);
        // validate
        assertEquals(ThemeStyle.LIGHT, fixture.getCurrentThemeStyle());
        verify(scanner, never()).update();
    }

    @Test
    public void testOnPause() throws Exception {
        // execute
        fixture.onPause();
        // validate
        verify(scanner).pause();
    }

    @Test
    public void testOnBackPressed() throws Exception {
        // setup
        // execute
        fixture.onBackPressed();
        // validate
    }
}