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
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewTreeObserver
import com.maxpilotto.actionedittext.Util.attr
import kotlinx.android.synthetic.main.action_edit_text.view.*

/**
 * Upgraded version of the EditText that supports different types of actions and text validation
 */
class ActionEditText : BaseEditText {
    override var label: String?
        set(value) {
            _label.text = value
            field = value
        }

    override var error: String?
        set(value) {
            showError(value != null, _edit.isFocused)

            _error.text = value
            field = value
        }

    override var text: String?
        set(value) {
            _edit.setText(value)
            field = value
        }
        get() {
            return _edit.text.toString()
        }

    override var hint: String?
        set(value) {
            _edit.hint = value
            field = value
        }

    override var editable: Boolean
        set(value) {
            _edit.isClickable = value
            _edit.isFocusable = value
            _edit.isFocusableInTouchMode = value
            _edit.isCursorVisible = value

            field = value
        }

    override var inputType: Int
        set(value) {
            _edit.inputType = value
            text?.let {
                _edit.setSelection(it.length)
            }
            _edit.typeface = Typeface.DEFAULT

            field = value
        }

    override var textAlign: Int
        set(value) {
            _edit.textAlignment = value
            field = value
        }

    override var textGravity: Int
        set(value) {
            _edit.gravity = value
            field = value
        }

    override var textValidator: TextValidator? = null
        set(value) {
            value?.let {
                if (it.onPostValidate == null) {
                    it.onPostValidate = { _, errors, _ ->
                        if (errors.isNullOrEmpty()) {
                            error = null
                        } else {
                            error = errors.first().toString()
                        }
                    }
                }

                _edit.addTextChangedListener(value)
            }

            field = value
        }

    override var maxLength: Int
        set(value) {
            if (value > 0) {
                _edit.filters = arrayOf(InputFilter.LengthFilter(value))
            }

            field = value
        }

    override var background: Int
        set(value) {
            _edit.backgroundTintList = ColorStateList.valueOf(value)
            field = value
        }

    override var errorColor: Int
        set(value) {
            _error.setTextColor(value)
            field = value
        }

    override var labelColor: Int
        set(value) {
            _label.setTextColor(value)
            field = value
        }

    override var textColor: Int
        set(value) {
            _edit.setTextColor(value)
            field = value
        }

    override var hintColor: Int
        set(value) {
            _edit.setHintTextColor(value)
            field = value
        }

    override var textWatcher: TextWatcher? = null
        set(value) {
            _edit.addTextChangedListener(value)
            field = value
        }

    override var textSize: Int
        set(value) {
            _edit.setTextSize(TypedValue.COMPLEX_UNIT_PX, value.toFloat())
            field = value
        }

    override var errorSize: Int
        set(value) {
            _error.setTextSize(TypedValue.COMPLEX_UNIT_PX, value.toFloat())
            field = value
        }

    override var labelSize: Int
        set(value) {
            _label.setTextSize(TypedValue.COMPLEX_UNIT_PX, value.toFloat())
            field = value
        }

    override var errorAlign: Int
        set(value) {
            _error.textAlignment = value
            field = value
        }

    override var errorGravity: Int
        set(value) {
            _error.gravity = value
            field = value
        }

    override var labelAlign: Int
        set(value) {
            _label.textAlignment = value
            field = value
        }

