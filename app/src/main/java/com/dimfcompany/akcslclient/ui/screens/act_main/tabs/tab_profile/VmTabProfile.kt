package com.dimfcompany.akcslclient.ui.screens.act_main.tabs.tab_profile

import io.reactivex.subjects.BehaviorSubject
import java.lang.RuntimeException
import android.content.Intent
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.extensions.Optional
import com.dimfcompany.akcslclient.base.extensions.asOptional
import com.dimfcompany.akcslclient.base.extensions.getStringMy
import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import com.dimfcompany.akcslclient.logic.BtnAction
import com.dimfcompany.akcslclient.logic.SharedPrefsManager
import com.dimfcompany.akcslclient.logic.models.ModelUser
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderDialogMy
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderIntent
import com.dimfcompany.akcslclient.ui.screens.act_first.ActFirst


class VmTabProfile : BaseViewModel()
{
    val bs_user: BehaviorSubject<Optional<ModelUser>> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setBaseVmEvents()

        reloadUserData()
    }

    fun reloadUserData()
    {
        base_networker.getUserById(getUserId(),
            {
                bs_user.onNext(it.asOptional())
            })
    }

    private fun getUserId(): Long
    {
        val user_id = SharedPrefsManager.pref_current_user.get().value?.id
        if (user_id == null)
        {
            throw RuntimeException("**** Error user not logged in ****")
        }
        return user_id
    }


    inner class ViewListener : TabProfileListener
    {
        override fun clickedPrivacyPolicy()
        {
            showPrivacyPolicy()
        }

        override fun clickedLogout()
        {
            BuilderDialogMy()
                    .setViewId(R.layout.la_dialog_simple)
                    .setTitle(getStringMy(R.string.exit))
                    .setText(getStringMy(R.string.exit_current_account))
                    .setBtnOk(BtnAction(getStringMy(R.string.exit),
                        {
                            bus_main_events.ps_to_logout.onNext(Any())
                        }))
                    .setBtnCancel(BtnAction.getDefaultCancel())
                    .sendInVm(this@VmTabProfile)
        }

        override fun clickedAvatar()
        {
            checkAndPickImage(
                {
                    base_networker.updateUserInfo(getUserId(), null, null, null, it,
                        {
                            bs_user.onNext(it.asOptional())
                        })
                })
        }
    }
}