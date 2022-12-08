package com.test.cdcn_appmobile.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/*
 * Created by tuyen.dang on 10/12/2022
 */
 
internal fun EditText.addTextChangedListener(onTextChange: (s: String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChange(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {

        }
    })
}
