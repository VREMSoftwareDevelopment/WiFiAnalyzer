/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.navigation.availability;

import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.options.OptionMenu;
import com.vrem.wifianalyzer.wifi.filter.adapter.FilterAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ContextCompat.class, DrawableCompat.class})
public class FilterOnTest {

    @Mock
    private MainActivity mainActivity;
    @Mock
    private OptionMenu optionMenu;
    @Mock
    private Menu menu;
    @Mock
    private MenuItem menuItem;
    @Mock
    private Drawable drawable;

    private FilterAdapter filterAdapter;
    private FilterOn fixture;

    @Before
    public void setUp() {
        mockStatic(ContextCompat.class);
        mockStatic(DrawableCompat.class);
        filterAdapter = MainContextHelper.INSTANCE.getFilterAdapter();
        fixture = new FilterOn();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
        verifyNoMoreInteractions(ContextCompat.class);
        verifyNoMoreInteractions(DrawableCompat.class);
        verifyNoMoreInteractions(menu);
        verifyNoMoreInteractions(menuItem);
        verifyNoMoreInteractions(filterAdapter);
        verifyNoMoreInteractions(optionMenu);
        verifyNoMoreInteractions(mainActivity);
        verifyNoMoreInteractions(drawable);
    }

    @Test
    public void testApplyWithFilterInactive() {
        // setup
        int colorResult = 200;
        when(filterAdapter.isActive()).thenReturn(false);
        when(ContextCompat.getColor(mainActivity, R.color.regular)).thenReturn(colorResult);
        withMenuItem();
        // execute
        fixture.apply(mainActivity);
        // validate
        verifyMenuItem();
        verifyStatic(ContextCompat.class);
        ContextCompat.getColor(mainActivity, R.color.regular);
        verifyStatic(DrawableCompat.class);
        DrawableCompat.setTint(drawable, colorResult);
    }

    @Test
    public void testApplyWithFilterActive() {
        // setup
        int colorResult = 100;
        when(filterAdapter.isActive()).thenReturn(true);
        when(ContextCompat.getColor(mainActivity, R.color.selected)).thenReturn(colorResult);
        withMenuItem();
        // execute
        fixture.apply(mainActivity);
        // validate
        verifyMenuItem();
        verifyStatic(ContextCompat.class);
        ContextCompat.getColor(mainActivity, R.color.selected);
        verifyStatic(DrawableCompat.class);
        DrawableCompat.setTint(drawable, colorResult);
    }

    @Test
    public void testApplyWithNoMenuDoesNotSetVisibleTrue() {
        // setup
        when(mainActivity.getOptionMenu()).thenReturn(optionMenu);
        when(optionMenu.getMenu()).thenReturn(null);
        // execute
        fixture.apply(mainActivity);
        // validate
        verify(mainActivity).getOptionMenu();
        verify(optionMenu).getMenu();
    }

    private void verifyMenuItem() {
        verify(mainActivity).getOptionMenu();
        verify(optionMenu).getMenu();
        verify(menu).findItem(R.id.action_filter);
        verify(menuItem).getIcon();
        verify(filterAdapter).isActive();
        verify(menuItem).setVisible(true);
    }

    private void withMenuItem() {
        when(mainActivity.getOptionMenu()).thenReturn(optionMenu);
        when(optionMenu.getMenu()).thenReturn(menu);
        when(menu.findItem(R.id.action_filter)).thenReturn(menuItem);
        when(menuItem.getIcon()).thenReturn(drawable);
    }
}