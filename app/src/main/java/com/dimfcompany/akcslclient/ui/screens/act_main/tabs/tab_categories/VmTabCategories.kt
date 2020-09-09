package com.dimfcompany.akcsl.ui.screens.act_main.tabs.tab_categories

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.viewModelScope
import com.dimfcompany.akcsl.base.FeedDisplayInfo
import com.dimfcompany.akcsl.base.LoadBehavior
import com.dimfcompany.akcsl.logic.models.ModelCategory
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.Constants
import com.dimfcompany.akcslclient.base.adapters.BaseCard
import com.dimfcompany.akcslclient.base.adapters.BaseCardListener
import com.dimfcompany.akcslclient.base.extensions.runActionWithDelay
import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import com.dimfcompany.akcslclient.databinding.ItemCategoryBinding
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderIntent
import com.dimfcompany.akcslclient.ui.screens.act_category_show.ActCategoryShow
import io.reactivex.subjects.BehaviorSubject

class VmTabCategories : BaseViewModel()
{
    val bs_categories: BehaviorSubject<FeedDisplayInfo<ModelCategory>> = BehaviorSubject.create()

    init
    {
        AppClass.app_component.inject(this)
        setBaseVmEvents()

        setEvents()
    }

    override fun viewAttached()
    {
        super.viewAttached()
        reloadCategories()

        runActionWithDelay(viewModelScope, 4000,
            {
                reloadCategories()
            })
    }

    private fun setEvents()
    {

    }

    private fun reloadCategories()
    {
        base_networker.getAllCategories(
            {
                val info = FeedDisplayInfo(it, LoadBehavior.UPDATE)
                bs_categories.onNext(info)
            })
    }

    inner class ViewListener : BaseCardListener<ItemCategoryBinding, ModelCategory>
    {
        override fun clickedCard(item: ModelCategory, bnd: ItemCategoryBinding)
        {
            val category_id = item.id ?: return
            BuilderIntent()
                    .setActivityToStart(ActCategoryShow::class.java)
                    .addParam(Constants.Extras.CATEGORY_ID, category_id)
                    .sendInVm(this@VmTabCategories)
        }
    }
}