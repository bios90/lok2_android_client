package com.dimfcompany.akcslclient.ui.screens.act_category_show

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimfcompany.akcsl.logic.models.ModelCategory
import com.dimfcompany.akcsl.logic.models.ModelDocument
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.BaseActivity
import com.dimfcompany.akcslclient.base.adapters.BaseCardListener
import com.dimfcompany.akcslclient.base.adapters.BaseRvAdapter
import com.dimfcompany.akcslclient.base.adapters.CardCategory
import com.dimfcompany.akcslclient.base.adapters.CardDocument
import com.dimfcompany.akcslclient.base.diff.DiffCategories
import com.dimfcompany.akcslclient.base.diff.DiffDocuments
import com.dimfcompany.akcslclient.base.extensions.*
import com.dimfcompany.akcslclient.databinding.ActCategoryShowBinding
import com.dimfcompany.akcslclient.databinding.ActFirstBinding
import com.dimfcompany.akcslclient.databinding.ItemCategoryBinding
import com.dimfcompany.akcslclient.databinding.ItemDocumentBinding
import com.dimfcompany.akcslclient.ui.screens.act_first.VmActFirst

class ActCategoryShow : BaseActivity()
{
    lateinit var bnd_act_category_show: ActCategoryShowBinding
    lateinit var vm_act_category_show: VmActCategoryShow
    lateinit var adapter_documents: BaseRvAdapter<ItemDocumentBinding, ModelDocument, CardDocument.Listener>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        bnd_act_category_show = DataBindingUtil.setContentView(this, R.layout.act_category_show)
        getActivityComponent().inject(this)
        vm_act_category_show = my_vm_factory.getViewModel(VmActCategoryShow::class.java)
        setBaseVmActions(vm_act_category_show)

        setEvents()
        setRecycler()
        setListeners()
    }

    fun setNavStatus()
    {
        is_full_screen = false
        color_status_bar = getColorMy(R.color.red)
        color_nav_bar = getColorMy(R.color.gray0)
        is_light_status_bar = true
        is_light_nav_bar = false
    }

    private fun setListeners()
    {
        connectBoth(bnd_act_category_show.etSearch.getBsText(), vm_act_category_show.bs_search, composite_disposable)
    }

    private fun setEvents()
    {
        vm_act_category_show.bs_category_title
                .mainThreaded()
                .subscribe(
                    {
                        Log.e("ActCategoryShow", "setEvents: Here got")
                        bnd_act_category_show.tvHeader.text = it.value
                    })
                .disposeBy(composite_disposable)

        vm_act_category_show.bs_rec_info_documents
                .mainThreaded()
                .subscribe(
                    {
                        Log.e("ActCategoryShow", "setEvents: Hererer ${it.items.size}")
                        adapter_documents.setItems(it)
                    })
                .disposeBy(composite_disposable)
    }

    private fun setRecycler()
    {
        adapter_documents = BaseRvAdapter(
            R.layout.item_document,
            CardDocument::class.java,
            DiffDocuments::class.java,
            ItemDocumentBinding::class.java,
            vm_act_category_show.ViewListener())

        bnd_act_category_show.recDocuments.adapter = adapter_documents
        bnd_act_category_show.recDocuments.layoutManager = LinearLayoutManager(this)
        bnd_act_category_show.recDocuments.setDivider(getColorMy(R.color.transparent), dp2pxInt(8f))
    }
}