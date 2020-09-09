package com.dimfcompany.akcslclient.ui.screens.act_register

import android.content.Intent
import android.util.Log
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.Constants
import com.dimfcompany.akcslclient.base.ObjWithImageUrl
import com.dimfcompany.akcslclient.base.extensions.*
import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import com.dimfcompany.akcslclient.logic.BtnAction
import com.dimfcompany.akcslclient.logic.utils.ValidationManager
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderAlerter
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderDialogMy
import com.dimfcompany.akcslclient.logic.utils.files.MyFileItem
import io.reactivex.subjects.BehaviorSubject

class VmActRegister : BaseViewModel()
{
    val bs_first_name: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_last_name: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_email: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_password_1: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_password_2: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_avatar: BehaviorSubject<Optional<ObjWithImageUrl>> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setBaseVmEvents()
    }

    inner class ViewListener() : ActRegisterListener
    {
        override fun clickedPrivacyPolicy()
        {
            BuilderDialogMy()
                    .setViewId(R.layout.la_dialog_scrollable_tv)
                    .setTitle(getStringMy(R.string.privacy_policy))
                    .setText(getStringMy(R.string.privacy_policy_text))
                    .setBtnOk(BtnAction(getStringMy(R.string.its_clear), {}))
                    .sendInVm(this@VmActRegister)
        }

        override fun clickedAvatar()
        {
            checkAndPickImage(
                {
                    bs_avatar.onNext(it.asOptional())
                })
        }

        override fun clickedRegister()
        {
            if (bs_is_keyboard_visible.value == true)
            {
                hideKeyboard()
            }
            val first_name = bs_first_name.value?.value
            val last_name = bs_last_name.value?.value
            val email = bs_email.value?.value
            val password_1 = bs_password_1.value?.value
            val password_2 = bs_password_2.value?.value
            val avatar = bs_avatar.value?.value as? MyFileItem

            val validation_data = ValidationManager.validateRegister(first_name, last_name, email, password_1, password_2)
            if (!validation_data.is_valid)
            {
                val builder = BuilderAlerter.getRedBuilder(validation_data.getErrorMessage())
                ps_to_show_alerter.onNext(builder)
                return
            }

            base_networker.makeRegister(first_name!!, last_name!!, email!!, password_1!!,avatar,
                {
                    val intent = Intent()
                    intent.myPutExtra(Constants.Extras.REGISTER_MADE, true)
                    intent.myPutExtra(Constants.Extras.EMAIL, email)
                    ps_to_finish.onNext(intent.asOptional())
                })
        }
    }
}