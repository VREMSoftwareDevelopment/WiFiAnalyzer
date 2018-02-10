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

package com.vrem.wifianalyzer.navigation.items;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.navigation.NavigationMenuView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FragmentItemTest {

    @Mock
    private Fragment fragment;
    @Mock
    private MainActivity mainActivity;
    @Mock
    private MenuItem menuItem;
    @Mock
    private NavigationMenuView navigationMenuView;
    @Mock
    private FragmentManager fragmentManager;
    @Mock
    private FragmentTransaction fragmentTransaction;

    @Test
    public void testActivate() throws Exception {
        // setup
        FragmentItem fixture = new FragmentItem(fragment, true);
        String title = "title";
        NavigationMenu navigationMenu = NavigationMenu.ACCESS_POINTS;
        withFragmentTransaction();
        when(menuItem.getTitle()).thenReturn(title);
        // execute
        fixture.activate(mainActivity, menuItem, navigationMenu);
        // validate
        verifyFragmentTransaction();
        verify(mainActivity).setCurrentNavigationMenu(navigationMenu);
        verify(mainActivity).setTitle(title);
        verify(mainActivity).updateActionBar();
    }

    @Test
    public void testIsRegisteredFalse() throws Exception {
        // setup
        FragmentItem fixture = new FragmentItem(fragment, false);
        // execute & validate
        assertFalse(fixture.isRegistered());
    }

    @Test
    public void testIsRegisteredTrue() throws Exception {
        // setup
        FragmentItem fixture = new FragmentItem(fragment, true);
        // execute & validate
        assertTrue(fixture.isRegistered());
    }

    private void withFragmentTransaction() {
        when(mainActivity.getSupportFragmentManager()).thenReturn(fragmentManager);
        when(fragmentManager.beginTransaction()).thenReturn(fragmentTransaction);
        when(fragmentTransaction.replace(R.id.main_fragment, fragment)).thenReturn(fragmentTransaction);
    }

    private void verifyFragmentTransaction() {
        verify(mainActivity).getSupportFragmentManager();
        verify(fragmentManager).beginTransaction();
        verify(fragmentTransaction).replace(R.id.main_fragment, fragment);
        verify(fragmentTransaction).commit();
    }
}