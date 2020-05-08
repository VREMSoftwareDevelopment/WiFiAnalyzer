/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.view.View;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BottomNavOffTest {

    @Mock
    private MainActivity mainActivity;
    @Mock
    private View view;

    private BottomNavOff fixture;

    @Before
    public void setUp() {
        fixture = new BottomNavOff();
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mainActivity);
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testApply() {
        // setup
        when(mainActivity.findViewById(R.id.nav_bottom)).thenReturn(view);
        // execute
        fixture.apply(mainActivity);
        // validate
        verify(mainActivity).findViewById(R.id.nav_bottom);
        verify(view).setVisibility(View.GONE);
    }

}