/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.annotation.Config;

import java.util.Locale;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class ConfigurationUtilsTest {
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

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Locale newLocale;

    @Before
    public void setUp() {
        newLocale = Locale.US;
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(context);
        verifyNoMoreInteractions(contextWrapper);
        verifyNoMoreInteractions(resources);
        verifyNoMoreInteractions(configuration);
        verifyNoMoreInteractions(displayMetrics);
    }

    @Test
    public void testCreateContext() {
        // setup
        when(context.getResources()).thenReturn(resources);
        when(resources.getConfiguration()).thenReturn(configuration);
        when(context.createConfigurationContext(configuration)).thenReturn(contextWrapper);
        when(contextWrapper.getBaseContext()).thenReturn(context);
        // execute
        Context actual = ConfigurationUtils.createContext(context, newLocale);
        // validate
        assertEquals(contextWrapper, actual);
        assertEquals(context, ((ContextWrapper) actual).getBaseContext());
        verify(configuration).setLocale(newLocale);
        verify(context).createConfigurationContext(configuration);
        verify(context).getResources();
        verify(contextWrapper).getBaseContext();
        verify(resources).getConfiguration();
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.M)
    public void testCreateContextLegacy() {
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

}