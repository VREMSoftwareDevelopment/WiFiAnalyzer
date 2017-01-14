/*
 * WiFi Analyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.view.View;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NextPrevNavigationTest {

    @Mock
    private MainActivity mainActivity;
    @Mock
    private View viewNext;
    @Mock
    private View viewPrev;

    private NextPrevNavigation fixture;

    @Test
    public void testApplySetsVisibilityOption() throws Exception {
        // setup
        int visibility = View.INVISIBLE;
        fixture = new NextPrevNavigation(visibility);
        when(mainActivity.findViewById(R.id.action_next)).thenReturn(viewNext);
        when(mainActivity.findViewById(R.id.action_prev)).thenReturn(viewPrev);
        // execute
        fixture.apply(mainActivity);
        // validate
        verify(mainActivity).findViewById(R.id.action_next);
        verify(mainActivity).findViewById(R.id.action_prev);
        verify(viewNext).setVisibility(visibility);
        verify(viewPrev).setVisibility(visibility);
    }

}