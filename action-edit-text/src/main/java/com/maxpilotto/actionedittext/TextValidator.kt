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

import android.text.Editable
import android.text.TextWatcher

typealias PostValidateCallback = (String, MutableList<Error>, TextValidator) -> Unit
typealias PreValidateCallback = (String, MutableList<Error>) -> Unit

/**
 * Text validation utility
 *
 * Created on 14/08/2019 at 14:39
 */
open class TextValidator protected constructor() : TextWatcher,Cloneable {
    /**
     * Callback invoked when the text is being validated, this has already a value and should only change if you don't want the default errors to show up
     */
    var onPreValidate: PreValidateCallback

    /**
     * Callback invoked when the text has already been validated but you want to add some additional validation while still using the default errors
     */
    var onPostValidate: PostValidateCallback?

    /**
     * Current errors, if there's no error then this list will be empty
     *
     * The first error of the list will be shown to the user
     */
    var errors: MutableList<Error>
        private set

    /**
     * Minimum required length, -1 to disable
     */
    var minLength: Int
        private set

    /**
     * Whether or not the text can be empty
     */
    var allowEmpty: Boolean
        private set

    /**
     * Whether or not the text can contain spaces
     */
    var allowSpaces: Boolean
        private set

    /**
     * Whether or not the text can contain numbers
     */
    var allowNumbers: Boolean
        private set

    /**
     * Whether or not the text can contain lowercase letters
     */
    var allowLowercase: Boolean
        private set

    /**
     * Whether or not the text can contain uppercase letters
     */
    var allowUppercase: Boolean
        private set

    /**
     * Whether or not the text can contain special characters
     */
    var allowSpecialCharacters: Boolean
        private set

    /**
     * Whether or not the text can contain ambiguous characters
     */
    var allowAmbiguousCharacters: Boolean
        private set

    /**
     * Whether or not the text can contain similar characters
     */
    var allowSimilarCharacters: Boolean
        private set

    /**
     * Whether or not the text can contain repeated characters
     */
    var allowRepeatedCharacters: Boolean
        private set

    /**
     * Array of characters that should be considered invalid
     */
    var invalidCharacters: CharArray
        private set

    /**
     * Array of words that should be considered illegal
     */
    var illegalWords: Array<out String>
        private set

    /**
     * Whether or not illegal words check should ignore case
     */
    var ignoreWordsCase: Boolean
        private set

    /**
     * Array of characters that must be inside the text
     */
    var requiredCharacters: CharArray
        private set

    /**
     * Pattern that the text must match
     */
    var requiredPattern: Regex?
        private set

    /**
     * Number of required characters that must be numbers
     */
    var requiredNumbers: Int
        private set

    /**
     * Number of required characters that must be lowercase letters
     */
    var requiredLowercase: Int
        private set

    /**
     * Number of required characters that must be uppercase letters
     */
    var requiredUppercase: Int
        private set

    /**
     * Number of required characters that must be special characters
     */
    var requiredSpecialCharacters: Int
        private set

