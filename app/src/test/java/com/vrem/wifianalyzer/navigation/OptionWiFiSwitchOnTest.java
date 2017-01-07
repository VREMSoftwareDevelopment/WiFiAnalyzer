/*
 * WiFi Analyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.navigation;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class OptionWiFiSwitchOnTest {
    private MainActivity mainActivity;
    private OptionWiFiSwitchOn fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new OptionWiFiSwitchOn();
    }

    @Test
    public void testApplySetSubtitle() throws Exception {
        // setup
        WiFiBand wiFiBand = MainContext.INSTANCE.getSettings().getWiFiBand();
        int color = ContextCompat.getColor(mainActivity, R.color.connected);
        String subtitle = fixture.makeSubtitle(mainActivity, wiFiBand, color);
        CharSequence expected = fixture.fromHtml(subtitle);
        // execute
        fixture.apply(mainActivity);
        // validate
        CharSequence actual = mainActivity.getSupportActionBar().getSubtitle();
        assertTrue(expected.length() > 0);
        assertEquals(expected.length(), actual.length());
        for (int i = 0; i < expected.length(); i++) {
            assertEquals("" + i + ":" + expected.charAt(i) + ":" + actual.charAt(i), expected.charAt(i), actual.charAt(i));
        }
    }

    @Test
    public void testApplyWithNoActionBarDoesNotSetSubtitle() throws Exception {
        // setup
        MainActivity mainActivity = mock(MainActivity.class);
        ActionBar actionBar = mock(ActionBar.class);
        when(mainActivity.getSupportActionBar()).thenReturn(null);
        // execute
        fixture.apply(mainActivity);
        // validate
        verify(mainActivity).getSupportActionBar();
        verify(actionBar, never()).setSubtitle(anyString());
    }

    @Test
    public void testMakeSubtitleGHZ2() throws Exception {
        // setup
        int color = 10;
        String expected = "<font color='" + color + "'><strong>" + WiFiBand.GHZ2.getBand()
            + "</strong></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<small>" + WiFiBand.GHZ5.getBand() + "</small>";
        // execute
        String actual = fixture.makeSubtitle(mainActivity, WiFiBand.GHZ2, color);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testMakeSubtitleGHZ5() throws Exception {
        // setup
        int color = 10;
        String expected = "<small>" + WiFiBand.GHZ2.getBand() + "</small>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "<font color='" + color + "'><strong>" + WiFiBand.GHZ5.getBand() + "</strong></font>";
        // execute
        String actual = fixture.makeSubtitle(mainActivity, WiFiBand.GHZ5, color);
        // validate
        assertEquals(expected, actual);
    }

}