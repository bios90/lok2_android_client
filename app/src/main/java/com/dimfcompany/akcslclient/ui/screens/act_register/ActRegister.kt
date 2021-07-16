package com.dimfcompany.akcslclient.ui.screens.act_register

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.BaseActivity
import com.dimfcompany.akcslclient.base.Constants
import com.dimfcompany.akcslclient.base.extensions.*
import com.dimfcompany.akcslclient.databinding.ActRegisterBinding
import com.rucode.autopass.logic.utils.images.GlideManager

class ActRegister : BaseActivity()
{
    lateinit var bnd_act_register: ActRegisterBinding
    lateinit var vm_act_register: VmActRegister

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_register = DataBindingUtil.setContentView(this, R.layout.act_register)
        getActivityComponent().inject(this)
        vm_act_register = my_vm_factory.getViewModel(VmActRegister::class.java)
        setBaseVmActions(vm_act_register)

        setEvents()
        setListeners()
        checkExtras()
    }

    fun setNavStatus()
    {
        is_full_screen = true
        is_below_nav_bar = false
        color_status_bar = getColorMy(R.color.transparent)
        color_nav_bar = getColorMy(R.color.red)
        is_light_status_bar = true
        is_light_nav_bar = true
    }

    private fun setListeners()
    {
        connectBoth(bnd_act_register.etFirstName.getBsText(), vm_act_register.bs_first_name, composite_disposable)
        connectBoth(bnd_act_register.etLastName.getBsText(), vm_act_register.bs_last_name, composite_disposable)
        connectBoth(bnd_act_register.etEmail.getBsText(), vm_act_register.bs_email, composite_disposable)
        connectBoth(bnd_act_register.etPassword1.getBsText(), vm_act_register.bs_password_1, composite_disposable)
        connectBoth(bnd_act_register.etPassword2.getBsText(), vm_act_register.bs_password_2, composite_disposable)

        bnd_act_register.etPassword2.addDoneAction(
            {
                vm_act_register.ViewListener().clickedRegister()
            })

        bnd_act_register.tvRegister.setOnClickListener(
            {
                vm_act_register.ViewListener().clickedRegister()
            })

        bnd_act_register.cvAvatar.imgImg.setOnClickListener(
            {
                vm_act_register.ViewListener().clickedAvatar()
            })

        bnd_act_register.tvPrivacy.setOnClickListener(
            {
                vm_act_register.ViewListener().clickedPrivacyPolicy()
            })
    }

    private fun setEvents()
    {
        vm_act_register.bs_avatar
                .subscribe(
                    {
                        val url = it.value?.image_url ?: return@subscribe
                        GlideManager.loadImage(bnd_act_register.cvAvatar.imgImg, url)
                    })
                .disposeBy(composite_disposable)
    }

    private fun checkExtras()
    {
        val email = intent.getStringExtra(Constants.Extras.EMAIL)
        if (email != null)
        {
            vm_act_register.bs_email.onNext(email.asOptional())
        }
    }
}