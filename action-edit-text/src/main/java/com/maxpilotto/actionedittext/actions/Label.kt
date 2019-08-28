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
package com.maxpilotto.actionedittext.actions

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.maxpilotto.actionedittext.Default

/**
 * Label that can be added at the end of an EditText
 *
 * Created on 13/08/2019 at 17:17
 */
open class Label : Action<TextView> {
    /**
     * Label's text
     */
    var text: String
        set(value) {
            actionView?.text = value
            field = value
        }

    /**
     * Text size in pixels
     */
    var textSize: Int
        set(value) {
            actionView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, value.toFloat())
            field = value
        }

    /**
     * Text color
     */
    @ColorInt
    var textColor: Int
        set(value) {
            actionView?.setTextColor(value)
            field = value
        }

    @JvmOverloads
    constructor(context: Context, text: String = "") : super(context) {
        this.actionView = TextView(context).apply {
            layoutParams = defaultLayoutParams
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }

        this.text = text
        this.size = LinearLayout.LayoutParams.WRAP_CONTENT
        this.textColor = Default.TEXT_COLOR
        this.textSize = Default.ACTION_TEXT_SIZE
        this.ripple = Ripple.CIRCULAR
        this.extraPadding = 1.2F
        this.fixedWidth = -1
    }

    override fun tintAll(applyErrorTint: Boolean) {
        actionView?.setTextColor(if (applyErrorTint) errorColor else textColor)
    }

    /**
     * Sets the text from a text resource
     */
    fun setTextRes(@StringRes strRes: Int){
        text = context.getString(strRes)
    }

    /**
     * Sets the text color from a color resource
     */
    fun setTextColorRes(@ColorRes colorRes: Int){
        textColor = context.resources.getColor(colorRes)
    }
}