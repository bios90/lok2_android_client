package com.dimfcompany.akcslclient.ui.screens.act_main.tabs.tab_favorite

import android.util.Log
import com.dimfcompany.akcsl.base.FeedDisplayInfo
import com.dimfcompany.akcsl.base.LoadBehavior
import com.dimfcompany.akcsl.logic.models.ModelDocument
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.adapters.CardDocument
import com.dimfcompany.akcslclient.base.extensions.asOptional
import com.dimfcompany.akcslclient.base.extensions.disposeBy
import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import com.dimfcompany.akcslclient.databinding.ItemDocumentBinding
import com.dimfcompany.akcslclient.logic.SharedPrefsManager
import com.dimfcompany.akcslclient.logic.utils.BaseVmHelper
import com.dimfcompany.akcslclient.logic.utils.strings.StringManager
import io.reactivex.subjects.BehaviorSubject

class VmTabFavorite : BaseViewModel()
{
    val bs_documents_favorites: BehaviorSubject<FeedDisplayInfo<ModelDocument>> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setBaseVmEvents()

        setEvents()
    }

    private fun setEvents()
    {
        SharedPrefsManager.pref_favorite_docs
                .asObservable()
                .subscribe(
                    {
                        Log.e("VmTabFavorite", "setEvents: Got herererer")
                        reloadFavorites()
                    })
                .disposeBy(composite_disposable)
    }

    fun reloadFavorites()
    {
        val ids = SharedPrefsManager.pref_favorite_docs.get().value?.favorites_ids
        if (ids == null || ids.size == 0)
        {
            bs_documents_favorites.onNext(FeedDisplayInfo.getDefault())
            return
        }

        val ids_as_str_array = ids.map({ it.toString() }).toCollection(ArrayList())
        val ids_str = StringManager.listOfStringToSingle(ids_as_str_array, "-")

        base_networker.getDocumentByMultiIds(ids_str,
            {
                bs_documents_favorites.onNext(FeedDisplayInfo(it, LoadBehavior.UPDATE))
            })
    }

    inner class ViewListener : CardDocument.Listener
    {
        override fun clickedLikeDislike(item: ModelDocument, bnd: ItemDocumentBinding)
        {
            BaseVmHelper.clickedDocumentLikeDislike(item, this@VmTabFavorite)
        }

        override fun clickedCard(item: ModelDocument, bnd: ItemDocumentBinding)
        {
            BaseVmHelper.clickedCard(item, this@VmTabFavorite)
        }
    }
}