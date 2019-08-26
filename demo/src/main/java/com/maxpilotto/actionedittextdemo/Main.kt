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

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.maxpilotto.actionedittext.TextValidator
import com.maxpilotto.actionedittext.Util.set
import com.maxpilotto.actionedittext.actions.Icon
import com.maxpilotto.actionedittext.actions.Toggle
import kotlinx.android.synthetic.main.main.*

/**
 * Created on 10/08/2019 at 15:49
 */
class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        firstName.set {
            textValidator = TextValidator.builder()
                .allowSpecialCharacters(false)
                .allowNumbers(false)
                .allowSpecialCharacters(false)
                .allowEmpty(false)
                .withMinLength(2)
                .build()

            action(Icon(context).apply {
                icon = R.drawable.information
                tint = Color.parseColor("#1e88e5")

                onClick = {
                    Toast.makeText(context, "This field is your first name", Toast.LENGTH_LONG).show()
                }
            })
        }

        lastName.set {
            textValidator = TextValidator.builder()
                .allowSpecialCharacters(false)
                .allowNumbers(false)
                .allowSpecialCharacters(false)
                .build()

            action(Icon(context).apply {
                icon = R.drawable.information
                tint = Color.parseColor("#1e88e5")

                onClick = {
                    Toast.makeText(context, "This field is your last name", Toast.LENGTH_LONG).show()
                }
            })
        }

        password.set {
            textValidator = TextValidator.builder()
                .allowSpaces(false)
                .allowEmpty(false)
                .withMinLength(8)
                .requiredNumbers(1)
                .requiredSpecialCharacters(1)
                .requiredLowercase(1)
                .requiredUppercase(1)
                .build()

            action(Toggle(context).apply {
                checkedRes = R.drawable.eye
                checkedTint = Color.parseColor("#4CAF50")
                uncheckedRes = R.drawable.eye_off
                uncheckedTint = Color.parseColor("#2196F3")
                checked = false

                onToggle = { checked ->
                    setPassword(!checked)
                }
            })
        }

        email.set {
            textValidator = TextValidator.builder()
                .allowSpaces(false)
                .allowEmpty(false)
                .require(TextValidator.EMAIL_REGEX)     // The pattern is "%s@%s.%s", where %s is a string of 1 or more characters
                .build()

            action(CustomAction(context).apply{
                firstText = "Hello"
                secondText = "World!"
                textColor = Color.parseColor("#4CAF50")
            })

//            action(Icon(context).apply {
//                icon = R.drawable.information
//                tint = Color.parseColor("#1e88e5")
//
//                onClick = {
//                    Toast.makeText(context, "The email will be used as an username", Toast.LENGTH_LONG).show()
//                }
//            })
        }

        confirm.setOnClickListener {
            val errors = firstName.hasErrors() or
                    lastName.hasErrors() or
                    password.hasErrors() or
                    email.hasErrors()

            if (!errors) {
                Toast.makeText(this, "Registration complete!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}