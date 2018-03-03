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
import android.util.DisplayMetrics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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

}