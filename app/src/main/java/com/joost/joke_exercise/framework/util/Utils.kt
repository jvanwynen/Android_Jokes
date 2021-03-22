package com.joost.joke_exercise.framework.util

import android.widget.Spinner

val <T> T.exhaustive: T
    get() = this

fun Spinner.setSelectionOnStringValue(value: String?){
    for (i in 0 until this.count) {
        if (this.getItemAtPosition(i).toString() == value) {
            this.setSelection(i)
            return
        }
    }
     this.setSelection(0)
}