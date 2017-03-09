/*
 * WiFiAnalyzer
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

package com.vrem.wifianalyzer.navigation.options;

import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;

import com.vrem.util.TextUtils;
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
public class WiFiSwitchOnTest {
    private MainActivity mainActivity;
    private WiFiSwitchOn fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new WiFiSwitchOn();
    }

    @Test
    public void testApplySetSubtitle() throws Exception {
        // setup
        WiFiBand wiFiBand = MainContext.INSTANCE.getSettings().getWiFiBand();
        Resources resources = mainActivity.getResources();
        String wiFiBand2 = resources.getString(WiFiBand.GHZ2.getTextResource());
        String wiFiBand5 = resources.getString(WiFiBand.GHZ5.getTextResource());
        int colorSelected = ContextCompat.getColor(mainActivity, R.color.connected);
        int colorNotSelected = ContextCompat.getColor(mainActivity, R.color.icons_color);
        String subtitle = fixture.makeSubtitle(WiFiBand.GHZ2.equals(wiFiBand), wiFiBand2, wiFiBand5, colorSelected, colorNotSelected);
        CharSequence expected = TextUtils.fromHtml(subtitle);
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
        int color1 = 10;
        int color2 = 20;
        String text1 = "text1";
        String text2 = "text2";
        String expected = "<font color='" + color1 + "'><strong>" + text1
            + "</strong></font>" + WiFiSwitchOn.SPACER
            + "<font color='" + color2 + "'><small>" + text2 + "</small></font>";
        // execute
        String actual = fixture.makeSubtitle(true, text1, text2, color1, color2);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testMakeSubtitleGHZ5() throws Exception {
        // setup
        int color1 = 10;
        int color2 = 20;
        String text1 = "text1";
        String text2 = "text2";
        String expected = "<font color='" + color2 + "'><small>" + text1
            + "</small></font>" + WiFiSwitchOn.SPACER
            + "<font color='" + color1 + "'><strong>" + text2 + "</strong></font>";
        // execute
        String actual = fixture.makeSubtitle(false, text1, text2, color1, color2);
        // validate
        assertEquals(expected, actual);
    }

}