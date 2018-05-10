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

package com.vrem.wifianalyzer.navigation.availability;

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

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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

    private FilterAdapter filterAdapter;
    private FilterOn fixture;

    @Before
    public void setUp() {
        filterAdapter = MainContextHelper.INSTANCE.getFilterAdapter();
        fixture = new FilterOn();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testApplySetMenuItemVisibleTrue() {
        // setup
        withMenuItem();
        // execute
        fixture.apply(mainActivity);
        // validate
        verifyMenuItem();
        verify(menuItem).setVisible(true);

    }

    @Test
    public void testApplyWithFilterInactive() {
        // setup
        when(filterAdapter.isActive()).thenReturn(false);
        withMenuItem();
        // execute
        fixture.apply(mainActivity);
        // validate
        verifyMenuItem();
        verify(filterAdapter).isActive();
        verify(menuItem).setIcon(R.drawable.ic_filter_list_grey_500_24dp);
    }

    @Test
    public void testApplyWithFilterActive() {
        // setup
        when(filterAdapter.isActive()).thenReturn(true);
        withMenuItem();
        // execute
        fixture.apply(mainActivity);
        // validate
        verifyMenuItem();
        verify(filterAdapter).isActive();
        verify(menuItem).setIcon(R.drawable.ic_filter_list_blue_500_24dp);
    }

    private void verifyMenuItem() {
        verify(mainActivity).getOptionMenu();
        verify(optionMenu).getMenu();
        verify(menu).findItem(R.id.action_filter);
    }

    private void withMenuItem() {
        when(mainActivity.getOptionMenu()).thenReturn(optionMenu);
        when(optionMenu.getMenu()).thenReturn(menu);
        when(menu.findItem(R.id.action_filter)).thenReturn(menuItem);
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
        verify(menu, never()).findItem(R.id.action_filter);
        verify(menuItem, never()).setVisible(true);
    }

}