    override var labelGravity: Int
        set(value) {
            _label.gravity = value
            field = value
        }

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null
    ) : super(context, attrs) {
        inflate(context, R.layout.action_edit_text, this)

        with(context.obtainStyledAttributes(attrs, R.styleable.ActionEditText)) {
            label = getString(R.styleable.ActionEditText_aed_label)
            labelColor = getColor(R.styleable.ActionEditText_aed_labelColor, context.attr(R.attr.colorPrimary))
            labelSize = getDimensionPixelSize(R.styleable.ActionEditText_aed_labelSize, Default.TEXT_SIZE_SMALL)
            text = getString(R.styleable.ActionEditText_aed_text)
            textColor = getColor(R.styleable.ActionEditText_aed_textColor, Default.TEXT_COLOR)
            textSize = getDimensionPixelSize(R.styleable.ActionEditText_aed_textSize, Default.TEXT_SIZE_NORMAL)
            hint = getString(R.styleable.ActionEditText_aed_hint)
            hintColor = getColor(R.styleable.ActionEditText_aed_hintColor, Default.BACKGROUND)
            error = getString(R.styleable.ActionEditText_aed_error)
            errorColor = getColor(R.styleable.ActionEditText_aed_textColor, Default.ERROR_COLOR)
            errorSize = getDimensionPixelSize(R.styleable.ActionEditText_aed_errorSize, Default.TEXT_SIZE_SMALL)
            editable = getBoolean(R.styleable.ActionEditText_aed_editable, true)
            inputType = getInt(R.styleable.ActionEditText_aed_inputType, InputType.TEXT)
            textGravity = getInt(R.styleable.ActionEditText_aed_textGravity, Gravity.NO_GRAVITY)
            textAlign = getInt(R.styleable.ActionEditText_aed_textAlign, Gravity.NO_GRAVITY)
            maxLength = getInt(R.styleable.ActionEditText_aed_maxLength, 0)
            backgroundFocused = getColor(R.styleable.ActionEditText_aed_focusedLineColor, backgroundFocused)
            backgroundNormal = getColor(R.styleable.ActionEditText_aed_normalLineColor, backgroundNormal)
            tintAllOnError = getBoolean(R.styleable.ActionEditText_aed_tintAllOnError, tintAllOnError)
            labelGravity = getInt(R.styleable.ActionEditText_aed_labelGravity, Gravity.NO_GRAVITY)
            labelAlign = getInt(R.styleable.ActionEditText_aed_labelAlign, Gravity.NO_GRAVITY)
            errorAlign = getInt(R.styleable.ActionEditText_aed_errorAlign, Gravity.NO_GRAVITY)
            errorGravity = getInt(R.styleable.ActionEditText_aed_errorGravity, Gravity.NO_GRAVITY)
            setErrorEnabled(getBoolean(R.styleable.ActionEditText_aed_errorEnabled, true))
            setLabelEnabled(getBoolean(R.styleable.ActionEditText_aed_labelEnabled, true))

            recycle()
        }

        _edit.setOnFocusChangeListener { _, focused ->
            if (textValidator != null) {
                showError(textValidator!!.hasErrors(), focused)
            } else {
                showError(false, focused)
            }
        }

        background = backgroundNormal
    }

    override fun removeLast() {
        super.removeLast()

        text?.let {
            _edit.setSelection(it.length)
        }
    }

    override fun showActions() {
        var padding = 0
        var calculated = 0
        val updatePadding: (Int) -> Unit = { pad ->
            calculated++
            padding += pad

            if (calculated == actions.size) {
                _edit.setPadding(
                    Default.ACTION_MARGIN_START,
                    _edit.paddingTop,
                    padding,
                    _edit.paddingBottom
                )
            }
        }

        _actions.removeAllViews()

        for (a in actions) {
            _actions.addView(a.actionView)

            if (a.fixedWidth != -1) {
                updatePadding(a.fixedWidth)
            } else {
                a.actionView?.viewTreeObserver?.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        a.actionView?.let {
                            it.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                            updatePadding((it.width * a.extraPadding + Default.ACTION_MARGIN_START).toInt())
                        }
                    }
                })
            }
        }
    }

    override fun showError(hasError: Boolean, focused: Boolean) {
        if (_error.visibility != GONE) {
            if (tintAllOnError) {
                for (action in actions) {
                    action.tintAll(hasError)
                }

                _label.setTextColor(if (hasError) errorColor else labelColor)
            }

            if (hasError) {
                _error.visibility = VISIBLE
                background = errorColor
            } else {
                _error.visibility = INVISIBLE
                background = if (focused) backgroundFocused else backgroundNormal
            }
        }
    }

    override fun setLabelEnabled(enabled: Boolean) {
        _label.visibility = if (enabled) VISIBLE else GONE
    }

    override fun setErrorEnabled(enabled: Boolean) {
        _error.visibility = if (enabled) VISIBLE else GONE
    }

    override fun setSelection(index: Int) {
        _edit.setSelection(index)
    }

    override fun setText(strRes: Int) {
        text = context.getString(strRes)
    }

    override fun setLabel(strRes: Int) {
        label = context.getString(strRes)
    }

    override fun setError(strRes: Int) {
        error = context.getString(strRes)
    }

    override fun setHint(strRes: Int) {
        hint = context.getString(strRes)
    }

    override fun setTextColorRes(colorRes: Int) {
        textColor = context.resources.getColor(colorRes)
    }

    override fun setHintColorRes(colorRes: Int) {
        hintColor = context.resources.getColor(colorRes)
    }

    override fun setErrorColorRes(colorRes: Int) {
        errorColor = context.resources.getColor(colorRes)
    }

    override fun setLabelColorRes(colorRes: Int) {
        labelColor = context.resources.getColor(colorRes)
    }
}