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
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.maxpilotto.actionedittext.Default
import com.maxpilotto.actionedittext.R
import com.maxpilotto.actionedittext.Util.attr

/**
 * Created on 19/08/2019 at 17:41
 */
open class Toggle : Action<ImageView> {
    /**
     * Whether or not there's an error,
     * this is stored here to resolve all the problems related to the tint and icon resource changes
     */
    private var hasError = false

    /**
     * Icon resource for when the Toggle is checked
     */
    @DrawableRes
    var checkedRes: Int
        set(value) {
            field = value

            refresh()
        }

    /**
     * Tint for when the Toggle is checked
     */
    @ColorInt
    var checkedTint: Int
        set(value) {
            field = value

            refresh()
        }

    /**
     * Icon resource for when the Toggle is not checked
     */
    @DrawableRes
    var uncheckedRes: Int
        set(value) {
            field = value

            refresh()
        }

    /**
     * Tint for when the Toggle is not checked
     */
    @ColorInt
    var uncheckedTint: Int
        set(value) {
            field = value

            refresh()
        }

    /**
     * Icon resource for both checked and unchecked states
     */
    @DrawableRes
    var icon: Int
        set(value) {
            uncheckedRes = value
            checkedRes = value

            field = value
        }

    /**
     * Callback invoked when the CheckBox is toggled
     */
    var onToggle: (Boolean) -> Unit

    /**
     * Value of the Toggle
     */
    var checked: Boolean = false
        set(value) {
            actionView?.let {
                it.setImageResource(if (value) checkedRes else uncheckedRes)

                if (hasError) {
                    it.setColorFilter(errorColor, PorterDuff.Mode.SRC_IN)
                } else {
                    it.setColorFilter(if (value) checkedTint else uncheckedTint, PorterDuff.Mode.SRC_IN)
                }
            }

            field = value
        }

    override var onClick: (View) -> Unit = {}
        get() = super.onClick
        set(value) {
            field = value

            actionView?.setOnClickListener {
                checked = !checked
                onToggle(checked)
                onClick(it)
            }
        }

    @JvmOverloads
    constructor(context: Context, callback: (Boolean) -> Unit = {}) : super(context) {
        this.onToggle = callback
        this.actionView = ImageView(context).apply {
            layoutParams = defaultLayoutParams
        }

        this.size = Default.ACTION_SIZE
        this.icon = 0
        this.checkedRes = 0
        this.uncheckedRes = 0
        this.checked = false
        this.onClick = {}
        this.ripple = Ripple.CIRCULAR
        this.fixedWidth = (1.15 * size + Default.ACTION_MARGIN_START).toInt()

        with(context.attr(R.attr.colorPrimary)) {
            checkedTint = this
            uncheckedTint = this
        }
    }

    /**
     * Refresh the icon's tint and resource
     */
    private fun refresh() {
        checked = checked   // This is not an error, it's made on purpose to re-set the tints and icons
    }

    override fun tintAll(applyErrorTint: Boolean) {
        hasError = applyErrorTint

        refresh()
    }

    /**
     * Toggles the toggle
     */
    fun toggle() {
        checked = !checked
    }
}