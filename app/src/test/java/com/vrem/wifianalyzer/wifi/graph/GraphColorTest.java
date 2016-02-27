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

package com.vrem.wifianalyzer.wifi.graph;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GraphColorTest {

    @Test
    public void testGraphColors() throws Exception {
        assertEquals(20, GraphColor.values().length);
    }

    @Test
    public void testFindColorWrapWhenMaxIsReached() throws Exception {
        for (int i = 0; i < GraphColor.values().length; i++) {
            if (i < GraphColor.maxColor) {
                assertEquals(GraphColor.values()[i], GraphColor.findColor());
            } else {
                assertEquals(GraphColor.values()[i - GraphColor.maxColor], GraphColor.findColor());
            }
        }
    }

    @Test
    public void testGetPrimary() throws Exception {
        assertEquals(0xFF2196F3, GraphColor.BLUE.getPrimary());
    }

    @Test
    public void testGetBackground() throws Exception {
        assertEquals(0x332196F3, GraphColor.BLUE.getBackground());
    }
}