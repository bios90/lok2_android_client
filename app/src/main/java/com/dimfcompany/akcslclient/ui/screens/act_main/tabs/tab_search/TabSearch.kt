package com.dimfcompany.akcslclient.ui.screens.act_main.tabs.tab_search

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimfcompany.akcsl.logic.models.ModelDocument
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.adapters.BaseRvAdapter
import com.dimfcompany.akcslclient.base.adapters.CardDocument
import com.dimfcompany.akcslclient.base.diff.DiffDocuments
import com.dimfcompany.akcslclient.base.extensions.*
import com.dimfcompany.akcslclient.databinding.ItemDocumentBinding
import com.dimfcompany.akcslclient.databinding.LaProfileBinding
import com.dimfcompany.akcslclient.databinding.LaSearchBinding
import com.dimfcompany.akcslclient.ui.screens.act_main.ActMain
import com.dimfcompany.akcslclient.ui.screens.act_main.tabs.tab_profile.VmTabProfile

class TabSearch(val act_main: ActMain)
{
    val composite_disposable = act_main.composite_disposable
    val bnd_tab_search: LaSearchBinding
    val vm_tab_search: VmTabSearch
    lateinit var adapter_documents: BaseRvAdapter<ItemDocumentBinding, ModelDocument, CardDocument.Listener>

    init
    {
        bnd_tab_search = DataBindingUtil.inflate(act_main.layoutInflater, R.layout.la_search, null, false)
        vm_tab_search = act_main.my_vm_factory.getViewModel(VmTabSearch::class.java)
        act_main.setBaseVmActions(vm_tab_search)

        setEvents()
        setRecycler()
        setListeners()
    }

    private fun setEvents()
    {
        vm_tab_search.bs_rec_info_documents
                .mainThreaded()
                .subscribe(
                    {
                        adapter_documents.setItems(it)
                    })
                .disposeBy(composite_disposable)
    }

    private fun setListeners()
    {
        connectBoth(bnd_tab_search.etSearch.getBsText(), vm_tab_search.bs_search, composite_disposable)
    }

    private fun setRecycler()
    {
        adapter_documents = BaseRvAdapter(
            R.layout.item_document,
            CardDocument::class.java,
            DiffDocuments::class.java,
            ItemDocumentBinding::class.java,
            vm_tab_search.ViewListener())

        bnd_tab_search.recDocuments.adapter = adapter_documents
        bnd_tab_search.recDocuments.layoutManager = LinearLayoutManager(act_main)
        bnd_tab_search.recDocuments.setDivider(getColorMy(R.color.transparent), dp2pxInt(8f))
    }
}