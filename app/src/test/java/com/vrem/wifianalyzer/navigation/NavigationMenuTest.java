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
package com.vrem.wifianalyzer.navigation;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.SettingActivity;
import com.vrem.wifianalyzer.vendor.VendorFragment;
import com.vrem.wifianalyzer.wifi.WiFiFragment;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NavigationMenuTest {

    @Test
    public void testNavigationMenu() throws Exception {
        assertEquals(4, NavigationMenu.values().length);
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(NavigationMenu.WIFI_LIST, NavigationMenu.find(-1));

        assertEquals(NavigationMenu.WIFI_LIST, NavigationMenu.find(NavigationMenu.WIFI_LIST.ordinal()));
        assertEquals(NavigationMenu.CHANNEL_ANALYZER, NavigationMenu.find(NavigationMenu.CHANNEL_ANALYZER.ordinal()));
        assertEquals(NavigationMenu.VENDOR_LIST, NavigationMenu.find(NavigationMenu.VENDOR_LIST.ordinal()));
        assertEquals(NavigationMenu.SETTINGS, NavigationMenu.find(NavigationMenu.SETTINGS.ordinal()));
    }

    @Test
    public void testGetFragment() throws Exception {
        assertTrue(NavigationMenu.WIFI_LIST.getFragment() instanceof WiFiFragment);
        assertTrue(NavigationMenu.WIFI_LIST.getFragment() instanceof WiFiFragment);
        assertTrue(NavigationMenu.VENDOR_LIST.getFragment() instanceof VendorFragment);
        assertNull(NavigationMenu.SETTINGS.getFragment());
    }

    @Test
    public void testGetActivity() throws Exception {
        assertNull(NavigationMenu.WIFI_LIST.getActivity());
        assertNull(NavigationMenu.VENDOR_LIST.getActivity());
        assertEquals(SettingActivity.class, NavigationMenu.SETTINGS.getActivity());
    }

    @Test
    public void testGetTitle() throws Exception {
        assertEquals(R.string.action_wifi_list, NavigationMenu.WIFI_LIST.getTitle());
        assertEquals(R.string.action_vendors, NavigationMenu.VENDOR_LIST.getTitle());
        assertEquals(R.string.action_settings, NavigationMenu.SETTINGS.getTitle());
    }

    @Test
    public void testGetIcon() throws Exception {
        assertEquals(R.mipmap.ic_launcher, NavigationMenu.WIFI_LIST.getIcon());
        assertEquals(R.drawable.ic_list_grey_500_48dp, NavigationMenu.VENDOR_LIST.getIcon());
        assertEquals(R.drawable.ic_settings_grey_500_48dp, NavigationMenu.SETTINGS.getIcon());
    }
}