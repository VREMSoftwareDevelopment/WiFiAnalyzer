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

import android.support.annotation.NonNull;
import android.util.Log;

public class Logger {

    public void error(@NonNull Object object, @NonNull String msg, @NonNull Throwable e) {
        Log.e(object.getClass().getSimpleName() + " ", msg, e);
    }

    public void info(@NonNull Object object, @NonNull String msg) {
        Log.i(object.getClass().getSimpleName() + " ", msg);
    }

}
