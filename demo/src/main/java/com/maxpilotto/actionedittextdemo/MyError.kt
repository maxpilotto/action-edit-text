/*
 * Copyright 2018 Max Pilotto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.maxpilotto.actionedittextdemo

import com.maxpilotto.actionedittext.Error

/**
 * Created on 26/08/2019 at 20:42
 */
class MyErrors{
    companion object{
        val NO_SPACES = Error("Text must have spaces")
        val NO_DATE = Error("Inserted text must be a date")
    }
}