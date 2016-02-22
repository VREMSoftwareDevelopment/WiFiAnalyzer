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

package com.vrem.wifianalyzer.vendor.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MacAddressTest {
    @Test
    public void testClean() throws Exception {
        assertEquals("34AF", MacAddress.clean("34aF"));
        assertEquals("34AF0B", MacAddress.clean("34aF0B"));
        assertEquals("34AA0B", MacAddress.clean("34:aa:0b"));
        assertEquals("34AC0B", MacAddress.clean(MacAddress.clean("34:ac:0B:A0")));
    }
}