    init {
        errors = mutableListOf()
        minLength = -1
        allowEmpty = true
        allowSpaces = true
        allowNumbers = true
        allowLowercase = true
        allowUppercase = true
        allowSpecialCharacters = true
        allowAmbiguousCharacters = true
        allowRepeatedCharacters = true
        allowSimilarCharacters = true
        ignoreWordsCase = true
        requiredPattern = null
        requiredCharacters = charArrayOf()
        invalidCharacters = charArrayOf()
        illegalWords = emptyArray()
        requiredNumbers = 0
        requiredLowercase = 0
        requiredUppercase = 0
        requiredSpecialCharacters = 0
        onPreValidate = { text, errors ->
            val array = text.toCharArray()
            val size = text.length
            var numbers = 0
            var lowercases = 0
            var uppercases = 0
            var specials = 0
            var spaces = allowSpaces
            var ambiguous = allowAmbiguousCharacters
            var similar = allowSimilarCharacters
            var repeated = allowRepeatedCharacters
            var invalidChars = invalidCharacters.isEmpty()

            with(errors) {
                // Check for string emptiness
                if (size == 0 && !allowEmpty) {
                    add(Error.EMPTY)
                }

                // Check for the minimum length
                if (size < minLength && minLength != -1) {
                    add(Error.MIN_LENGTH)
                }

                // Checking each character
                for (i in 0 until size) {
                    val c = array[i]

                    // Checking if c is an invalid character
                    if (!invalidChars) {
                        if (invalidCharacters.contains(c)) {
                            add(Error.INVALID_CHARACTER)
                            invalidChars = true
                        }
                    }

                    // Checking if c is a space
                    if (!spaces) {
                        if (c == 32.toChar()) {
                            add(Error.SPACE)
                            spaces = true
                        }
                    }

                    // Checking if c is a number
                    if (c in NUMBERS.first()..NUMBERS.last()) {
                        if (!contains(Error.NUMBER) && !allowNumbers) {
                            add(Error.NUMBER)
                        }

                        numbers++
                    }

                    // Checking if c is a lowercase letter
                    if ((c in LOWERCASE_ALPHABET.first()..LOWERCASE_ALPHABET.last())) {
                        if (!contains(Error.LOWERCASE) && !allowLowercase) {
                            add(Error.LOWERCASE)
                        }

                        lowercases++
                    }

                    // Checking if c is an uppercase letter
                    if ((c in UPPERCASE_ALPHABET.first()..UPPERCASE_ALPHABET.last())) {
                        if (!contains(Error.UPPERCASE) && !allowUppercase) {
                            add(Error.UPPERCASE)
                        }

                        uppercases++
                    }

                    // Checking if c is a special character
                    if (SPECIAL_CHARACTERS.contains(c)) {
                        if (!contains(Error.SPECIAL_CHARACTER) && !allowSpecialCharacters) {
                            add(Error.SPECIAL_CHARACTER)
                        }

                        specials++
                    }

                    // Checking if c is an ambiguous character
                    if (!ambiguous) {
                        if (AMBIGUOUS_CHARACTERS.contains(c)) {
                            add(Error.AMBIGUOUS_CHARACTER)
                            ambiguous = true
                        }
                    }

                    // Checking if c is a similar character
                    if (!similar) {
                        if (SIMILAR_CHARACTERS.contains(c)) {
                            add(Error.SIMILAR)
                            similar = true
                        }
                    }

                    // Checking if c has already been used
                    if (!repeated) {
                        for (j in i + 1 until size) {
                            if (array[j] == c) {
                                add(Error.REPEATED)
                                repeated = true
                            }
                        }
                    }
                }

                // Checking for illegal words
                for (word in illegalWords) {
                    if (text.contains(word, ignoreWordsCase)) {
                        errors.add(Error.ILLEGAL_WORD)
                        break
                    }
                }

                // Checking for required characters
                for (c in requiredCharacters) {
                    if (!text.contains(c)) {
                        add(Error.REQUIRED_CHARACTERS)
                        break
                    }
                }

                // Checking for the pattern
                requiredPattern?.let { p ->
                    if (!text.contains(p)) {
                        add(Error.REQUIRED_PATTERN)
                    }
                }

                // Checking for required numbers
                if (requiredNumbers > numbers) {
                    add(Error.REQUIRED_NUMBERS)
                }

                // Checking for required lowercase letters
                if (requiredLowercase > lowercases) {
                    add(Error.REQUIRED_LOWERCASE)
                }

                // Checking for required uppercase letters
                if (requiredUppercase > uppercases) {
                    add(Error.REQUIRED_UPPERCASE)
                }

                // Checking for required special characters
                if (requiredSpecialCharacters > specials) {
                    add(Error.REQUIRED_SPECIAL)
                }
            }
        }
        onPostValidate = null
    }

    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        errors.clear()

