package com.dimfcompany.akcsl.ui.screens.act_main.tabs.tab_categories

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimfcompany.akcsl.logic.models.ModelCategory
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.adapters.BaseCard
import com.dimfcompany.akcslclient.base.adapters.BaseCardListener
import com.dimfcompany.akcslclient.base.adapters.BaseRvAdapter
import com.dimfcompany.akcslclient.base.adapters.CardCategory
import com.dimfcompany.akcslclient.base.diff.DiffCategories
import com.dimfcompany.akcslclient.base.extensions.*
import com.dimfcompany.akcslclient.databinding.ItemCategoryBinding
import com.dimfcompany.akcslclient.databinding.LaCategoriesBinding
import com.dimfcompany.akcslclient.ui.screens.act_main.ActMain

class TabCategories(act_main: ActMain)
{
    val composite_disposable = act_main.composite_disposable
    val bnd_tab_categories: LaCategoriesBinding
    val vm_tab_categories: VmTabCategories
    lateinit var adapter_category: BaseRvAdapter<ItemCategoryBinding, ModelCategory, BaseCardListener<ItemCategoryBinding, ModelCategory>>

    init
    {
        bnd_tab_categories = DataBindingUtil.inflate(act_main.layoutInflater, R.layout.la_categories, null, false)
        vm_tab_categories = act_main.my_vm_factory.getViewModel(VmTabCategories::class.java)
        act_main.setBaseVmActions(vm_tab_categories)

        setListeners()
        setRecycler()
        setEvents()
    }


    private fun setListeners()
    {

    }

    private fun setRecycler()
    {
        adapter_category = BaseRvAdapter(R.layout.item_category, CardCategory::class.java, DiffCategories::class.java, ItemCategoryBinding::class.java, vm_tab_categories.ViewListener())
        bnd_tab_categories.recCategories.adapter = adapter_category
        bnd_tab_categories.recCategories.layoutManager = LinearLayoutManager(bnd_tab_categories.root.context)
        bnd_tab_categories.recCategories.setDivider(getColorMy(R.color.transparent), dp2pxInt(8f))

        bnd_tab_categories.srlCategories.setColorSchemeColors(getColorMy(R.color.red_dark), getColorMy(R.color.red), getColorMy(R.color.red_light))
        bnd_tab_categories.srlCategories.setOnRefreshListener(
            {
                vm_tab_categories.ViewListener().swipedToRefresh()
            })
    }

    private fun setEvents()
    {
        vm_tab_categories.bs_categories
                .mainThreaded()
                .subscribe(
                    {
                        bnd_tab_categories.srlCategories.isRefreshing = false
                        adapter_category.setItems(it)
                    })
                .disposeBy(composite_disposable)
    }
}