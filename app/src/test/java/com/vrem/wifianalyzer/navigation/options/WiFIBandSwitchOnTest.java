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

package com.vrem.wifianalyzer.navigation.options;

import android.view.Menu;
import android.view.MenuItem;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.menu.OptionMenu;

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
public class WiFIBandSwitchOnTest {

	@Mock
	private MainActivity mainActivity;
	@Mock
	private OptionMenu optionMenu;
	@Mock
	private Menu menu;
	@Mock
	private MenuItem menuItem;

	private WiFIBandSwitchOn fixture;

	@Before
	public void setUp() {
		fixture = new WiFIBandSwitchOn();
	}

	@After
	public void tearDown() {
		MainContextHelper.INSTANCE.restore();
	}


	@Test
	public void testApplySetMenuItemVisibleTrue() throws Exception {
		// setup
		withMenuItem();
		// execute
		fixture.apply(mainActivity);
		// validate
		verifyMenuItem();
		verify(menuItem).setVisible(true);
	}

	private void verifyMenuItem() {
		verify(mainActivity).getOptionMenu();
		verify(optionMenu).getMenu();
		verify(menu).findItem(R.id.action_change_band);
	}

	private void withMenuItem() {
		when(mainActivity.getOptionMenu()).thenReturn(optionMenu);
		when(optionMenu.getMenu()).thenReturn(menu);
		when(menu.findItem(R.id.action_change_band)).thenReturn(menuItem);
	}

	@Test
	public void testApplyWithNoMenuDoesNotSetVisibleTrue() throws Exception {
		// setup
		when(mainActivity.getOptionMenu()).thenReturn(optionMenu);
		when(optionMenu.getMenu()).thenReturn(null);
		// execute
		fixture.apply(mainActivity);
		// validate
		verify(mainActivity).getOptionMenu();
		verify(optionMenu).getMenu();
		verify(menu, never()).findItem(R.id.action_change_band);
		verify(menuItem, never()).setVisible(true);
	}
}