        with(p0.toString()) {
            onPreValidate(this, errors)
            onPostValidate?.let {
                it(this, errors, this@TextValidator)
            }
        }
    }

    /**
     * Returns whether or not this validator has an error
     */
    fun hasErrors(): Boolean {
        return errors.isNotEmpty()
    }

    class Builder {
        private val validator: TextValidator = TextValidator()

        /**
         * Set the required amount of characters that must be special characters
         */
        fun requiredSpecialCharacters(count: Int): Builder {
            validator.requiredSpecialCharacters = count

            return this
        }

        /**
         * Set the required amount of characters that must be uppercase letters
         */
        fun requiredUppercase(count: Int): Builder {
            validator.requiredUppercase = count

            return this
        }

        /**
         * Set the required amount of characters that must be lowercase letters
         */
        fun requiredLowercase(count: Int): Builder {
            validator.requiredLowercase = count

            return this
        }

        /**
         * Set the required amount of characters that must be numbers
         */
        fun requiredNumbers(count: Int): Builder {
            validator.requiredNumbers = count

            return this
        }

        /**
         * Defines a pattern that the text must match, this can be used to check if the input is a an email
         */
        fun require(pattern: Regex): Builder {
            validator.requiredPattern = pattern

            return this
        }

        /**
         * Defines an array of characters that MUST be inside the text
         */
        fun require(vararg characters: Char): Builder {
            validator.requiredCharacters = characters

            return this
        }

        /**
         * Defines the minimum length, -1 by default (not defined)
         */
        fun withMinLength(minLength: Int): Builder {
            validator.minLength = minLength

            return this
        }

        /**
         * Defines whether or not the field can be empty, allowed by default
         */
        fun allowEmpty(value: Boolean): Builder {
            validator.allowEmpty = value

            return this
        }

        /**
         * Defines whether or not the field allows spaces or not, allowed by default
         */
        fun allowSpaces(value: Boolean): Builder {
            validator.allowSpaces = value

            return this
        }

        /**
         * Defines whether or not the field allows numbers, allowed by default
         */
        fun allowNumbers(value: Boolean): Builder {
            validator.allowNumbers = value

            return this
        }

        /**
         * Defines whether or not the field allows uppercase letters, allowed by default
         */
        fun allowUppercase(value: Boolean): Builder {
            validator.allowUppercase = value

            return this
        }

        /**
         * Defines whether or not the field allows lowercase letters, allowed by default
         */
        fun allowLowercase(value: Boolean): Builder {
            validator.allowLowercase = value

            return this
        }

        /**
         * Defines whether or not the field allows special characters, allowed by default
         *
         * Special characters can be customized by changing the value of [SPECIAL_CHARACTERS]
         */
        fun allowSpecialCharacters(value: Boolean): Builder {
            validator.allowSpecialCharacters = value

            return this
        }

        /**
         * Defines whether or not the field allows one character multiple times, allowed by default
         */
        fun allowRepeatedCharacters(value: Boolean): Builder {
            validator.allowRepeatedCharacters = value

            return this
        }

        /**
         * Defines whether or not the field allows similar characters, allowed by default
         *
         * Similar characters can be customized by changing the value of [SIMILAR_CHARACTERS]
         */
        fun allowSimilarCharacters(value: Boolean): Builder {
            validator.allowSimilarCharacters = value

            return this
        }

        /**
         * Defines whether or not the field allows ambiguous characters, allowed by default
         *
         * Ambiguous characters can be customized by changing the value of [AMBIGUOUS_CHARACTERS]
         */
        fun allowAmbiguousCharacters(value: Boolean): Builder {
            validator.allowAmbiguousCharacters = value

            return this
        }

        /**
         * Sets the given characters as invalid
         */
        fun without(vararg invalidCharacters: Char): Builder {
            validator.invalidCharacters = invalidCharacters

            return this
        }

        /**
         * Sets the given strings as illegal
         */
        @JvmOverloads
        fun without(vararg illegalWords: String, ignoreCase: Boolean = true): Builder {
            validator.illegalWords = illegalWords
            validator.ignoreWordsCase = ignoreCase

            return this
        }

        /**
         * Sets the [onPreValidate] callback value
         */
        fun onPreValidate(callback: PreValidateCallback): Builder {
            validator.onPreValidate = callback

            return this
        }

        /**
         * Sets the [onPostValidate] callback value
         */
        fun onPostValidate(callback: PostValidateCallback): Builder {
            validator.onPostValidate = callback

            return this
        }

        /**
         * Returns a TextValidator instance
         */
        fun build(): TextValidator {
            return validator
        }
    }

    companion object {
        /**
         * Characters that considered are special/symbols, this is customizable at run time
         */
        @JvmStatic
        var SPECIAL_CHARACTERS = "!#$%&+*/,.<>=?@^_~".toCharArray()

        /**
         * Ambiguous characters, this is customizable at run time
         */
        @JvmStatic
        var AMBIGUOUS_CHARACTERS = "$&%,./<>_^~".toCharArray()

        /**
         * Similar characters, this is customizable at run time
         */
        @JvmStatic
        val SIMILAR_CHARACTERS = "1l0OoiI".toCharArray()

        /**
         * Uppercase letters
         */
        @JvmStatic
        val UPPERCASE_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

        /**
         * Lowercase letters
         */
        @JvmStatic
        val LOWERCASE_ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray()

        /**
         * Numbers
         */
        @JvmStatic
        val NUMBERS = "0123456789".toCharArray()

        /**
         * Regex used to check if a string is an email
         */
        @JvmStatic
        val EMAIL_REGEX = Regex("\\w+@\\w+\\.\\w+")

        /**
         * Regex used to check if a string is a Date, this follows the ISO_8601 standard,
         * also it **only** checks for a date formatted like "yyyyMMdd hh:mm:ss"
         */
        @JvmStatic
        val SIMPLE_DATE_REGEX = Regex("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}")

        /**
         * Returns a [Builder] instance
         */
        @JvmStatic
        fun builder(): Builder {
            return Builder()
        }
    }
}

