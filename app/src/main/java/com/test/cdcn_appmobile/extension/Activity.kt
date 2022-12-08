package com.test.cdcn_appmobile.extension

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.test.cdcn_appmobile.R

/*
 * Created by tuyenpc on 10/9/2022
 */

internal fun FragmentActivity.addFragment(
    containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String? = null,
) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.add(containerId, fragment, tag ?: fragment.javaClass.name)
    if (addToBackStack) {
        transaction.addToBackStack(tag ?: fragment.javaClass.name)
    }
    transaction.commit()
}

internal fun FragmentActivity.replaceFragment(
    containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String? = null,
    for_signUp: Boolean = false,
) {
    val transaction = supportFragmentManager.beginTransaction()
    if (for_signUp)
        transaction.setCustomAnimations(
            R.anim.enter,
            R.anim.exit,
            R.anim.pop_enter,
            R.anim.pop_exit
        )
    transaction.replace(containerId, fragment, tag ?: fragment.javaClass.name)
    if (addToBackStack) {
        transaction.addToBackStack(tag ?: fragment.javaClass.name)
    }
    transaction.commit()
}

internal fun FragmentActivity.backToPreFragment() {
    supportFragmentManager.popBackStack()
}

internal fun FragmentActivity.popAllFragment() {
    repeat(supportFragmentManager.backStackEntryCount) {
        supportFragmentManager.popBackStack()
    }
}

internal fun FragmentActivity.showDialogFrag(
    title: String = "",
    content: String = "",
    confirm: String = "",
    negative: String = "",
    negative_callback: () -> Unit,
) {
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.fragment_dialog)

    val window = dialog.window ?: return

    window.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val windowAttributes = window.attributes

    windowAttributes.gravity = Gravity.CENTER
    window.attributes = windowAttributes

    dialog.setCancelable(true)

    val txtTitle: TextView = dialog.findViewById(R.id.txtTitleDialog)
    val txtContentDialog: TextView = dialog.findViewById(R.id.txtContentDialog)
    val btnCancelDialog: Button = dialog.findViewById(R.id.btnCancelDialog)
    val btnChangeDialog: Button = dialog.findViewById(R.id.btnChangeDialog)

    if(title != "") txtTitle.text = title
    if(content != "") txtContentDialog.text = content
    if(negative != "") btnCancelDialog.text = negative
    btnCancelDialog.setOnClickListener {
        dialog.dismiss()
    }

    if(confirm != "") btnChangeDialog.text = confirm
    btnChangeDialog.setOnClickListener {
        negative_callback()
        dialog.dismiss()
    }

    dialog.show()
}

internal fun Context.getHeight(): Float = this.resources.displayMetrics.heightPixels.toFloat()

internal fun Context.getWidth(): Float = this.resources.displayMetrics.widthPixels.toFloat()
