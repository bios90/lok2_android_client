package com.dimfcompany.akcslclient.base.adapters

import android.util.Log
import com.dimfcompany.akcsl.logic.models.ModelCategory
import com.dimfcompany.akcsl.logic.models.ModelDocument
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.extensions.*
import com.dimfcompany.akcslclient.databinding.ItemCategoryBinding
import com.dimfcompany.akcslclient.databinding.ItemDocumentBinding
import com.dimfcompany.akcslclient.logic.SharedPrefsManager
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderBg
import com.dimfcompany.akcslclient.logic.utils.formatToString
import com.rucode.autopass.logic.utils.images.GlideManager

class CardDocument(bnd: ItemDocumentBinding) : BaseCard<ItemDocumentBinding, ModelDocument, CardDocument.Listener>(bnd)
{
    interface Listener : BaseCardListener<ItemDocumentBinding, ModelDocument>
    {
        fun clickedLikeDislike(item: ModelDocument, bnd: ItemDocumentBinding)
    }

    override fun bindItem(item: ModelDocument, listener: Listener?)
    {
        setListeners(item, listener)

        GlideManager.loadImage(bnd.imgLogo, item.image_url)
        bnd.tvTitle.text = item.title
        bnd.tvText.text = item.text
        if (item.updated != null)
        {
            val date_formated = item.updated!!.formatToString()
            bnd.tvUpdated.text = "${getStringMy(R.string.updated)} : $date_formated"
        }
        else
        {
            bnd.tvUpdated.text = null
        }

        bnd.imgNew.visibility = item.isNew().toVisibility()

        bnd.tvLink.visibility = (item.url_html != null).toVisibility()
        bnd.tvVideo.visibility = (item.url_video != null).toVisibility()
        bnd.tvPdf.visibility = (item.url_pdf != null).toVisibility()

        bnd.cvContent.post(
            {
                val height = bnd.cvContent.height
                bnd.tvLikeDislike.setHeightMy(height)
                bnd.tvLikeDislike.setWidthMy(height)
            })

        val drw_red = BuilderBg.getSimpleDrawableRipple(4f, getColorMy(R.color.red), getColorMy(R.color.red_dark))
        val drw_green = BuilderBg.getSimpleDrawableRipple(4f, getColorMy(R.color.green), getColorMy(R.color.green_dark))

        if (item.isLiked())
        {
            bnd.tvLikeDislike.setBackground(drw_red)
            bnd.tvLikeDislike.text = getStringMy(R.string.faw_ban)
        }
        else
        {
            bnd.tvLikeDislike.setBackground(drw_green)
            bnd.tvLikeDislike.text = getStringMy(R.string.faw_heart)
        }
    }

    private fun setListeners(item: ModelDocument, listener: Listener?)
    {
        bnd.cvContent.setOnClickListener(
            {
                listener?.clickedCard(item, bnd)
            })

        bnd.tvLikeDislike.setOnClickListener(
            {
                listener?.clickedLikeDislike(item, bnd)
            })
    }
}