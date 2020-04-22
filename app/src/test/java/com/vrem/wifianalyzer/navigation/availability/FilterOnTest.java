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
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
        filterAdapter = MainContextHelper.INSTANCE.getFilterAdapter();
        fixture = spy(new FilterOn());
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
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
        doReturn(colorResult).when(fixture).getColor(mainActivity, R.color.regular);
        withMenuItem();
        // execute
        fixture.apply(mainActivity);
        // validate
        verifyMenuItem();
        verify(fixture).getColor(mainActivity, R.color.regular);
        verify(fixture).setTint(drawable, colorResult);
    }

    @Test
    public void testApplyWithFilterActive() {
        // setup
        int colorResult = 100;
        when(filterAdapter.isActive()).thenReturn(true);
        doReturn(colorResult).when(fixture).getColor(mainActivity, R.color.selected);
        withMenuItem();
        // execute
        fixture.apply(mainActivity);
        // validate
        verifyMenuItem();
        verify(fixture).getColor(mainActivity, R.color.selected);
        verify(fixture).setTint(drawable, colorResult);
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