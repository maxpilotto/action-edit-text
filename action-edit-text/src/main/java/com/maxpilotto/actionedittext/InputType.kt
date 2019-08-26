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

import android.text.InputType

/**
 * Created on 14/08/2019 at 15:53
 */
class InputType {
    companion object {
        const val NONE = 0
        const val TEXT = InputType.TYPE_CLASS_TEXT
        const val MULTILINE = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        const val SINGLELINE = TEXT
        const val PASSWORD = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        const val PASSWORD_VISIBLE = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        const val AUTOCOMPLETE = InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
        const val NUMERIC_VISIBLE = InputType.TYPE_CLASS_NUMBER
        const val NUMERIC = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
    }
}