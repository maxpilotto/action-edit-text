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
import androidx.annotation.DrawableRes
import com.maxpilotto.actionedittext.Default
import com.maxpilotto.actionedittext.R
import com.maxpilotto.actionedittext.Util.attr

/**
 * Created on 13/08/2019 at 22:08
 */
open class Icon : Action<ImageView> {
    /**
     * Image resource
     */
    @DrawableRes
    var icon: Int
        set(value) {
            actionView?.setImageResource(value)
            field = value
        }

    /**
     * Tint of the icon
     */
    var tint: Int
        set(value) {
            actionView?.setColorFilter(value, PorterDuff.Mode.SRC_IN)
            field = value
        }

    @JvmOverloads
    constructor(
        context: Context,
        @DrawableRes iconRes: Int = View.NO_ID,
        callback: (View) -> Unit = {}
    ) : super(context) {
        this.actionView = ImageView(context).apply {
            layoutParams = defaultLayoutParams
        }

        this.size = Default.ACTION_SIZE
        this.icon = iconRes
        this.tint = context.attr(R.attr.colorPrimary)
        this.onClick = callback
        this.ripple = Ripple.CIRCULAR
        this.fixedWidth = (1.15 * size + Default.ACTION_MARGIN_START).toInt()
    }

    override fun tintAll(applyErrorTint: Boolean) {
        actionView?.setColorFilter(if (applyErrorTint) errorColor else tint, PorterDuff.Mode.SRC_IN)
    }
}