package com.dimfcompany.akcslclient.ui.screens.act_main.tabs.tab_search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dimfcompany.akcsl.base.FeedDisplayInfo
import com.dimfcompany.akcsl.base.LoadBehavior
import com.dimfcompany.akcsl.logic.models.ModelDocument
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.Constants
import com.dimfcompany.akcslclient.base.adapters.CardDocument
import com.dimfcompany.akcslclient.base.enums.TypeTab
import com.dimfcompany.akcslclient.base.extensions.*
import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import com.dimfcompany.akcslclient.databinding.ItemDocumentBinding
import com.dimfcompany.akcslclient.logic.utils.BaseVmHelper
import com.dimfcompany.akcslclient.logic.utils.MyPush
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class VmTabSearch : BaseViewModel()
{
    val bs_search: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_rec_info_documents: BehaviorSubject<FeedDisplayInfo<ModelDocument>> = BehaviorSubject.createDefault(FeedDisplayInfo.getDefault())


    init
    {
        AppClass.app_component.inject(this)
        setBaseVmEvents()

        setEvents()
    }

    override fun viewAttached()
    {
        super.viewAttached()
        checkPushExtra()
    }

    private fun setEvents()
    {
        bs_search
                .distinctUntilChanged()
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(
                    {
                        if (it.value.isNullOrEmpty())
                        {
                            bs_rec_info_documents.onNext(FeedDisplayInfo.getDefault())
                        }
                        else
                        {
                            reloadDocuments()
                        }
                    })
                .disposeBy(composite_disposable)
    }

    private fun reloadDocuments()
    {
        val search = bs_search.value?.value
        base_networker.getDocuments(null, search,
            {
                val rec_info = FeedDisplayInfo(it, LoadBehavior.UPDATE)
                bs_rec_info_documents.onNext(rec_info)
            })
    }

    private fun checkPushExtra()
    {
        val my_push = intent_extra.getSerializableExtra(Constants.Extras.MY_PUSH) as? MyPush
        my_push?.document_name?.let(
            {
                runActionWithDelay(viewModelScope, 500,
                    {
                        bus_main_events.bs_current_tab.onNext(TypeTab.SEARCH)
                        bs_search.onNext(it.asOptional())
                    })

            })
    }

    inner class ViewListener : CardDocument.Listener
    {
        override fun clickedLikeDislike(item: ModelDocument, bnd: ItemDocumentBinding)
        {
            BaseVmHelper.clickedDocumentLikeDislike(item, this@VmTabSearch)
            val rec_info = bs_rec_info_documents.value ?: return
            bs_rec_info_documents.onNext(rec_info)
        }

        override fun clickedCard(item: ModelDocument, bnd: ItemDocumentBinding)
        {
            BaseVmHelper.clickedCard(item, this@VmTabSearch)
        }
    }
}