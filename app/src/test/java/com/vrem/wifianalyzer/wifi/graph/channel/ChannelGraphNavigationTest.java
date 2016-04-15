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

package com.vrem.wifianalyzer.wifi.graph.channel;

import android.support.v4.util.Pair;
import android.view.View;
import android.widget.Button;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChannelGraphNavigationTest {
    private Scanner scanner;
    private Settings settings;
    private Configuration configuration;
    private ChannelGraphNavigation fixture;

    @Before
    public void setUp() throws Exception {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getMainActivity();

        settings = mock(Settings.class);
        scanner = mock(Scanner.class);
        configuration = mock(Configuration.class);

        when(configuration.getWiFiChannelPair()).thenReturn(new Pair<>(WiFiChannel.UNKNOWN, WiFiChannel.UNKNOWN));
        when(configuration.getLocale()).thenReturn(Locale.US);

        fixture = new ChannelGraphNavigation(mainActivity, configuration);
        fixture.setSettings(settings);
        fixture.setScanner(scanner);
        fixture.setResources(mainActivity.getResources());
    }

    @After
    public void tearDown() throws Exception {
        verify(configuration).getWiFiChannelPair();
        verify(configuration).getLocale();
    }

    @Test
    public void testGetNavigationItems() throws Exception {
        // execute
        List<Button> actual = fixture.getNavigationItems();
        // validate
        assertEquals(3, actual.size());
    }

    @Test
    public void testUpdateGHZ2ChangesNavigationToInvisible() throws Exception {
        // setup
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ2);
        // execute
        fixture.update();
        // validate
        List<Button> navigationItems = fixture.getNavigationItems();
        for (Button button : navigationItems) {
            assertEquals(View.GONE, button.getVisibility());
        }
    }

    @Test
    public void testUpdateGHZ5ChangesNavigationToVisible() throws Exception {
        // setup
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ5);
        // execute
        fixture.update();
        // validate
        List<Button> navigationItems = fixture.getNavigationItems();
        for (Button button : navigationItems) {
            assertEquals(View.VISIBLE, button.getVisibility());
        }
    }

    @Test
    public void testSelectNavigationUpdatesConfigurationAndScans() throws Exception {
        // setup
        Pair<WiFiChannel, WiFiChannel> expected = WiFiBand.GHZ5.getWiFiChannels().getWiFiChannelPairs(Locale.US).get(0);
        Button navigationItem = fixture.getNavigationItems().get(0);
        // execute
        navigationItem.callOnClick();
        // validate
        verify(configuration).setWiFiChannelPair(expected);
        verify(scanner).update();
    }

}