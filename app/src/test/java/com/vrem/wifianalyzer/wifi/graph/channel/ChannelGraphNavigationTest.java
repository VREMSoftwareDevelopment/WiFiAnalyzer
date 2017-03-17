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

package com.vrem.wifianalyzer.wifi.graph.channel;

import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelsGHZ5;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChannelGraphNavigationTest {

    private Scanner scanner;
    private Settings settings;
    private Configuration configuration;
    private View layout;
    private Map<Pair<WiFiChannel, WiFiChannel>, View> views;

    private MainActivity mainActivity;
    private ChannelGraphNavigation fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();

        scanner = MainContextHelper.INSTANCE.getScanner();
        settings = MainContextHelper.INSTANCE.getSettings();
        configuration = MainContextHelper.INSTANCE.getConfiguration();

        layout = mock(View.class);
        views = new HashMap<>();
        for (Pair<WiFiChannel, WiFiChannel> key : ChannelGraphNavigation.ids.keySet()) {
            Integer id = ChannelGraphNavigation.ids.get(key);
            View view = mock(View.class);
            views.put(key, view);
            when(layout.findViewById(id)).thenReturn(view);
        }

        fixture = new ChannelGraphNavigation(layout, mainActivity);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testMapping() throws Exception {
        assertEquals(WiFiChannelsGHZ5.SETS.size(), ChannelGraphNavigation.ids.size());
        for (Pair<WiFiChannel, WiFiChannel> set : WiFiChannelsGHZ5.SETS) {
            assertNotNull(ChannelGraphNavigation.ids.get(set));
        }
    }

    @Test
    public void testConstructor() throws Exception {
        for (Integer id : ChannelGraphNavigation.ids.values()) {
            verify(layout).findViewById(id);
        }
        for (View view : views.values()) {
            verify(view).setOnClickListener(any(ChannelGraphNavigation.SetOnClickListener.class));
        }
    }

    @Test
    public void testUpdateWithGHZ2() throws Exception {
        // setup
        when(settings.getCountryCode()).thenReturn(Locale.US.getCountry());
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ2);
        // execute
        fixture.update();
        // validate
        verify(layout).setVisibility(View.GONE);
        verify(settings).getCountryCode();
        verify(settings).getWiFiBand();
    }


    @Test
    public void testUpdateWithGHZ5AndUS() throws Exception {
        // setup
        int colorSelected = ContextCompat.getColor(mainActivity, R.color.connected);
        int colorNotSelected = ContextCompat.getColor(mainActivity, R.color.connected_background);
        Pair<WiFiChannel, WiFiChannel> selectedKey = WiFiBand.GHZ5.getWiFiChannels().getWiFiChannelPairs().get(0);
        when(configuration.getWiFiChannelPair()).thenReturn(selectedKey);
        when(settings.getCountryCode()).thenReturn(Locale.US.getCountry());
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ5);
        // execute
        fixture.update();
        // validate
        verify(layout).setVisibility(View.VISIBLE);
        for (Pair<WiFiChannel, WiFiChannel> key : views.keySet()) {
            View view = views.get(key);
            verify(view).setVisibility(View.VISIBLE);
            verify(view).setBackgroundColor(selectedKey.equals(key) ? colorSelected : colorNotSelected);
            verify(view).setSelected(selectedKey.equals(key));
        }
        for (Integer id : ChannelGraphNavigation.ids.values()) {
            verify(layout, times(2)).findViewById(id);
        }
        verify(settings).getCountryCode();
        verify(settings).getWiFiBand();
        verify(configuration).getWiFiChannelPair();
    }

    @Test
    public void testUpdateGHZ5WithJapan() throws Exception {
        // setup
        when(settings.getCountryCode()).thenReturn(Locale.JAPAN.getCountry());
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ5);
        // execute
        fixture.update();
        // validate
        verify(layout).setVisibility(View.VISIBLE);
        for (Pair<WiFiChannel, WiFiChannel> key : views.keySet()) {
            verify(views.get(key)).setVisibility(WiFiChannelsGHZ5.SET3.equals(key) ? View.GONE : View.VISIBLE);
        }
        verify(settings).getCountryCode();
        verify(settings).getWiFiBand();
    }

    @Test
    public void testUpdateGHZ5WithCountryThatHasOnlyOneSet() throws Exception {
        // setup
        when(settings.getCountryCode()).thenReturn("IL");
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ5);
        // execute
        fixture.update();
        // validate
        verify(layout).setVisibility(View.GONE);
        verify(settings).getCountryCode();
        verify(settings).getWiFiBand();
    }

    @Test
    public void testSetOnClickListener() throws Exception {
        // setup
        Pair<WiFiChannel, WiFiChannel> expected = WiFiChannelsGHZ5.SET3;
        ChannelGraphNavigation.SetOnClickListener fixture = new ChannelGraphNavigation.SetOnClickListener(expected);
        // execute
        fixture.onClick(layout);
        // validate
        verify(configuration).setWiFiChannelPair(expected);
        verify(scanner).update();
    }

}