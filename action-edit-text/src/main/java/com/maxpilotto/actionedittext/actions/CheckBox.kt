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
import android.content.res.ColorStateList
import android.widget.CheckBox
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.widget.CompoundButtonCompat
import com.maxpilotto.actionedittext.Default
import com.maxpilotto.actionedittext.R
import com.maxpilotto.actionedittext.Util.attr

/**
 * CheckBox action, the same as the Android's CheckBox but embeddable inside an ActionEditText view
 *
 * Created on 10/08/2019 at 18:42
 */
open class CheckBox : Action<CheckBox> {
    /**
     * Tells whether or not this CheckBox is checked
     */
    var checked: Boolean = false
        set(value) {
            actionView?.isChecked = value
            field = value
        }

    /**
     * Callback invoked when the CheckBox is toggled
     */
    var onToggle: (Boolean) -> Unit

    /**
     * Color of the checkbox's tick
     */
    @ColorInt
    var tint: Int
        set(value) {
            actionView?.let {
                CompoundButtonCompat.setButtonTintList(it, ColorStateList.valueOf(value))
            }

            field = value
        }

    @JvmOverloads
    constructor(context: Context, callback: (Boolean) -> Unit = {}) : super(context) {
        this.onToggle = callback
        this.actionView = CheckBox(context).apply {
            layoutParams = defaultLayoutParams
            isChecked = checked

            setOnCheckedChangeListener { _, b ->
                onToggle(b)

                checked = b
            }
        }

        this.size = Default.CHECKBOX_SIZE
        this.tint = context.attr(R.attr.colorPrimary)
        this.ripple = Ripple.CIRCULAR
        this.fixedWidth = (size + Default.ACTION_MARGIN_START)
    }

    override fun tintAll(applyErrorTint: Boolean) {
        actionView?.let {
            val color = if (applyErrorTint) errorColor else tint

            CompoundButtonCompat.setButtonTintList(it, ColorStateList.valueOf(color))
        }
    }

    /**
     * Sets the tint color from a color resource
     */
    fun setTintRes(@ColorRes colorRes: Int){
        tint = context.resources.getColor(colorRes)
    }
}