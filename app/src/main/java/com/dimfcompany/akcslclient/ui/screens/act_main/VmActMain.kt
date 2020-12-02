package com.dimfcompany.akcsl.ui.screens.act_main

import android.content.Intent
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.enums.TypeTab
import com.dimfcompany.akcslclient.base.extensions.disposeBy
import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderIntent
import com.dimfcompany.akcslclient.ui.screens.act_first.ActFirst
import io.reactivex.subjects.BehaviorSubject

class VmActMain : BaseViewModel()
{
    lateinit var bs_scroll_to_tab: BehaviorSubject<TypeTab>

    init
    {
        AppClass.app_component.inject(this)
        setBaseVmEvents()

        setEvents()
    }

    private fun setEvents()
    {
        bs_scroll_to_tab = BehaviorSubject.createDefault(bus_main_events.bs_current_tab.value)
        bus_main_events.bs_current_tab
                .distinctUntilChanged()
                .subscribe(bs_scroll_to_tab)

        bus_main_events.ps_to_logout
                .subscribe(
                    {
                        BuilderIntent()
                                .setActivityToStart(ActFirst::class.java)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                .sendInVm(this)
                    })
                .disposeBy(composite_disposable)
    }

    inner class ViewListener() : ActMainListener
    {
        override fun clickedTab(tab: TypeTab)
        {
            bus_main_events.bs_current_tab.onNext(tab)
        }
    }
}