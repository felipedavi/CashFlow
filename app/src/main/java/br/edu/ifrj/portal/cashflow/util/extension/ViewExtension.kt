package br.edu.ifrj.portal.cashflow.util.extension

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import br.edu.ifrj.portal.cashflow.R

fun View.hideKeyboard() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        windowInsetsController?.hide(WindowInsets.Type.ime())
    } else {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
    this.clearFocus()
}

fun EditText.isValid(): Boolean {
    val text = this.text.toString().trim()
    return if (TextUtils.isEmpty(text)) {
        this.error = resources.getString(R.string.edit_error)
        false
    } else {
        this.error = null
        true
    }
}