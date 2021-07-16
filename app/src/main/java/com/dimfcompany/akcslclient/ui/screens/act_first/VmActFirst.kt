package com.dimfcompany.akcslclient.ui.screens.act_first

import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.Constants
import com.dimfcompany.akcslclient.base.extensions.Optional
import com.dimfcompany.akcslclient.base.extensions.asOptional
import com.dimfcompany.akcslclient.base.extensions.getStringMy
import com.dimfcompany.akcslclient.base.extensions.hideKeyboard
import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import com.dimfcompany.akcslclient.logic.BtnAction
import com.dimfcompany.akcslclient.logic.BtnActionWithText
import com.dimfcompany.akcslclient.logic.SharedPrefsManager
import com.dimfcompany.akcslclient.logic.utils.ValidationManager
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderAlerter
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderDialogMy
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderIntent
import com.dimfcompany.akcslclient.logic.utils.strings.StringManager
import com.dimfcompany.akcslclient.ui.screens.act_main.ActMain
import com.dimfcompany.akcslclient.ui.screens.act_register.ActRegister
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class VmActFirst : BaseViewModel()
{
    val bs_email: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_password: BehaviorSubject<Optional<String>> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setBaseVmEvents()
    }

    private fun checkLogin()
    {
        val is_logged = SharedPrefsManager.pref_current_user.get().value != null
        if (is_logged)
        {
            BuilderIntent()
                    .setActivityToStart(ActMain::class.java)
                    .addOnStartAction(
                        {
                            ps_to_finish.onNext(Optional(null))
                        })
                    .sendInVm(this@VmActFirst)
        }
    }

    inner class ViewListener : ActFirstListener
    {
        override fun clickedForgotPass()
        {
            return
            BuilderDialogMy()
                    .setTitle(getStringMy(R.string.password_recover))
                    .setText(getStringMy(R.string.for_password_recover))
                    .setViewId(R.layout.la_dialog_with_et)
                    .setBtnOkWithText(BtnActionWithText(getStringMy(R.string.recover),
                        {
                            if (!ValidationManager.isEmail(it))
                            {
                                val builder = BuilderAlerter.getRedBuilder(getStringMy(R.string.enter_valid_email))
                                ps_to_show_alerter.onNext(builder)
                                return@BtnActionWithText
                            }

                            base_networker.makePassReset(it!!,
                                {
                                    val builder = BuilderAlerter.getGreenBuilder("На почту $it отправлено письмо с инструкцией по восстановлению")
                                    ps_to_show_alerter.onNext(builder)
                                })
                        }))
                    .setBtnCancel(BtnAction.getDefaultCancel())
                    .sendInVm(this@VmActFirst)
        }

        override fun isUserLogged(): Boolean
        {
            return SharedPrefsManager.pref_current_user.get().value != null
        }

        override fun clickedLogin()
        {
            hideKeyboard()

            val email = bs_email.value?.value
            val password = bs_password.value?.value
            val fb_token = SharedPrefsManager.pref_fb_token.get().value

            val validation_data = ValidationManager.validateLogin(email, password)
            if (!validation_data.is_valid)
            {
                val builder = BuilderAlerter.getRedBuilder(validation_data.getErrorMessage())
                ps_to_show_alerter.onNext(builder)
                return
            }

            base_networker.makeLogin(email!!, password!!, fb_token,
                {

//                    if (it.email_verified != true)
//                    {
//                        val message = "Необходимо подтвердить email"
//                        val builder = BuilderAlerter.getRedBuilder(message)
//                        ps_to_show_alerter.onNext(builder)
//                        return@makeLogin
//                    }

                    SharedPrefsManager.pref_current_user.asConsumer().accept(it.asOptional())
                    checkLogin()
                })
        }

        override fun clickedToRegister()
        {
            BuilderIntent()
                    .setActivityToStart(ActRegister::class.java)
                    .addParam(Constants.Extras.EMAIL, bs_email.value?.value)
                    .setOkAction(
                        {
                            val email = it?.getStringExtra(Constants.Extras.EMAIL)
                            if (email != null)
                            {
                                bs_email.onNext(email.asOptional())
                            }

                            if (it?.getBooleanExtra(Constants.Extras.REGISTER_MADE, false) == true)
                            {
                                val builder_aleter = BuilderAlerter.getGreenBuilder("Поздравляем вы зарегистрированы в приложении Хронос. Вы сможете войти после одобрения от администратора")
                                ps_to_show_alerter.onNext(builder_aleter)
                            }
                        })
                    .sendInVm(this@VmActFirst)
        }
    }
}