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

package com.vrem.wifianalyzer.navigation.options;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.vrem.wifianalyzer.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OptionMenuTest {

    @Mock
    private Menu menu;
    @Mock
    private MenuItem menuItem;
    @Mock
    private Activity activity;
    @Mock
    private MenuInflater menuInflater;

    private OptionMenu fixture;

    @Before
    public void setUp() {
        fixture = new OptionMenu();
    }

    @Test
    public void testCreate() {
        // setup
        when(activity.getMenuInflater()).thenReturn(menuInflater);
        // execute
        fixture.create(activity, menu);
        // validate
        assertEquals(menu, fixture.getMenu());
        verify(menuInflater).inflate(R.menu.optionmenu, menu);
    }

    @Test
    public void testActions() {
        // setup
        int itemId = -1;
        when(menuItem.getItemId()).thenReturn(itemId);
        // execute
        fixture.select(menuItem);
        // validate
        verify(menuItem).getItemId();
    }

}