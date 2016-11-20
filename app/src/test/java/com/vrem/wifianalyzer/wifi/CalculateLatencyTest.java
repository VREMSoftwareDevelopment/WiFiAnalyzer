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

package com.vrem.wifianalyzer.wifi;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CalculateLatencyTest {
    private static final String GATEWAY_ADDRESS = "192.168.1.1";

    @Mock
    private ConnectionView connectionView;

    private CalculateLatency fixture;
    private boolean isReachable;

    @Before
    public void setUp() throws Exception {
        fixture = new CalculateLatency(connectionView) {
            @Override
            boolean isReachable(String gatewayAddress) {
                return isReachable;
            }
        };
    }

    @Test
    public void testDoInBackgroundWithReachable() throws Exception {
        // setup
        isReachable = true;
        // execute
        Long actual = fixture.doInBackground(GATEWAY_ADDRESS);
        // validate
        assertFalse(CalculateLatency.INVALID.equals(actual));
    }

    @Test
    public void testDoInBackgroundWithUnreachable() throws Exception {
        // setup
        isReachable = false;
        // execute
        Long actual = fixture.doInBackground(GATEWAY_ADDRESS);
        // validate
        assertEquals(CalculateLatency.INVALID, actual);
    }

    @Test
    public void testDoInBackgroundWithNull() throws Exception {
        // execute
        Long actual = fixture.doInBackground((String) null);
        // validate
        assertEquals(CalculateLatency.INVALID, actual);
    }

    @Test
    public void testDoInBackgroundWithEmpty() throws Exception {
        // execute
        Long actual = fixture.doInBackground(StringUtils.EMPTY);
        // validate
        assertEquals(CalculateLatency.INVALID, actual);
    }

    @Test
    public void testOnPostExecute() throws Exception {
        // setup
        Long result = 11L;
        // execute
        fixture.onPostExecute(result);
        // validate
        verify(connectionView).updateLatency(result);
    }

    @Test
    public void testOnPostExecuteWithInvalidTime() throws Exception {
        // setup
        // execute
        fixture.onPostExecute(CalculateLatency.INVALID);
        // validate
        verify(connectionView, never()).updateLatency(anyLong());
    }

}