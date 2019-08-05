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

package com.vrem.wifianalyzer.navigation.items;

import android.view.MenuItem;
import android.view.View;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.NavigationMenu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
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
    private FragmentManager fragmentManager;
    @Mock
    private FragmentTransaction fragmentTransaction;

    @Test
    public void testActivateWithStateSaved() {
        // setup
        FragmentItem fixture = new FragmentItem(fragment, true);
        String title = "title";
        NavigationMenu navigationMenu = NavigationMenu.ACCESS_POINTS;
        when(mainActivity.getSupportFragmentManager()).thenReturn(fragmentManager);
        when(fragmentManager.isStateSaved()).thenReturn(true);
        // execute
        fixture.activate(mainActivity, menuItem, navigationMenu);
        // validate
        verify(mainActivity).getSupportFragmentManager();
        verify(fragmentManager).isStateSaved();
        verifyFragmentManagerIsNotCalled();
        verifyNoChangesToMainActivity(title, navigationMenu);
    }

    @Test
    public void testActivateWithStateNotSaved() {
        // setup
        FragmentItem fixture = new FragmentItem(fragment, true);
        String title = "title";
        NavigationMenu navigationMenu = NavigationMenu.ACCESS_POINTS;
        when(mainActivity.getSupportFragmentManager()).thenReturn(fragmentManager);
        when(fragmentManager.isStateSaved()).thenReturn(false);
        withFragmentTransaction();
        when(menuItem.getTitle()).thenReturn(title);
        // execute
        fixture.activate(mainActivity, menuItem, navigationMenu);
        // validate
        verify(mainActivity).getSupportFragmentManager();
        verify(fragmentManager).isStateSaved();
        verifyFragmentManager();
        verifyMainActivityChanges(title, navigationMenu);
    }

    @Test
    public void testIsRegisteredFalse() {
        // setup
        FragmentItem fixture = new FragmentItem(fragment, false);
        // execute & validate
        assertFalse(fixture.isRegistered());
    }

    @Test
    public void testIsRegisteredTrue() {
        // setup
        FragmentItem fixture = new FragmentItem(fragment, true);
        // execute & validate
        assertTrue(fixture.isRegistered());
    }

    @Test
    public void testGetV() {
        // setup
        FragmentItem fixture = new FragmentItem(fragment, false, View.INVISIBLE);
        // execute & validate
        assertEquals(View.INVISIBLE, fixture.getVisibility());
    }

    private void withFragmentTransaction() {
        when(fragmentManager.beginTransaction()).thenReturn(fragmentTransaction);
        when(fragmentTransaction.replace(R.id.main_fragment, fragment)).thenReturn(fragmentTransaction);
    }

    private void verifyFragmentManager() {
        verify(fragmentManager).beginTransaction();
        verify(fragmentTransaction).replace(R.id.main_fragment, fragment);
        verify(fragmentTransaction).commit();
    }

    private void verifyMainActivityChanges(String title, NavigationMenu navigationMenu) {
        verify(mainActivity).setCurrentNavigationMenu(navigationMenu);
        verify(mainActivity).setTitle(title);
        verify(mainActivity).updateActionBar();
    }

    private void verifyFragmentManagerIsNotCalled() {
        verify(fragmentManager, never()).beginTransaction();
        verify(fragmentTransaction, never()).replace(R.id.main_fragment, fragment);
        verify(fragmentTransaction, never()).commit();
    }

    private void verifyNoChangesToMainActivity(String title, NavigationMenu navigationMenu) {
        verify(mainActivity, never()).setCurrentNavigationMenu(navigationMenu);
        verify(mainActivity, never()).setTitle(title);
        verify(mainActivity, never()).updateActionBar();
    }

}