/**
 * Errors used by the [TextValidator], you can extend this and add more errors
 */
open class Error(val text: String) {
    override fun toString(): String {
        return text
    }

    companion object {
        @JvmStatic
        var EMPTY = Error("Text is empty")

        @JvmStatic
        var INVALID_CHARACTER = Error("Invalid character")

        @JvmStatic
        var SPACE = Error("Space are not allowed")

        @JvmStatic
        var MIN_LENGTH = Error("Minimum length not met")

        @JvmStatic
        var ILLEGAL_WORD = Error("Illegal word")

        @JvmStatic
        var NUMBER = Error("Numbers are not allowed")

        @JvmStatic
        var SPECIAL_CHARACTER = Error("Special characters are not allowed")

        @JvmStatic
        var UPPERCASE = Error("Uppercase letters are not allowed")

        @JvmStatic
        var LOWERCASE = Error("Lowercase letters are not allowed")

        @JvmStatic
        var SIMILAR = Error("Similar characters are not allowed")

        @JvmStatic
        var AMBIGUOUS_CHARACTER = Error("Ambiguous characters are not allowed")

        @JvmStatic
        var REPEATED = Error("Repeated characters are not allowed")

        @JvmStatic
        var REQUIRED_CHARACTERS = Error("Missing required characters")

        @JvmStatic
        var REQUIRED_PATTERN = Error("Required pattern is not matched")

        @JvmStatic
        var REQUIRED_NUMBERS = Error("Required number of numbers is not met")

        @JvmStatic
        var REQUIRED_LOWERCASE = Error("Required number of lowercase letters is not met")

        @JvmStatic
        var REQUIRED_UPPERCASE = Error("Required number of uppercase letters is not met")

        @JvmStatic
        var REQUIRED_SPECIAL = Error("Required number of special characters is not met")
    }
}

/**
 * Collection of default validators
 */
class Validators {
    companion object {
        /**
         * Email validator, this includes:
         * + No spaces
         * + Email regex pattern
         * + Mandatory characters: @(at) .(dot)
         */
        @JvmStatic
        val EMAIL = TextValidator.builder()
            .require(TextValidator.EMAIL_REGEX)
            .allowSpaces(false)
            .require('@', '.')
            .build()

        /**
         * Password validator, this includes:
         * + No spaces
         * + Min 1 lowercase
         * + Min 1 uppercase
         * + Min 1 number
         * + Not empty
         */
        @JvmStatic
        val PASSWORD_GOOD = TextValidator.builder()
            .allowSpaces(false)
            .requiredLowercase(1)
            .requiredUppercase(1)
            .requiredNumbers(1)
            .allowEmpty(false)
            .build()

        /**
         * Password validator with best config, this includes:
         * + No spaces
         * + Min length 10
         * + Min 3 lowercase
         * + Min 3 uppercase
         * + Min 3 number
         * + Min 1 special character
         * + Not empty
         */
        @JvmStatic
        val PASSWORD_BEST = TextValidator.builder()
            .allowSpaces(false)
            .requiredLowercase(3)
            .requiredUppercase(3)
            .requiredNumbers(3)
            .requiredSpecialCharacters(1)
            .allowEmpty(false)
            .withMinLength(10)
            .build()

        /**
         * Datetime validator that follows the ISO-8601 format "yyyy-mm-dd hh:mm:ss"
         */
        @JvmStatic
        val ISO_8601_DATETIME = TextValidator.builder()
            .require(Regex("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"))
            .allowLowercase(false)
            .allowUppercase(false)
            .build()
    }
}