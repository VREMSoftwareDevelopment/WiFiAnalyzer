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

import android.support.v7.app.ActionBar;

import com.vrem.wifianalyzer.MainActivity;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WiFiSwitchOffTest {

    @Mock
    private MainActivity mainActivity;
    @Mock
    private ActionBar actionBar;

    private WiFiSwitchOff fixture;

    @Before
    public void setUp() {
        fixture = new WiFiSwitchOff();
    }

    @Test
    public void testApplySetEmptySubtitle() throws Exception {
        // setup
        when(mainActivity.getSupportActionBar()).thenReturn(actionBar);
        // execute
        fixture.apply(mainActivity);
        // validate
        verify(mainActivity).getSupportActionBar();
        verify(actionBar).setSubtitle(StringUtils.EMPTY);
    }

    @Test
    public void testApplyWithNoActionBarDoesNotSetSubtitle() throws Exception {
        // setup
        when(mainActivity.getSupportActionBar()).thenReturn(null);
        // execute
        fixture.apply(mainActivity);
        // validate
        verify(mainActivity).getSupportActionBar();
        verify(actionBar, never()).setSubtitle(anyString());
    }

}