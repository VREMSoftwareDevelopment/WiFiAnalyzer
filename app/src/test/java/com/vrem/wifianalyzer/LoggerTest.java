/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.vrem.wifianalyzer;

import com.ibm.icu.impl.IllegalIcuArgumentException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LoggerTest {

    @Before
    public void setUp() throws Exception {
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