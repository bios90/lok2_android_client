package com.dimfcompany.akcslclient.ui.screens.act_main.tabs.tab_favorite

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimfcompany.akcsl.logic.models.ModelCategory
import com.dimfcompany.akcsl.logic.models.ModelDocument
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.adapters.BaseCardListener
import com.dimfcompany.akcslclient.base.adapters.BaseRvAdapter
import com.dimfcompany.akcslclient.base.adapters.CardDocument
import com.dimfcompany.akcslclient.base.diff.DiffDocuments
import com.dimfcompany.akcslclient.base.extensions.*
import com.dimfcompany.akcslclient.databinding.ItemCategoryBinding
import com.dimfcompany.akcslclient.databinding.ItemDocumentBinding
import com.dimfcompany.akcslclient.databinding.LaFavoriteBinding
import com.dimfcompany.akcslclient.databinding.LaProfileBinding
import com.dimfcompany.akcslclient.ui.screens.act_main.ActMain
import com.dimfcompany.akcslclient.ui.screens.act_main.tabs.tab_profile.VmTabProfile

class TabFavorite(val act_main: ActMain)
{
    val composite_disposable = act_main.composite_disposable
    val bnd_tab_favorite: LaFavoriteBinding
    val vm_tab_favorite: VmTabFavorite
    lateinit var adapter_documents: BaseRvAdapter<ItemDocumentBinding, ModelDocument, CardDocument.Listener>

    init
    {
        bnd_tab_favorite = DataBindingUtil.inflate(act_main.layoutInflater, R.layout.la_favorite, null, false)
        vm_tab_favorite = act_main.my_vm_factory.getViewModel(VmTabFavorite::class.java)
        act_main.setBaseVmActions(vm_tab_favorite)

        setEvents()
        setRecycler()
        setListeners()
    }

    private fun setEvents()
    {
        vm_tab_favorite.bs_documents_favorites
                .mainThreaded()
                .subscribe(
                    {
                        adapter_documents.setItems(it)
                        bnd_tab_favorite.tvNoFavorites.visibility = (it.items.size == 0).toVisibility()
                    })
                .disposeBy(composite_disposable)
    }

    private fun setListeners()
    {

    }

    private fun setRecycler()
    {
        adapter_documents = BaseRvAdapter(
            R.layout.item_document,
            CardDocument::class.java,
            DiffDocuments::class.java,
            ItemDocumentBinding::class.java,
            vm_tab_favorite.ViewListener())

        bnd_tab_favorite.recDocuments.adapter = adapter_documents
        bnd_tab_favorite.recDocuments.layoutManager = LinearLayoutManager(act_main)
        bnd_tab_favorite.recDocuments.setDivider(getColorMy(R.color.transparent), dp2pxInt(8f))
    }
}