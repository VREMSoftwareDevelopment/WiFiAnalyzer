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

package com.vrem.wifianalyzer.settings;

import android.util.AttributeSet;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.navigation.NavigationGroup;
import com.vrem.wifianalyzer.navigation.NavigationMenu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class StartMenuPreferenceTest {

    private MainActivity mainActivity;
    private StartMenuPreference fixture;
    private AttributeSet attributeSet;

    @Before
    public void setUp() throws Exception {
        mainActivity = RobolectricUtil.INSTANCE.getMainActivity();
        attributeSet = mock(AttributeSet.class);
        fixture = new StartMenuPreference(mainActivity, attributeSet);
    }

    @Test
    public void testGetEntries() throws Exception {
        // setup
        NavigationMenu[] expected = NavigationGroup.GROUP_FEATURE.navigationMenu();
        // execute
        CharSequence[] actual = fixture.getEntries();
        // validate
        assertEquals(expected.length, actual.length);
        assertEquals(mainActivity.getResources().getString(expected[0].getTitle()), actual[0]);
        assertEquals(mainActivity.getResources().getString(expected[expected.length - 1].getTitle()), actual[expected.length - 1]);
    }

    @Test
    public void testGetEntryValues() throws Exception {
        // setup
        NavigationMenu[] expected = NavigationGroup.GROUP_FEATURE.navigationMenu();
        // execute
        CharSequence[] actual = fixture.getEntryValues();
        // validate
        assertEquals(expected.length, actual.length);
        assertEquals("" + expected[0].ordinal(), actual[0]);
        assertEquals("" + expected[expected.length - 1].ordinal(), actual[expected.length - 1]);
    }
}