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

public class Info {
    private final List<Details> parents = new ArrayList<>();

    public List<Details> getParents() {
        return parents;
    }
    public Details getParent(int index) {
        return parents.get(index);
    }

    public List<Details> getChildren(int index) {
        return parents.get(index).getChildren();
    }
    public Details getChild(int indexParent, int indexChild) {
        return parents.get(indexParent).getChild(indexChild);
    }

    public void add(Details details) {
        if (parents.contains(details)) {
            int index = parents.indexOf(details);
            Details parent = parents.get(index);
            if (parent.getLevel() >= details.getLevel()) {
                parent.addChild(details);
            } else {
                details.addChildren(parent.getChildren());
                parent.clearChildren();
                details.addChild(parent);
                parents.add(index, details);
            }
        } else {
            parents.add(details);
        }
        Collections.sort(parents);
    }

}
