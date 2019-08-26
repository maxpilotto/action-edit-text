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
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.maxpilotto.actionedittext.Util.attr
import com.maxpilotto.actionedittext.actions.Action

typealias ActionList = MutableList<Action<*>>

/**
 * Base ActionEditText class
 *
 * Created on 13/08/2019 at 15:30
 */
abstract class BaseEditText : LinearLayout {    //TODO setSelection, copyAll, clear, removeFromSelection
    /**
     * List of the actions that were added to this ActionEditText
     */
    protected val actions: ActionList = mutableListOf()

    /**
     * Text value of the input field, this is the final value and not an hint/placeholder
     */
    abstract var text: String?

    /**
     * Color of the text
     */
    @get:ColorInt
    abstract var textColor: Int

    /**
     * Size of the text, in pixels
     */
    abstract var textSize: Int

    /**
     * Hint/placeholder for the input field
     */
    abstract var hint: String?

    /**
     * Color of the hint
     */
    @get:ColorInt
    abstract var hintColor: Int

    /**
     * Text of the error view, set to null to completely hide it
     */
    abstract var error: String?

    /**
     * Color of the error message and EditText underline when there's an error
     */
    @get:ColorInt
    abstract var errorColor: Int

    /**
     * Size of the error text, in pixels
     */
    abstract var errorSize: Int

    /**
     * Text of the label, set to null to completely hide it
     */
    abstract var label: String?

    /**
     * Color of the label
     */
    @get:ColorInt
    abstract var labelColor: Int

    /**
     * Size of the label text, in pixels
     */
    abstract var labelSize: Int

    /**
     * Tells whether or not this EditText is editable or not
     */
    abstract var editable: Boolean

    /**
     * Input type of the edit text, can be from [InputType] or Android's [android.text.InputType]
     */
    abstract var inputType: Int

    /**
     * Alignment of the text inside the EditText
     */
    abstract var textAlign: Int

    /**
     * Gravity of the text inside of the EditText, this might be an alternative to [textAlign]
     */
    abstract var textGravity: Int

    /**
     * Text validator for this EditText
     */
    abstract var textValidator: TextValidator?

    /**
     * Text watcher, this is the Android's TextWatcher, a alternative to [textValidator]
     */
    abstract var textWatcher: TextWatcher?

    /**
     * Max number of characters that can be inserted
     */
    abstract var maxLength: Int

    /**
     * Current color of the line under the EditText, this is used to set the color
     */
    @get:ColorInt
    abstract var background: Int

    /**
     * Whether or not all components (label, error, underline, actions) should be tinted with the error color when there's an error; Enabled by default
     */
    var tintAllOnError: Boolean

    /**
     * Color of the line under the EditText when it is focused, this will only be applied when the view is focused,
     * if you need to apply it instantly used [background]
     */
    @get:ColorInt
    var backgroundFocused: Int

    /**
     * Color of the line under the EditText is not focused, this will only be applied when the view is not focused,
     * if you need to apply it instantly used [background]
     */
    @get:ColorInt
    var backgroundNormal: Int

    /**
     * EditText's current error, this won't be displayed but will be available through the [TextValidator.onValidate] callback
     */
    var errors: List<Error>?
        private set
        get() {
            if (textValidator != null) {
                return textValidator!!.errors
            } else {
                return null
            }
        }

    init {
        errors = null
        tintAllOnError = true
        backgroundFocused = context.attr(R.attr.colorPrimary)
        backgroundNormal = Default.BACKGROUND
    }

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr)

    /**
     * Refreshes the layout, this should be called after all actions have been added
     */
    abstract fun showActions()

    /**
     * Shows or hides the error colors, this will apply the errorTint to all components only if [tintAllOnError] is set to true
     */
    abstract fun showError(hasError: Boolean, focused: Boolean)

    /**
     * Shows/Hides the label view, if the view is hidden the total view height will be reduced
     */
    abstract fun setLabelEnabled(enabled: Boolean)

    /**
     * Shows/Hides the error view, if the view is hidden the total view height will be reduced
     */
    abstract fun setErrorEnabled(enabled: Boolean)

    /**
     * Appends the given text
     */
    open fun append(charSeq: CharSequence) {
        text += charSeq
    }

    /**
     * Removes the last character
     */
    open fun removeLast() {
        text?.let {
            if (it.isNotEmpty()) {
                text = it.substring(0, it.length - 1)
            }
        }
    }

    /**
     * Returns the action at the given index, actions counting starts from right and goes to left; First action will the one at most right
     */
    fun actionAt(index: Int): Action<*>? {
        if (actions.size <= index) {
            return null
        }

        return actions[index]
    }

    /**
     * Adds the given Action, actions will be placed from right to left
     */
    fun action(action: Action<*>) {
        actions.add(action)
    }

    /**
     * Returns whether or not the EditText is empty
     */
    fun isEmpty(): Boolean {
        return text.isNullOrEmpty()
    }

    /**
     * Manually calls the internal TextValidator so it looks for errors
     */
    fun checkErrors() {
        textValidator?.onTextChanged(text,0,0,0)
    }

    /**
     * Returns whether or not this EditText has errors
     */
    fun hasErrors(): Boolean {
        checkErrors()

        return !errors.isNullOrEmpty()
    }

    /**
     * Sets the [inputType] value to [InputType.PASSWORD] or [InputType.PASSWORD_VISIBLE] based on the given value
     */
    fun setPassword(hide: Boolean) {
        inputType = if (hide) {
            InputType.PASSWORD
        } else {
            InputType.PASSWORD_VISIBLE
        }
    }

    /**
     * Sets the [inputType] value to [InputType.NUMERIC] or [InputType.NUMERIC_VISIBLE] based on the given value
     */
    fun setNumeric(hide: Boolean) {
        inputType = if (hide) {
            InputType.NUMERIC
        } else {
            InputType.NUMERIC_VISIBLE
        }
    }
}