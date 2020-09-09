package com.dimfcompany.akcslclient.di.application

import com.dimfcompany.akcsl.ui.screens.act_main.VmActMain
import com.dimfcompany.akcsl.ui.screens.act_main.tabs.tab_categories.VmTabCategories
import com.dimfcompany.akcslclient.di.activity.ComponentActivity
import com.dimfcompany.akcslclient.di.activity.ModuleActivity
import com.dimfcompany.akcslclient.logic.utils.NotificationManager
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderDownloader
import com.dimfcompany.akcslclient.networking.FbMessagingService
import com.dimfcompany.akcslclient.ui.screens.act_category_show.VmActCategoryShow
import com.dimfcompany.akcslclient.ui.screens.act_first.VmActFirst
import com.dimfcompany.akcslclient.ui.screens.act_main.tabs.tab_favorite.VmTabFavorite
import com.dimfcompany.akcslclient.ui.screens.act_main.tabs.tab_profile.VmTabProfile
import com.dimfcompany.akcslclient.ui.screens.act_main.tabs.tab_search.VmTabSearch
import com.dimfcompany.akcslclient.ui.screens.act_register.VmActRegister
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ModuleGlobal::class, ModuleNetworking::class])
interface ComponentApplication
{
    fun getActivityComponent(module: ModuleActivity): ComponentActivity
    fun inject(fb_messaging_service: FbMessagingService)
    fun inject(builder_downloader: BuilderDownloader)
    fun inject(notification_manager: NotificationManager)
    fun inject(vm: VmActFirst)
    fun inject(vm: VmActRegister)
    fun inject(vm: VmTabCategories)
    fun inject(vm: VmActMain)
    fun inject(vm: VmTabProfile)
    fun inject(vm: VmTabFavorite)
    fun inject(vm: VmTabSearch)
    fun inject(vm: VmActCategoryShow)
}