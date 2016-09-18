/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer;

import com.ibm.icu.impl.IllegalIcuArgumentException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LoggerTest {

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getMainActivity();
    }

    @Test
    public void testError() throws Exception {
        MainContext.INSTANCE.getLogger().error(this, "This is a error message", new IllegalIcuArgumentException("IllegalIcuArgumentException"));
    }

    @Test
    public void testInfo() throws Exception {
        MainContext.INSTANCE.getLogger().info(this, "This is a info message");
    }
}