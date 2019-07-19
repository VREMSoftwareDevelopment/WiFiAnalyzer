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

package com.vrem.wifianalyzer.wifi.channelgraph;

import android.os.Build;
import android.view.View;
import android.widget.Button;

import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelsGHZ5;
import com.vrem.wifianalyzer.wifi.model.SortBy;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.scanner.ScannerService;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class ChannelGraphNavigationTest {

    private ScannerService scanner;
    private Settings settings;
    private Configuration configuration;
    private View layout;
    private Map<Pair<WiFiChannel, WiFiChannel>, View> views;

    private MainActivity mainActivity;
    private ChannelGraphNavigation fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();

        scanner = MainContextHelper.INSTANCE.getScannerService();
        settings = MainContextHelper.INSTANCE.getSettings();
        configuration = MainContextHelper.INSTANCE.getConfiguration();

        layout = mock(View.class);
        views = new HashMap<>();
        IterableUtils.forEach(ChannelGraphNavigation.ids.keySet(), new PairSetUpClosure());

        fixture = new ChannelGraphNavigation(layout, mainActivity);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testMapping() {
        assertEquals(WiFiChannelsGHZ5.SETS.size(), ChannelGraphNavigation.ids.size());
        IterableUtils.forEach(WiFiChannelsGHZ5.SETS, new PairMappingClosure());
    }

    @Test
    public void testConstructor() {
        IterableUtils.forEach(ChannelGraphNavigation.ids.values(), new IntegerClosure());
        IterableUtils.forEach(views.values(), new ViewClosure());
    }

    @Test
    public void testUpdateWithGHZ2() {
        // setup
        when(settings.getCountryCode()).thenReturn(Locale.US.getCountry());
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ2);
        // execute
        fixture.update(WiFiData.EMPTY);
        // validate
        verify(layout).setVisibility(View.GONE);
        verify(settings).getCountryCode();
        verify(settings).getWiFiBand();
    }


    @Test
    public void testUpdateWithGHZ5AndUS() {
        // setup
        int colorSelected = ContextCompat.getColor(mainActivity, R.color.selected);
        int colorNotSelected = ContextCompat.getColor(mainActivity, R.color.background);
        Pair<WiFiChannel, WiFiChannel> selectedKey = WiFiBand.GHZ5.getWiFiChannels().getWiFiChannelPairs().get(0);
        when(configuration.getWiFiChannelPair()).thenReturn(selectedKey);
        when(settings.getCountryCode()).thenReturn(Locale.US.getCountry());
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ5);
        when(settings.getSortBy()).thenReturn(SortBy.CHANNEL);
        // execute
        fixture.update(WiFiData.EMPTY);
        // validate
        verify(layout).setVisibility(View.VISIBLE);
        IterableUtils.forEach(views.keySet(), new PairUpdateClosure(selectedKey, colorSelected, colorNotSelected));
        IterableUtils.forEach(ChannelGraphNavigation.ids.values(), new IntegerUpdateClosure());
        verify(settings).getCountryCode();
        verify(settings, times(2)).getWiFiBand();
        verify(settings).getSortBy();
        verify(configuration).getWiFiChannelPair();
    }

    @Test
    public void testUpdateGHZ5WithJapan() {
        // setup
        when(settings.getCountryCode()).thenReturn(Locale.JAPAN.getCountry());
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ5);
        when(settings.getSortBy()).thenReturn(SortBy.CHANNEL);
        // execute
        fixture.update(WiFiData.EMPTY);
        // validate
        verify(layout).setVisibility(View.VISIBLE);
        IterableUtils.forEach(views.keySet(), new PairClosure());
        verify(settings).getCountryCode();
        verify(settings, times(2)).getWiFiBand();
        verify(settings).getSortBy();
    }

    @Test
    public void testUpdateGHZ5WithCountryThatHasOnlyOneSet() {
        // setup
        when(settings.getCountryCode()).thenReturn("IL");
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ5);
        // execute
        fixture.update(WiFiData.EMPTY);
        // validate
        verify(layout).setVisibility(View.GONE);
        verify(settings).getCountryCode();
        verify(settings).getWiFiBand();
    }

    @Test
    public void testSetOnClickListener() {
        // setup
        Pair<WiFiChannel, WiFiChannel> expected = WiFiChannelsGHZ5.SET3;
        ChannelGraphNavigation.SetOnClickListener fixture = new ChannelGraphNavigation.SetOnClickListener(expected);
        // execute
        fixture.onClick(layout);
        // validate
        verify(configuration).setWiFiChannelPair(expected);
        verify(scanner).update();
    }

    private static class PairMappingClosure implements Closure<Pair<WiFiChannel, WiFiChannel>> {
        @Override
        public void execute(Pair<WiFiChannel, WiFiChannel> set) {
            assertNotNull(ChannelGraphNavigation.ids.get(set));
        }
    }

    private static class ViewClosure implements Closure<View> {
        @Override
        public void execute(View view) {
            verify(view).setOnClickListener(any(ChannelGraphNavigation.SetOnClickListener.class));
        }
    }

    private class PairSetUpClosure implements Closure<Pair<WiFiChannel, WiFiChannel>> {
        @Override
        public void execute(Pair<WiFiChannel, WiFiChannel> key) {
            Integer id = ChannelGraphNavigation.ids.get(key);
            Button button = mock(Button.class);
            views.put(key, button);
            when(layout.findViewById(id)).thenReturn(button);
            when(button.getText()).thenReturn("ButtonName");
        }
    }

    private class IntegerClosure implements Closure<Integer> {
        @Override
        public void execute(Integer input) {
            verify(layout).findViewById(input);
        }
    }

    private class PairUpdateClosure implements Closure<Pair<WiFiChannel, WiFiChannel>> {
        private final Pair<WiFiChannel, WiFiChannel> selectedKey;
        private final int colorSelected;
        private final int colorNotSelected;

        private PairUpdateClosure(Pair<WiFiChannel, WiFiChannel> selectedKey, int colorSelected, int colorNotSelected) {
            this.selectedKey = selectedKey;
            this.colorSelected = colorSelected;
            this.colorNotSelected = colorNotSelected;
        }

        @Override
        public void execute(Pair<WiFiChannel, WiFiChannel> key) {
            Button button = (Button) views.get(key);
            verify(button).setVisibility(View.VISIBLE);
            verify(button).setBackgroundColor(selectedKey.equals(key) ? colorSelected : colorNotSelected);
            verify(button).setSelected(selectedKey.equals(key));
        }
    }

    private class IntegerUpdateClosure implements Closure<Integer> {
        @Override
        public void execute(Integer id) {
            verify(layout, times(2)).findViewById(id);
        }
    }

    private class PairClosure implements Closure<Pair<WiFiChannel, WiFiChannel>> {
        @Override
        public void execute(Pair<WiFiChannel, WiFiChannel> key) {
            verify(views.get(key)).setVisibility(WiFiChannelsGHZ5.SET3.equals(key) ? View.GONE : View.VISIBLE);
        }
    }
}