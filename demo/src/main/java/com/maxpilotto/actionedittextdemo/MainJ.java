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
package com.maxpilotto.actionedittextdemo;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maxpilotto.actionedittext.ActionEditText;
import com.maxpilotto.actionedittext.TextValidator;
import com.maxpilotto.actionedittext.actions.Icon;
import com.maxpilotto.actionedittext.actions.Toggle;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Created on 26/08/2019 at 15:36
 */
public class MainJ extends AppCompatActivity {
    private ActionEditText firstName;
    private ActionEditText password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firstName = findViewById(R.id.firstName);
        password = findViewById(R.id.password);

        // First name ActionEditText
        firstName.setLabel("First name");
        firstName.setHint("Type your name ...");

        Icon i1 = new Icon(this);
        i1.setIcon(R.drawable.eye);
        i1.setTint(Color.parseColor("#1e88e5"));

        Icon i2 = new Icon(this);
        i2.setIcon(R.drawable.information);
        i2.setTint(Color.parseColor("#1e88e5"));

        firstName.action(i1);
        firstName.action(i2);
        firstName.showActions();

        // Password ActionEditText
        Toggle t1 = new Toggle(this);
        t1.setCheckedRes(R.drawable.eye);
        t1.setUncheckedRes(R.drawable.eye_off);
        t1.setOnToggle(new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(Boolean checked) {
                password.setPassword(!checked);

                return Unit.INSTANCE;
            }
        });
        t1.setChecked(false);

        password.setTextValidator(TextValidator.builder()
                .allowSpaces(false)
                .allowEmpty(false)
                .withMinLength(8)
                .requiredNumbers(1)
                .requiredSpecialCharacters(1)
                .requiredLowercase(1)
                .requiredUppercase(1)
                .build()
        );
        password.action(t1);
        password.showActions();
    }
}
