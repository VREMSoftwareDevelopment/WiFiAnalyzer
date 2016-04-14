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

package com.vrem.wifianalyzer.wifi;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChannelAvailableFragmentTest {

    private Configuration configuration;
    private ChannelAvailableFragment fixture;

    @Before
    public void setUp() throws Exception {
        RobolectricUtil.INSTANCE.getMainActivity();

        configuration = mock(Configuration.class);

        when(configuration.getLocale()).thenReturn(Locale.US);

        fixture = new ChannelAvailableFragment();
        fixture.setConfiguration(configuration);
    }

    @After
    public void tearDown() throws Exception {
        verify(configuration, atLeastOnce()).getLocale();
        verify(configuration, atLeastOnce()).isDevelopmentMode();
    }

    @Test
    public void testOnCreateView() throws Exception {
        // setup
        when(configuration.isDevelopmentMode()).thenReturn(false);
        // execute
        SupportFragmentTestUtil.startFragment(fixture);
        // validate
        assertNotNull(fixture);
    }

    @Test
    public void testOnCreateViewInDevelopmentMode() throws Exception {
        // setup
        when(configuration.isDevelopmentMode()).thenReturn(true);
        // execute
        SupportFragmentTestUtil.startFragment(fixture);
        // validate
        assertNotNull(fixture);
    }

}