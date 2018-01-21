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

import android.view.View;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.NavigationSwipeOnTouchListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NextPrevNavigationOnTest {
    @Mock
    private MainActivity mainActivity;
    @Mock
    private View view;

    @Test
    public void testApplySetsSwipeOnTouchListener() throws Exception {
        // setup
        NextPrevNavigationOn fixture = new NextPrevNavigationOn();
        when(mainActivity.findViewById(R.id.main_fragment_layout)).thenReturn(view);
        // execute
        fixture.apply(mainActivity);
        // validate
        verify(mainActivity).findViewById(R.id.main_fragment_layout);
        verify(view).setOnTouchListener(any(NavigationSwipeOnTouchListener.class));
    }

}