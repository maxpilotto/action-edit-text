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
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maxpilotto.actionedittext.actions.Action;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 26/08/2019 at 23:01
 */
public class CustomActionJ extends Action<TextView> {
    public String firstText;
    public String secondText;
    public int textColor;
    public boolean checked;

    public CustomActionJ(@NotNull Context context) {
        super(context);

        TextView view = new TextView(context);
        view.setLayoutParams(getDefaultLayoutParams());
        view.setText(firstText);
        view.setOnClickListener((v) -> {
            checked = !checked;

            if (checked){
                view.setText(secondText);
            }else{
                view.setText(firstText);
            }
        });

        setSize(LinearLayout.LayoutParams.WRAP_CONTENT);
        setRipple(Ripple.CIRCULAR);
        setFixedWidth(-1);

        setActionView(view);
    }

    @Override
    public void tintAll(boolean applyErrorTint) {
        if (applyErrorTint){
            getActionView().setTextColor(getErrorColor());
        }else{
            getActionView().setTextColor(textColor);
        }
    }
}
