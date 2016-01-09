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
package com.vrem.wifianalyzer.wifi.model;

import com.vrem.wifianalyzer.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelRatingTest {

    @Mock
    private DetailsInfo detailsInfo1;
    @Mock
    private DetailsInfo detailsInfo2;
    @Mock
    private DetailsInfo detailsInfo3;
    @Mock
    private DetailsInfo detailsInfo4;
    @Mock
    private DetailsInfo detailsInfo5;

    private ChannelRating fixture;

    @Test
    public void testWhenListIsNull() throws Exception {
        // setup
        fixture = new ChannelRating(null);
        // execute & validate
        assertEquals(0, fixture.getCount());
        assertEquals(R.color.success_color, fixture.getColor());
    }

    @Test
    public void testWhenListIsEmpty() throws Exception {
        // setup
        fixture = new ChannelRating(new ArrayList<DetailsInfo>());
        // execute & validate
        assertEquals(0, fixture.getCount());
        assertEquals(R.color.success_color, fixture.getColor());
    }

    @Test
    public void testGetCount() throws Exception {
        // setup
        List<DetailsInfo> details = withDetails();
        fixture = new ChannelRating(details);
        // execute & validate
        assertEquals(details.size() - 1, fixture.getCount());
        verifyDetails();
    }

    @Test
    public void testGetColorWarning() throws Exception {
        // setup
        List<DetailsInfo> details = withDetails();
        fixture = new ChannelRating(details);
        // execute & validate
        assertEquals(R.color.warning_color, fixture.getColor());
        verifyDetails();
    }

    @Test
    public void testGetColorError() throws Exception {
        // setup
        List<DetailsInfo> details = withDetails();
        details.add(detailsInfo4);
        details.add(detailsInfo5);
        fixture = new ChannelRating(details);
        // execute & validate
        assertEquals(R.color.error_color, fixture.getColor());
        verifyDetails();
        verify(detailsInfo4, never()).isConnected();
        verify(detailsInfo5, never()).isConnected();
    }

    private void verifyDetails() {
        verify(detailsInfo1).isConnected();
        verify(detailsInfo2).isConnected();
        verify(detailsInfo3, never()).isConnected();
    }

    private List<DetailsInfo> withDetails() {
        when(detailsInfo1.isConnected()).thenReturn(false);
        when(detailsInfo2.isConnected()).thenReturn(true);
        return new ArrayList<>(Arrays.asList(new DetailsInfo[]{detailsInfo1, detailsInfo2, detailsInfo2}));
    }

}