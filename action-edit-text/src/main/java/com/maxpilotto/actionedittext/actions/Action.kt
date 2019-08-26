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
import android.widget.LinearLayout.LayoutParams
import com.maxpilotto.actionedittext.Default

/**
 * Abstract class that represents an Action, which is an interactive component ([TextView], [ImageView], [Checkbox] ...)
 * that has special properties and can be added to an [com.maxpilotto.actionedittext.BaseEditText]
 *
 * Created on 10/08/2019 at 17:59
 */
abstract class Action<T : View>(protected val context: Context) {
    enum class Ripple constructor(val id: Int) {
        NONE(0),
        NORMAL(android.R.attr.selectableItemBackground),
        CIRCULAR(android.R.attr.selectableItemBackgroundBorderless)
    }

    /**
     * Extra padding that is multiplied when the [actionView]'s width is calculated
     */
    var extraPadding: Float = 1F
        protected set

    /**
     * Callback invoked when the view is clicked
     */
    open var onClick: (View) -> Unit = {}
        set(value) {
            actionView?.setOnClickListener(value)
            field = value
        }

    /**
     * Callback invoked when the view is long clicked
     */
    open var onLongClick: (View) -> Boolean = { false }
        set(value) {
            actionView?.setOnLongClickListener(value)
            field = value
        }

    /**
     * Color used if [com.maxpilotto.actionedittext.BaseEditText.tintAllOnError] is enabled and there's an error
     *
     * By default this color will be the same as the BaseEditText this Action is added to
     */
    var errorColor: Int = Default.ERROR_COLOR

    /**
     * Width and height of this Action, this can also be [LayoutParams.WRAP_CONTENT] or [LayoutParams.MATCH_PARENT]
     */
    var size: Int = LayoutParams.WRAP_CONTENT
        set(value) {
            actionView?.let {
                it.layoutParams.height = value
                it.layoutParams.width = value
            }

            field = value
        }

    /**
     * Ripple effect used for this Action, must be one of [Ripple]
     */
    var ripple: Ripple = Ripple.CIRCULAR
        set(value) {
            if (value != Ripple.NONE) {
                with(TypedValue()) {
                    context.theme.resolveAttribute(value.id, this, true)
                    actionView?.setBackgroundResource(resourceId)
                }
            } else {
                actionView?.setBackgroundResource(0)
            }

            field = value
        }

    /**
     * Width of this Action's view
     *
     * This is used by the parent's ActionEditText to calculate the EditText end padding,
     * it should be set to -1 if you want the ActionEditText to take care of the measurements
     */
    var fixedWidth: Int = 0
        protected set

    /**
     * View used for this Action
     */
    var actionView: T? = null
        protected set

    /**
     * Default layout params that can be used with any Action
     */
    val defaultLayoutParams: LayoutParams
        get() {
            return LayoutParams(size, size).apply {
                marginStart = Default.ACTION_MARGIN_START
            }
        }

    /**
     * Tints all the component of this Action either with the error color or with its default color
     */
    abstract fun tintAll(applyErrorTint: Boolean)
}