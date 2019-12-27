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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.maxpilotto.actionedittext.Util.attr
import com.maxpilotto.actionedittext.actions.Action

typealias ActionList = MutableList<Action<*>>

/**
 * Base ActionEditText class
 */
abstract class BaseEditText : LinearLayout {
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
     * Alignment of the text inside the EditText
     */
    abstract var textAlign: Int

    /**
     * Gravity of the text inside of the EditText, this might be an alternative to [textAlign]
     */
    abstract var textGravity: Int

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
     * Alignment of the text inside the error
     */
    abstract var errorAlign: Int

    /**
     * Gravity of the text inside of the error, this might be an alternative to [errorAlign]
     */
    abstract var errorGravity: Int

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
     * Alignment of the text inside the label
     */
    abstract var labelAlign: Int

    /**
     * Gravity of the text inside of the label, this might be an alternative to [labelAlign]
     */
    abstract var labelGravity: Int

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
        attrs: AttributeSet? = null
    ) : super(context, attrs)

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
     * Sets the text cursor position (or selection)
     */
    abstract fun setSelection(index: Int)

    /**
     * Sets the text from a string resource
     */
    abstract fun setText(@StringRes strRes: Int)

    /**
     * Sets the label's text from a string resource
     */
    abstract fun setLabel(@StringRes strRes: Int)

    /**
     * Sets the error's text from a string resource
     */
    abstract fun setError(@StringRes strRes: Int)

    /**
     * Sets the hint's text from a string resource
     */
    abstract fun setHint(@StringRes strRes: Int)

    /**
     * Sets the text color from a color resource
     */
    abstract fun setTextColorRes(@ColorRes colorRes: Int)

    /**
     * Sets the hint's text color from a color resource
     */
    abstract fun setHintColorRes(@ColorRes colorRes: Int)

    /**
     * Sets the label's text color from a color resource
     */
    abstract fun setErrorColorRes(@ColorRes colorRes: Int)

    /**
     * Sets the label's text color from a color resource
     */
    abstract fun setLabelColorRes(@ColorRes colorRes: Int)

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
     * Copies all the text to the clipboard, it will use the current label as the clip's label/description
     */
    open fun copyAll() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)

        clipboard.setPrimaryClip(clip)
    }

    /**
     * Clears the text
     */
    fun clear() {
        text = ""
    }

    /**
     * Returns the given amount of characters at the start of the text
     */
    fun first(count: Int): CharArray? {
        return text?.let {
            if (count > it.length) {
                throw Exception("Count must be smaller or the same size of the text\'s length")
            }

            val array = CharArray(count)

            for ((i, c) in it.substring(0, count).toCharArray().withIndex()) {
                array[i] = c
            }

            return array
        }
    }

    /**
     * Returns the first character in the text
     */
    fun first(): Char? {
        return first(1)?.get(0)
    }

    /**
     * Returns the given amount of characters at the end of the text
     */
    fun last(count: Int): CharArray? {
        return text?.let {
            if (count > it.length) {
                throw Exception("Count must be smaller or the same size of the text\'s length")
            }

            val array = CharArray(count)

            for ((i, c) in it.substring(it.length - count, it.length).toCharArray().withIndex()) {
                array[i] = c
            }

            return array
        }
    }

    /**
     * Returns the last character in the text
     */
    fun last(): Char? {
        return last(1)?.get(0)
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
        textValidator?.onTextChanged(text, 0, 0, 0)
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