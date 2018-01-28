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

package com.vrem.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationUtilsTest {
    private final static @StringRes
    int RES_ID = 12;

    @Mock
    private Context context;
    @Mock
    private ContextWrapper contextWrapper;
    @Mock
    private Resources resources;
    @Mock
    private Configuration configuration;
    @Mock
    private DisplayMetrics displayMetrics;
    @Mock
    private android.app.ActionBar actionBar;
    @Mock
    private android.support.v7.app.ActionBar actionBarv7;

    private Locale newLocale;

    @Before
    public void setUp() {
        newLocale = Locale.US;
    }

    @Test
    public void testCreateContext() throws Exception {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            validateCreateContextWithNougat();
        } else {
            validateCreateContextWithLegacy();
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void validateCreateContextWithNougat() throws Exception {
        // setup
        when(context.getResources()).thenReturn(resources);
        when(resources.getConfiguration()).thenReturn(configuration);
        when(context.createConfigurationContext(configuration)).thenReturn(contextWrapper);
        // execute
        Context actual = ConfigurationUtils.createContext(context, newLocale);
        // validate
        assertEquals(contextWrapper, actual);
        assertEquals(context, ((ContextWrapper) actual).getBaseContext());
        verify(configuration).setLocale(newLocale);
        verify(context).createConfigurationContext(configuration);
        verify(context).getResources();
        verify(resources).getConfiguration();
    }

    @SuppressWarnings("deprecation")
    private void validateCreateContextWithLegacy() throws Exception {
        // setup
        when(context.getResources()).thenReturn(resources);
        when(resources.getConfiguration()).thenReturn(configuration);
        when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        // execute
        Context actual = ConfigurationUtils.createContext(context, newLocale);
        // validate
        assertEquals(context, actual);
        assertEquals(newLocale, configuration.locale);
        verify(resources).getDisplayMetrics();
        verify(resources).updateConfiguration(configuration, displayMetrics);
        verify(context).getResources();
        verify(resources).getConfiguration();
    }

    @Test
    public void testSetActionBarOptions() throws Exception {
        // execute
        ConfigurationUtils.setActionBarOptions(actionBar, RES_ID);
        // validate
        verify(actionBar).setHomeButtonEnabled(true);
        verify(actionBar).setDisplayHomeAsUpEnabled(true);
        verify(actionBar).setTitle(RES_ID);
    }

    @Test
    public void testSetActionBarOptionsWithNullActionBar() throws Exception {
        // execute
        ConfigurationUtils.setActionBarOptions((android.app.ActionBar) null, RES_ID);
        // validate
        verify(actionBar, never()).setHomeButtonEnabled(true);
        verify(actionBar, never()).setDisplayHomeAsUpEnabled(true);
        verify(actionBar, never()).setTitle(RES_ID);
    }

    @Test
    public void testSetActionBarOptionsv7() throws Exception {
        // execute
        ConfigurationUtils.setActionBarOptions(actionBarv7, RES_ID);
        // validate
        verify(actionBarv7).setHomeButtonEnabled(true);
        verify(actionBarv7).setDisplayHomeAsUpEnabled(true);
        verify(actionBarv7).setTitle(RES_ID);
    }

    @Test
    public void testSetActionBarOptionsv7WithNullActionBar() throws Exception {
        // execute
        ConfigurationUtils.setActionBarOptions((android.support.v7.app.ActionBar) null, RES_ID);
        // validate
        verify(actionBarv7, never()).setHomeButtonEnabled(true);
        verify(actionBarv7, never()).setDisplayHomeAsUpEnabled(true);
        verify(actionBarv7, never()).setTitle(RES_ID);
    }

}