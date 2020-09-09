package com.dimfcompany.akcslclient.base

import com.dimfcompany.akcslclient.base.enums.TypeTab
import com.dimfcompany.akcslclient.base.extensions.Optional
import com.dimfcompany.akcslclient.base.extensions.disposeBy
import com.dimfcompany.akcslclient.logic.SharedPrefsManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class BusMainEvents
{
    val composite_disposable = CompositeDisposable()
    val bs_current_tab = BehaviorSubject.createDefault(TypeTab.CATEGORIES)
    val ps_to_logout: PublishSubject<Any> = PublishSubject.create()

    init
    {
        ps_to_logout.subscribe(
            {
                SharedPrefsManager.pref_current_user.asConsumer().accept(Optional(null))
            })
                .disposeBy(composite_disposable)
    }
}