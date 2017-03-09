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

import com.vrem.wifianalyzer.BuildConfig;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Ignore
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChannelGraphNavigationTest {
/*
    private Scanner scanner;
    private Settings settings;
    private Configuration configuration;
    private ChannelGraphNavigation fixture;


    @Before
    public void setUp() {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getActivity();

        scanner = MainContextHelper.INSTANCE.getScanner();
        settings = MainContextHelper.INSTANCE.getSettings();
        configuration = MainContextHelper.INSTANCE.getConfiguration();

        fixture = new ChannelGraphNavigation(mainActivity, configuration);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testChannelGraphNavigation() throws Exception {
        verify(configuration, times(3)).isLargeScreen();
    }

    @Test
    public void testUpdateWithGHZ2() throws Exception {
        // setup
        when(settings.getCountryCode()).thenReturn(Locale.US.getCountry());
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ2);
        // execute
        fixture.update();
        // validate
        List<NavigationItem> navigationItems = fixture.getNavigationItems();
        for (NavigationItem navigationItem : navigationItems) {
            Button button = navigationItem.getButton();
            assertEquals(View.GONE, button.getVisibility());
            assertFalse(button.isSelected());
        }
        verify(settings).getCountryCode();
        verify(settings).getWiFiBand();
    }

    @Test
    public void testUpdateWithGHZ5AndUS() throws Exception {
        // setup
        when(configuration.getWiFiChannelPair()).thenReturn(WiFiBand.GHZ5.getWiFiChannels().getWiFiChannelPairs().get(0));
        when(settings.getCountryCode()).thenReturn(Locale.US.getCountry());
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ5);
        // execute
        fixture.update();
        // validate
        List<NavigationItem> navigationItems = fixture.getNavigationItems();

        Button button = navigationItems.get(0).getButton();
        assertEquals(View.VISIBLE, button.getVisibility());
        assertTrue(button.isSelected());

        button = navigationItems.get(1).getButton();
        assertEquals(View.VISIBLE, button.getVisibility());
        assertFalse(button.isSelected());

        button = navigationItems.get(2).getButton();
        assertEquals(View.VISIBLE, button.getVisibility());
        assertFalse(button.isSelected());

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
        List<NavigationItem> navigationItems = fixture.getNavigationItems();
        assertEquals(View.VISIBLE, navigationItems.get(0).getButton().getVisibility());
        assertEquals(View.VISIBLE, navigationItems.get(1).getButton().getVisibility());
        assertEquals(View.GONE, navigationItems.get(2).getButton().getVisibility());
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
        List<NavigationItem> navigationItems = fixture.getNavigationItems();
        assertEquals(View.GONE, navigationItems.get(0).getButton().getVisibility());
        assertEquals(View.GONE, navigationItems.get(1).getButton().getVisibility());
        assertEquals(View.GONE, navigationItems.get(2).getButton().getVisibility());
        verify(settings).getCountryCode();
        verify(settings).getWiFiBand();
    }

    @Test
    public void testSelectNavigationUpdatesConfigurationAndScans() throws Exception {
        // setup
        Pair<WiFiChannel, WiFiChannel> expected = WiFiBand.GHZ5.getWiFiChannels().getWiFiChannelPairs().get(0);
        NavigationItem navigationItem = fixture.getNavigationItems().get(0);
        // execute
        navigationItem.getButton().callOnClick();
        // validate
        verify(configuration).setWiFiChannelPair(expected);
        verify(scanner).update();
    }
*/
}