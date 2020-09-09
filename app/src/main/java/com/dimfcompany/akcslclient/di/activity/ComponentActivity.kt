package com.dimfcompany.akcslclient.di.activity

import com.dimfcompany.akcslclient.ui.screens.act_category_show.ActCategoryShow
import com.dimfcompany.akcslclient.ui.screens.act_first.ActFirst
import com.dimfcompany.akcslclient.ui.screens.act_main.ActMain
import com.dimfcompany.akcslclient.ui.screens.act_register.ActRegister
import dagger.Subcomponent

@Subcomponent(modules = [ModuleActivity::class])
interface ComponentActivity
{
    fun inject(act:ActFirst)
    fun inject(act:ActRegister)
    fun inject(act:ActMain)
    fun inject(act:ActCategoryShow)
}