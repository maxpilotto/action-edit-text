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
package com.maxpilotto.actionedittext

import android.graphics.Color
import com.maxpilotto.actionedittext.Util.px

/**
 * Default values used inside the library
 *
 * Created on 20/08/2019 at 23:38
 */
class Default {
    companion object {
        /**
         * Default action size, in pixels
         */
        @JvmStatic
        var ACTION_SIZE = 24.px

        /**
         * Width and height of the checkbox, this value is taken from the [docs](https://material.io/design/components/selection-controls.html#specs)
         *
         * The value is increased by a little value to give the view a better look
         */
        @JvmStatic
        var CHECKBOX_SIZE = (24 + 2).px

        /**
         * Default action text size, in pixels
         */
        @JvmStatic
        var ACTION_TEXT_SIZE = 12.px

        /**
         * Default margin, in pixels, added to each Action
         */
        @JvmStatic
        var ACTION_MARGIN_START = 10.px

        /**
         * Default EditText text color
         */
        @JvmStatic
        var TEXT_COLOR = Color.parseColor("#000000")

        /**
         * Default EditText error color
         */
        @JvmStatic
        var ERROR_COLOR = Color.parseColor("#f44336")

        /**
         * Default EditText background color
         */
        @JvmStatic
        var BACKGROUND = Color.parseColor("#aaaaaa")

        /**
         * Default EditText text size, for small texts like the error or the label
         */
        @JvmStatic
        var TEXT_SIZE_SMALL = 12.px

        /**
         * Default EditText text size, for normal texts
         */
        @JvmStatic
        var TEXT_SIZE_NORMAL = 14.px
    }
}