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

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes

/**
 * Created on 10/08/2019 at 18:56
 */
object Util {
    /**
     * Converts the integer value representing a pixel unit into DP unit
     */
    val Int.dp: Float
        get() = this / Resources.getSystem().displayMetrics.density

    /**
     * Converts the integer value representing a DP unit into pixel unit
     */
    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    /**
     * Resolves the given attribute and returns its value
     */
    fun Context.attr(@AttrRes attrRes: Int): Int {
        return TypedValue().apply {
            theme.resolveAttribute(attrRes, this, true)
        }.data
    }

    /**
     * Clone of [apply] which calls the [BaseEditText.showActions] method
     */
    fun BaseEditText.set(block: BaseEditText.() -> Unit) {
        block()

        showActions()
    }

    /**
     * Similar to [String.replace] but it works with any type, this is to avoid the call to [Any.toString]
     */
    fun String.replaceAny(old: String, replacement: Any): String{
        return this.replace(old,replacement.toString())
    }
}