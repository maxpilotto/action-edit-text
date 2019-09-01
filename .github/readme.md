# ActionEditText
A customizable EditText that supports actions and has a quick way of validating user input

<br>
<img src="https://raw.github.com/maxpilotto/action-edit-text/master/.github/imgs/g3.gif" width="400">

### Index
+ [Getting started](#getting-started)
    + [Xml](#xml)
    + [Kotlin](#kotlin)
    + [Actions](#actions)
    + [Default properties](#default-properties)
    + [Text validation](#text-validation)
    + [Paint it red!](#paint-it-red)
    + [Error handling](#error-handling)
+ [Customizations](#custom-stuff)
    + [Custom errors](#custom-errors)
    + [Custom input validation](#custom-input-validation)
    + [Custom actions](#custom-actions)
+ [Demo](#demo)
+ [License](#license)

# Getting started
In your project's `build.gradle`
```gradle
repositories {
	maven { url "https://jitpack.io" }
}
```

In your modules's `build.gradle`
```gradle 
dependencies {
    implementation 'com.github.maxpilotto:action-edit-text:2.1'
}
```

## XML
```xml
<com.maxpilotto.actionedittext.ActionEditText
    android:id="@+id/firstName"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:aed_label="First name"
    app:aed_hint="Type your name ..."/>
```

## Kotlin
```kotlin
firstName.apply {
    label = "First name"
    hint = "Type your name ..."
}
```

## Java
```java
firstName = findViewById(R.id.firstName);
firstName.setLabel("First name");
firstName.setHint("Type your name ...");
```

## Actions
Actions are interactive views that are added to the right of an ActionEditText, to add an action you can use the `action()` method  
```kotlin
firstName.apply{    // Kotlin
    action(Icon(context).apply {
        icon = R.drawable.eye
        tint = Color.parseColor("#1e88e5")
    })

    action(Icon(context).apply {
        icon = R.drawable.information
        tint = Color.parseColor("#1e88e5")
    })

    showActions()   // This displays all the actions that are added
}
```
or
```kotlin
import com.maxpilotto.actionedittext.Util.set

firstName.set{    // Kotlin
    action(Icon(context).apply {
        icon = R.drawable.eye
        tint = Color.parseColor("#1e88e5")
    })

    action(Icon(context).apply {
        icon = R.drawable.information
        tint = Color.parseColor("#1e88e5")
    })
}
```
or
```java
Icon i1 = new Icon(this);   //Java
i1.setIcon(R.drawable.eye);
i1.setTint(Color.parseColor("#1e88e5"));

Icon i2 = new Icon(this);
i2.setIcon(R.drawable.information);
i2.setTint(Color.parseColor("#1e88e5"));

firstName.action(i1);
firstName.action(i2);
firstName.showActions();
```

This will look like the following  <br><br>
<img src="https://raw.github.com/maxpilotto/action-edit-text/master/.github/imgs/s1.jpg">

There are different Actions and each of them has different customizable properties
+ Action (Base Action, can't be used)
    + onClick and onLongClick callbacks
    + errorColor
+ CheckBox
    + tint
    + onToggle callback
    + checked value
+ Icon
    + icon
    + tint
+ Label
    + text
    + textSize
    + textColor
+ Toggle
    + checkedRes
    + uncheckedRes
    + checkedTint
    + uncheckedTint
    + onToggle callback
    + checked value

## Default properties
Every component (Action & ActionEditText) will be initialized with default values (text color, text size), these values are stored in the `Default` class, these can be changed so that the values will match your application theme for example.  

You should use write the changes inside the your `Application` class
```java
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        
        Default.TEXT_COLOR = Color.WHITE
        Default.ERROR_COLOR = Color.RED
    }
}
```


## Text validation
This library has a `TextValidator` class which can be used to quickly validate text and show errors

The validator lets you check:
+ Text emptiness
+ Minimum length
+ Presence of 
    + Spaces
    + Numbers
    + Lowercase letters
    + Uppercase letters
    + Special characters (customizable)
    + Ambiguos characters (,./<>_^~) (customizable)
    + Similar characters (1l0OoiI) (customizable)
    + Repeated characters
+ Defined list of invalid characters and words
+ Defined list of required characters or pattern (regex)
+ Required amount of
    + Numbers
    + Uppercase/Lowercase letters
    + Special characters

E.g. Password validator
```kotlin
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
        uncheckedRes = R.drawable.eye_off
        checked = false

        onToggle = { checked ->
            setPassword(!checked)
        }
    })
}
```
or
```java
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
```

Result  <br><br>
<img src="https://raw.github.com/maxpilotto/action-edit-text/master/.github/imgs/g1.gif">

## Paint it red
You can tint all components of an ActionEditText using the errorColor by setting `tintAllOnError` to true, in Java `setTintAllOnError(true)`, this feature is enabled by default

| Enabled | Disabled |
| - | - |
| <img src="https://raw.github.com/maxpilotto/action-edit-text/master/.github/imgs/s3.jpg">| <img src="https://raw.github.com/maxpilotto/action-edit-text/master/.github/imgs/s2.jpg"> |

## Error handling
Before submitting any form or collecting user data you should check if there are errors inside the input fields, this is done by calling the `hasErrors()` method
```kotlin
val errors = firstName.hasErrors() or
        lastName.hasErrors() or
        password.hasErrors() or
        email.hasErrors()

if (!errors) {
    showToast("Registration complete!")
}
```

# Customizations

## Custom errors
There's a bunch of pre-defined errors with hardcoded messages, these errors are static values that can be found inside the `TextValidator.Error` class, you should change these values when the application loads, inside the `Application` class for example
```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        
        Error.EMPTY = getString(R.string.empty)
        Error.MIN_LENGTH = getString(R.string.minLength)
    }
}
```

For further customization you can create your own errors which can be used later in the next paragraph
```kotlin
//Kotlin
class ErrorConst {
    companion object{
        val NO_SPACES = Error("Text must have spaces")
        val NO_DATE = Error("Inserted text must be a date")
    }
}

// Java
public class ErrorConst{
    public static Error NO_SPACES = Error("Text must have spaces")
    public static Error NO_DATE = Error("Inserted text must be a date")
}
```

## Custom input validation
If you have instantiate a `TextValidator` you may have noticed that there two callbacks, `onPreValidate` and `onPostValidate`, both already have a defined value, the first one will check for the default errors, the second one instead will pick the first error and set its text to the ActionEditText error view

`onPreValidate`, should only be used when you want to completely remove the default error checking
`onPostValidate`, this should be used if you need to check for extra (your own) errors or do any action on already existing errors

If you decide to change the `onPostValidate` you will have to take care of setting/removing the error text
```java
TextValidator.builder()
    .allowSpecialCharacters(false)
    .allowNumbers(false)
    .onPostValidate { text, errors, validator ->
        if (errors.isNotEmpty()) {
            val first = errors.first()  // Take the first error in the list

            error = when (first) {
                // Our MIN_LENGTH could be Error("Min size is {value}")
                Error.MIN_LENGTH -> first.text.replaceAny("{value}", validator.minLength)

                else -> first.text
            }
        } else {    // If there are no errors you should hide the error view
            error = null
        }
    }
    .build()
```

With custom errors
```java
class CustomError{
    companion object{
        val EMPTY_TEXT = Error("Text must not be empty!")
        val BLANK_TEXT = Error("Text must not be blank!")
        val NO_DATE = Error("Text must not contain dates!")
    }
}

...

TextValidator.builder()
    .onPreValidate { text, errors ->
        if (text.isNullOrEmpty()){
            errors.add(CustomError.EMPTY_TEXT)
        }
        
        if (text.isBlank()){
            errors.add(CustomError.BLANK_TEXT)
        }
        
        if (text.contains(DATE_REGEX))){
            errors.add(CustomError.NO_DATE)
        }
    }
    .build()
```

## Custom actions
Custom actions can be created and should be compatible with the ActionEditText, let's create a toggleable TextView, which has two text value

+ First off, create a class that implements the basic `Action` class
+ Specify the type of view, if you are using a complex view (which is made by different views) then you can use a ViewGroup or View
```java
class CustomAction : Action<TextView>{      // Kotlin
    
}

public class CustomAction extends Action<TextView> {   // Java

}
```

+ Declare some variables that we'll use later
```java
class CustomAction : Action<TextView>{      // Kotlin
    var firstText = ""      // Text when the view is checked
    var secondText = ""     // Text when the view is not checked
    var textColor: Int      // Text color of the TextView
        set(value) {
            actionView?.setTextColor(value)
            field = value
        }
    var checked = false     // Whether or not the view is checked

public class CustomAction extends Action<TextView> {   // Java
    public String firstText;
    public String secondText;
    public int textColor;
    public boolean checked;

    // NOTE: In Java you should use private fields and their getters/setters    
```

+ Implement the default constructor and create the view for this Action
```java
constructor(context: Context) : super(context){     // Kotlin
    actionView = TextView(context).apply{    // Create a TextView which will be the view of our Action
        layoutParams = defaultLayoutParams   // Set the default layout params (size,size) + 10dp margin start
        text = firstText                    // Set the default text, it should be set based on the [checked] value

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

public CustomActionJ(@NotNull Context context) {    // Java
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
```

+ Implement the `tintAll(Boolean)` method
```java
override fun tintAll(applyErrorTint: Boolean) {     // Kotlin
    actionView?.let{
        if (applyErrorTint){
            it.setTextColor(errorColor)
        }else{
            it.setTextColor(textColor)
        }

    }
}

@Override
public void tintAll(boolean applyErrorTint) {       // Java
    if (applyErrorTint){
        getActionView().setTextColor(getErrorColor());
    }else{
        getActionView().setTextColor(textColor);
    }
}
```

Result  <br><br>
<img src="https://raw.github.com/maxpilotto/action-edit-text/master/.github/imgs/g2.gif">

Full source code [here](https://github.com/maxpilotto/action-edit-text/blob/master/demo/src/main/java/com/maxpilotto/actionedittextdemo/CustomAction.kt)


# Demo
You can download the demo version [here](hhttps://github.com/maxpilotto/action-edit-text/releases/download/2.1/demo.apk)

# License
```
Copyright 2018 Max Pilotto

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
