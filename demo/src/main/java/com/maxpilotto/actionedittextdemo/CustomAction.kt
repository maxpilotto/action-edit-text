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

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.maxpilotto.actionedittext.Default
import com.maxpilotto.actionedittext.actions.Action

/**
 * Created on 26/08/2019 at 23:01
 */
class CustomAction : Action<TextView>{
    var firstText = ""      // Text when the view is checked
        set(value) {
            if (!checked){
                actionView?.text = value
            }
            field = value
        }

    var secondText = ""     // Text when the view is not checked
        set(value) {
            if (checked){
                actionView?.text = value
            }
            field = value
        }

    var textColor: Int // Text color of the TextView
        set(value) {
            actionView?.setTextColor(value)
            field = value
        }

    var checked = false     // Whether or not the view is checked

    override var onClick: (View) -> Unit = {}

    constructor(context: Context) : super(context){
        actionView = TextView(context).apply{    // Create a TextView which will be the view of our Action
            layoutParams = defaultLayoutParams   // Set the default layout params (size,size) + 10dp margin start

            setOnClickListener{
                checked = !checked          // Toggle the value when the user presses the View

                text = if (checked) secondText else firstText

                onClick(it)
            }
        }

        textColor = Default.TEXT_COLOR                  // Set the default text color
        size = LinearLayout.LayoutParams.WRAP_CONTENT   // Set the width/height to WRAP_CONTENT
        ripple = Ripple.CIRCULAR                        // Set the default ripple effect
        fixedWidth = -1                                 // This will tell the ActionEditText to calculate the width
    }

    override fun tintAll(applyErrorTint: Boolean) {
        actionView?.let{
            if (applyErrorTint){
                it.setTextColor(errorColor)
            }else{
                it.setTextColor(textColor)
            }

        }
    }
}