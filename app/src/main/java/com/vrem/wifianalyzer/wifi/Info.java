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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Info {
    private final Map<String,Details> parents = new TreeMap<>();

    public List<Details> getParents() {
        List<Details> results = new ArrayList<>(this.parents.values());
        Collections.sort(results);
        return results;
    }
    public Details getParent(int index) {
        return getParents().get(index);
    }

    public List<Details> getChildren(int index) {
        return getParents().get(index).getChildren();
    }
    public Details getChild(int indexParent, int indexChild) {
        return getParents().get(indexParent).getChild(indexChild);
    }

    public void add(Details details) {
        Details parent = this.parents.get(details.getSSID());
        if (parent == null) {
            this.parents.put(details.getSSID(), details);
            return;
        }
        if (parent.getLevel() >= details.getLevel()) {
            parent.addChild(details);
            return;
        }
        details.addChildren(parent.getChildren());
        parent.clearChildren();
        details.addChild(parent);
        parents.put(details.getSSID(), details);
    }

}
