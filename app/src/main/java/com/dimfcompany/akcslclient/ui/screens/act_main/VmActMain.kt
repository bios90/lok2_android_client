package com.dimfcompany.akcsl.ui.screens.act_main

import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.enums.TypeTab
import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

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
    }

    inner class ViewListener() : ActMainListener
    {
        override fun clickedTab(tab: TypeTab)
        {
            bus_main_events.bs_current_tab.onNext(tab)
        }
    }
}