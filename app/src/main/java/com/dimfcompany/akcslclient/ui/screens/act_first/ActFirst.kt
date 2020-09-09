package com.dimfcompany.akcslclient.ui.screens.act_first

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.BaseActivity
import com.dimfcompany.akcslclient.base.extensions.addDoneAction
import com.dimfcompany.akcslclient.base.extensions.connectBoth
import com.dimfcompany.akcslclient.base.extensions.getColorMy
import com.dimfcompany.akcslclient.databinding.ActFirstBinding
import com.dimfcompany.akcslclient.ui.screens.act_main.ActMain

class ActFirst : BaseActivity()
{
    lateinit var bnd_act_first: ActFirstBinding
    lateinit var vm_act_first: VmActFirst

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_first = DataBindingUtil.setContentView(this, R.layout.act_first)
        getActivityComponent().inject(this)
        vm_act_first = my_vm_factory.getViewModel(VmActFirst::class.java)

        //Removed because now check in splash activity
//        if(checkForLogin())
//        {
//            return
//        }
        setBaseVmActions(vm_act_first)
        setListeners()
    }

    fun setNavStatus()
    {
        is_full_screen = true
        color_status_bar = getColorMy(R.color.transparent)
        color_nav_bar = getColorMy(R.color.red)
        is_light_status_bar = true
        is_light_nav_bar = true
    }

    private fun checkForLogin():Boolean
    {
        if(vm_act_first.ViewListener().isUserLogged())
        {
            val intent = Intent(this, ActMain::class.java)
            startActivity(intent)
            finish()
            return true
        }

        return false
    }

    private fun setListeners()
    {
        connectBoth(bnd_act_first.etEmail.getBsText(), vm_act_first.bs_email, composite_disposable)
        connectBoth(bnd_act_first.etPassword.getBsText(), vm_act_first.bs_password, composite_disposable)

        bnd_act_first.tvLogin.setOnClickListener(
            {
                vm_act_first.ViewListener().clickedLogin()
            })

        bnd_act_first.etPassword.addDoneAction(
            {
                vm_act_first.ViewListener().clickedLogin()
            })

        bnd_act_first.tvRegister.setOnClickListener(
            {
                vm_act_first.ViewListener().clickedToRegister()
            })

        bnd_act_first.tvForgotPassword.setOnClickListener(
            {
                vm_act_first.ViewListener().clickedForgotPass()
            })
    }
}