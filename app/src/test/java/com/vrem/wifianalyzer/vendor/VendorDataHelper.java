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
package com.vrem.wifianalyzer.vendor;

class VendorDataHelper {
    static final String MAC_IN_RANGE1 = "00:23:AB:8C:DF:10";
    static final String MAC_IN_RANGE2 = "00:23:AB:00:DF:1C";

    static final String MAC_START_NOT_IN_RANGE = "00:23:AA:FF:FF:FF";
    static final String MAC_END_NOT_IN_RANGE = "00:23:AC:00:00:00";

    static final String MAC_START = "0023AB000000";
    static final String MAC_END = "0023ABFFFFFF";
    static final String COMPANY_NAME = "CISCO SYSTEMS, INC.";

    static VendorData make() {
        return new VendorData(MAC_START, MAC_END, COMPANY_NAME);
    }

}