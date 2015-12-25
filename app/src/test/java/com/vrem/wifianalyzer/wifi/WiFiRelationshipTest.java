/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi;

import android.net.wifi.ScanResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class WiFiRelationshipTest {
    @Mock private ScanResult scanResult;

    private DetailsInfo parent;
    private DetailsInfo child1;
    private DetailsInfo child2;
    private DetailsInfo child3;
    private WiFiRelationship fixture;

    @Before
    public void setUp() throws Exception {
        parent = makeDetailsInfo(0);
        child1 = makeDetailsInfo(1);
        child2 = makeDetailsInfo(2);
        child3 = makeDetailsInfo(3);

        fixture = new WiFiRelationship(parent);

        fixture.addChild(child3);
        fixture.addChild(child1);
        fixture.addChild(child2);
    }

    @Test
    public void testParent() throws Exception {
        // execute
        DetailsInfo actual = fixture.parent();
        // validate
        assertEquals(this.parent, actual);
        assertSame(this.parent, actual);
    }

    @Test
    public void testChild() throws Exception {
        assertEquals(child1, fixture.child(0));
        assertSame(child1, fixture.child(0));

        assertEquals(child2, fixture.child(1));
        assertSame(child2, fixture.child(1));

        assertEquals(child3, fixture.child(2));
        assertSame(child3, fixture.child(2));
    }

    @Test
    public void testChildrenCount() throws Exception {
        assertEquals(3, fixture.childrenCount());
    }

    private DetailsInfo makeDetailsInfo(int level) {
        return new DummyDetails(scanResult, "SSID-123", "BSSID-123", level);
    }
}