package com.dimfcompany.akcslclient.ui.screens.act_category_show

import android.util.Log
import com.dimfcompany.akcsl.base.FeedDisplayInfo
import com.dimfcompany.akcsl.base.LoadBehavior
import com.dimfcompany.akcsl.logic.models.ModelDocument
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.Constants
import com.dimfcompany.akcslclient.base.adapters.BaseCardListener
import com.dimfcompany.akcslclient.base.adapters.CardDocument
import com.dimfcompany.akcslclient.base.extensions.*
import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import com.dimfcompany.akcslclient.databinding.ItemDocumentBinding
import com.dimfcompany.akcslclient.logic.BtnAction
import com.dimfcompany.akcslclient.logic.SharedPrefsManager
import com.dimfcompany.akcslclient.logic.utils.BaseVmHelper
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderAlerter
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderDownloader
import com.dimfcompany.akcslclient.logic.utils.files.FileManager
import com.dimfcompany.akcslclient.logic.utils.files.MyFileItem
import com.dimfcompany.barista.logic.builders.BuilderDialogBottom
import com.justordercompany.barista.logic.utils.BuilderPermRequest
import com.justordercompany.barista.logic.utils.PermissionManager
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.io.File
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit
import kotlin.math.log

class VmActCategoryShow : BaseViewModel()
{
    val bs_category_title: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_search: BehaviorSubject<Optional<String>> = BehaviorSubject.create()
    val bs_rec_info_documents: BehaviorSubject<FeedDisplayInfo<ModelDocument>> = BehaviorSubject.createDefault(FeedDisplayInfo.getDefault())

    init
    {
        AppClass.app_component.inject(this)
        setBaseVmEvents()

        setEvents()
    }

    private fun setEvents()
    {
        bs_search
                .distinctUntilChanged()
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(
                    {
                        reloadDocuments()
                    })
                .disposeBy(composite_disposable)
    }

    override fun viewAttached()
    {
        super.viewAttached()
        reloadCategory()
        reloadDocuments()
    }

    private fun getCategoryIdExtra(): Long
    {
        val id = intent_extra.getLongExtraMy(Constants.Extras.CATEGORY_ID)
        if (id == null)
        {
            throw RuntimeException("**** Error no id passed ****")
        }
        return id
    }

    private fun reloadCategory()
    {
        base_networker.getCategoryById(getCategoryIdExtra(),
            {
                bs_category_title.onNext(it.title.asOptional())
            })
    }

    private fun reloadDocuments()
    {
        val search = bs_search.value?.value
        base_networker.getDocuments(getCategoryIdExtra(), search,
            {
                val rec_info = FeedDisplayInfo(it, LoadBehavior.UPDATE)
                bs_rec_info_documents.onNext(rec_info)
            })
    }


    inner class ViewListener : ActCategoryShowListener, CardDocument.Listener
    {
        override fun swipedToRefresh()
        {
            reloadDocuments()
        }

        override fun clickedLikeDislike(item: ModelDocument, bnd: ItemDocumentBinding)
        {
            BaseVmHelper.clickedDocumentLikeDislike(item, this@VmActCategoryShow)
            val rec_info = bs_rec_info_documents.value ?: return
            bs_rec_info_documents.onNext(rec_info)
        }

        override fun clickedCard(item: ModelDocument, bnd: ItemDocumentBinding)
        {
            BaseVmHelper.clickedCard(item, this@VmActCategoryShow)
        }
    }
}