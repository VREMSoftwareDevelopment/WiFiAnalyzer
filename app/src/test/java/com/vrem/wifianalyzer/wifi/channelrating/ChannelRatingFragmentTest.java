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

package com.vrem.wifianalyzer.wifi.channelrating;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChannelRatingFragmentTest {

    private Scanner scanner;
    private ChannelRatingFragment fixture;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();
        scanner = MainContextHelper.INSTANCE.getScanner();
        fixture = new ChannelRatingFragment();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testOnCreateView() throws Exception {
        // execute
        SupportFragmentTestUtil.startFragment(fixture);
        // validate
        assertNotNull(fixture);
        verify(scanner).update();
        verify(scanner).register(fixture.getChannelRatingAdapter());
    }

    @Test
    public void testOnResume() throws Exception {
        // setup
        SupportFragmentTestUtil.startFragment(fixture);
        // execute
        fixture.onResume();
        // validate
        verify(scanner, times(2)).update();
    }

    @Test
    public void testOnDestroy() throws Exception {
        // setup
        SupportFragmentTestUtil.startFragment(fixture);
        // execute
        fixture.onDestroy();
        // validate
        verify(scanner).unregister(fixture.getChannelRatingAdapter());
    }

}