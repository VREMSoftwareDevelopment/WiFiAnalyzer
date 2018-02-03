/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer;

import android.content.Context;
import android.support.v7.app.ActionBar;

import com.vrem.util.ConfigurationUtils;
import com.vrem.wifianalyzer.settings.Settings;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Locale;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConfigurationUtils.class)
public class ActivityUtilsTest {

    @Mock
    private ActionBar actionBar;
    @Mock
    private Context context;
    @Mock
    private Context newContext;

    private Settings settings;

    @Before
    public void setUp() {
        mockStatic(ConfigurationUtils.class);

        settings = MainContextHelper.INSTANCE.getSettings();
    }

    @After
    public void tearDown() {
        verifyStatic(ConfigurationUtils.class);
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testSetActionBarOptions() throws Exception {
        // execute
        ActivityUtils.setActionBarOptions(actionBar);
        // validate
        verify(actionBar).setHomeButtonEnabled(true);
        verify(actionBar).setDisplayHomeAsUpEnabled(true);
    }

    @Test
    public void testSetActionBarOptionsWithNullActionBar() throws Exception {
        // execute
        ActivityUtils.setActionBarOptions(null);
        // validate
        verify(actionBar, never()).setHomeButtonEnabled(true);
        verify(actionBar, never()).setDisplayHomeAsUpEnabled(true);
    }

    @Test
    public void testCreateContext() throws Exception {
        // setup
        when(settings.getLanguageLocale()).thenReturn(Locale.US);
        when(ConfigurationUtils.createContext(context, Locale.US)).thenReturn(newContext);
        // execute
        Context actual = ActivityUtils.createContext(context);
        // validate
        Assert.assertEquals(newContext, actual);
        verify(settings).getLanguageLocale();
    }

}