package com.dimfcompany.barista.logic.builders

import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import com.dimfcompany.akcslclient.logic.BtnAction


class BuilderDialogBottom
{
    val btns: ArrayList<BtnAction> = ArrayList()
    var show_cancel: Boolean = false
    var cancel_on_touch_outside: Boolean = true
    var title: String? = null
    var text: String? = null
    var dim_background: Boolean = true

    fun addBtn(btn: BtnAction) = apply(
        {
            this.btns.add(btn)
        })

    fun setBtns(btns: ArrayList<BtnAction>) = apply(
        {
            this.btns.addAll(btns)
        })

    fun setTitle(title: String?) = apply(
        {
            this.title = title
        })

    fun setText(text: String?) = apply(
        {
            this.text = text
        })

    fun setShowCancel(show_cancel: Boolean) = apply(
        {
            this.show_cancel = show_cancel
        })

    fun setCancelOnTouchOutside(cancel_on_touch_outside: Boolean) = apply(
        {
            this.cancel_on_touch_outside = cancel_on_touch_outside
        })

    fun setDimBackground(dim_background: Boolean) = apply(
        {
            this.dim_background = dim_background
        })

    fun sendInVm(vm:BaseViewModel)
    {
        vm.ps_to_show_bottom_dialog.onNext(this)
    }
}