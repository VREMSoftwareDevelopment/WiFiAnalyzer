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

import android.view.Menu;
import android.view.MenuItem;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.options.OptionMenu;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import androidx.appcompat.app.ActionBar;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class WiFiSwitchOffTest {

    @Mock
    private MainActivity mainActivity;
    @Mock
    private ActionBar actionBar;
    @Mock
    private OptionMenu optionMenu;
    @Mock
    private Menu menu;
    @Mock
    private MenuItem menuItem;

    private WiFiSwitchOff fixture;

    @Before
    public void setUp() {
        fixture = new WiFiSwitchOff();
    }

    @Test
    public void testApplyWithActionBarSetEmptySubtitle() {
        // setup
        when(mainActivity.getSupportActionBar()).thenReturn(actionBar);
        // execute
        fixture.apply(mainActivity);
        // validate
        verify(mainActivity).getSupportActionBar();
        verify(actionBar).setSubtitle(StringUtils.EMPTY);
    }

    @Test
    public void testApplyWithNoActionBarDoesNotSetSubtitle() {
        // setup
        when(mainActivity.getSupportActionBar()).thenReturn(null);
        // execute
        fixture.apply(mainActivity);
        // validate
        verify(mainActivity).getSupportActionBar();
        verify(actionBar, never()).setSubtitle(anyString());
    }

    @Test
    public void testApplyWithOptionMenuSetVisibleFalse() {
        // setup
        when(mainActivity.getOptionMenu()).thenReturn(optionMenu);
        when(optionMenu.getMenu()).thenReturn(menu);
        when(menu.findItem(R.id.action_wifi_band)).thenReturn(menuItem);
        // execute
        fixture.apply(mainActivity);
        // validate
        verify(mainActivity).getOptionMenu();
        verify(optionMenu).getMenu();
        verify(menu).findItem(R.id.action_wifi_band);
        verify(menuItem).setVisible(false);
    }

    @Test
    public void testApplyWithNoOptionMenuDoesNotSetWiFiBandVisible() {
        // setup
        MainActivity mainActivity = mock(MainActivity.class);
        when(mainActivity.getOptionMenu()).thenReturn(null);
        // execute
        fixture.apply(mainActivity);
        // validate
        verify(mainActivity).getOptionMenu();
        verifyMenu();
    }

    @Test
    public void testApplyWithNoMenuDoesNotSetWiFiBandVisible() {
        // setup
        MainActivity mainActivity = mock(MainActivity.class);
        when(mainActivity.getOptionMenu()).thenReturn(optionMenu);
        when(optionMenu.getMenu()).thenReturn(null);
        // execute
        fixture.apply(mainActivity);
        // validate
        verify(mainActivity).getOptionMenu();
        verify(optionMenu).getMenu();
        verifyMenu();
    }

    private void verifyMenu() {
        verify(menu, never()).findItem(R.id.action_wifi_band);
        verify(menuItem, never()).setVisible(anyBoolean());
    }

}