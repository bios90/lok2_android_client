package com.dimfcompany.akcslclient.ui.screens.act_main.tabs.tab_profile

import android.view.View
import androidx.databinding.DataBindingUtil
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.extensions.disposeBy
import com.dimfcompany.akcslclient.base.extensions.getColorMy
import com.dimfcompany.akcslclient.base.extensions.mainThreaded
import com.dimfcompany.akcslclient.databinding.LaProfileBinding
import com.dimfcompany.akcslclient.ui.screens.act_main.ActMain
import com.rucode.autopass.logic.utils.images.GlideManager

class TabProfile(act_main: ActMain)
{
    val composite_disposable = act_main.composite_disposable
    val bnd_tab_profile: LaProfileBinding
    val vm_tab_profile: VmTabProfile

    init
    {
        bnd_tab_profile = DataBindingUtil.inflate(act_main.layoutInflater, R.layout.la_profile, null, false)
        vm_tab_profile = act_main.my_vm_factory.getViewModel(VmTabProfile::class.java)
        act_main.setBaseVmActions(vm_tab_profile)

        setEvents()
        setListeners()

        bnd_tab_profile.cvAvatar.imgImg.setBackgroundColor(getColorMy(R.color.white))
    }

    private fun setListeners()
    {
        bnd_tab_profile.cvAvatar.imgImg.setOnClickListener(
            {
                vm_tab_profile.ViewListener().clickedAvatar()
            })

        bnd_tab_profile.tvLogout.setOnClickListener(
            {
                vm_tab_profile.ViewListener().clickedLogout()
            })

        bnd_tab_profile.tvPrivacy.setOnClickListener(
            {
                vm_tab_profile.ViewListener().clickedPrivacyPolicy()
            })
    }

    private fun setEvents()
    {
        vm_tab_profile.bs_user
                .mainThreaded()
                .subscribe(
                    {
                        val user = it.value ?: return@subscribe

                        bnd_tab_profile.tvEmail.text = user.email
                        bnd_tab_profile.tvName.text = user.getFullName()

                        user.url_avatar?.let(
                            {
                                GlideManager.loadImage(bnd_tab_profile.cvAvatar.imgImg, user.url_avatar,show_failed_images = false)
                            })

                    })
                .disposeBy(composite_disposable)
    }
}