package com.dimfcompany.akcslclient.base.adapters

import com.dimfcompany.akcsl.logic.models.ModelCategory
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.extensions.getStringMy
import com.dimfcompany.akcslclient.base.extensions.toVisibility
import com.dimfcompany.akcslclient.databinding.ItemCategoryBinding
import com.dimfcompany.akcslclient.logic.utils.formatToString
import com.rucode.autopass.logic.utils.images.GlideManager

class CardCategory(bnd: ItemCategoryBinding) : BaseCard<ItemCategoryBinding, ModelCategory, BaseCardListener<ItemCategoryBinding, ModelCategory>>(bnd)
{
    override fun bindItem(item: ModelCategory, listener: BaseCardListener<ItemCategoryBinding, ModelCategory>?)
    {
        super.bindItem(item, listener)
        item.url_logo?.let(
            {
                GlideManager.loadImage(bnd.imgLogo, it)
            })

        bnd.tvTitle.text = item.title
        bnd.tvText.text = item.text
        if (item.updated != null)
        {
            val date_formated = item.updated!!.formatToString()
            bnd.tvUpdated.text = "${getStringMy(R.string.updated)} : $date_formated"
        }

        bnd.imgNew.visibility = item.isNew().toVisibility()
        val documents_count = item.documents?.size ?: 0
        bnd.tvDocumensCount.text = "Документов: $documents_count"
    }
}