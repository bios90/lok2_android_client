package com.dimfcompany.akcslclient.base.diff

import com.dimfcompany.akcsl.logic.models.ModelCategory
import com.dimfcompany.akcsl.logic.models.ModelDocument
import com.dimfcompany.akcslclient.logic.utils.areDatesEqualForDiff

class DiffDocuments(items_new: List<ModelDocument>, items_old: List<ModelDocument>) : BaseDiff<ModelDocument>(items_new, items_old)
{
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        val item_new = items_new.get(newItemPosition)
        val item_old = items_old.get(oldItemPosition)

        if (!areDatesEqualForDiff(item_new.updated, item_old.updated))
        {
            return false
        }

        if (!item_new.url_logo.equals(item_old.url_logo))
        {
            return false
        }

        if (!item_new.title.equals(item_old.title))
        {
            return false
        }

        if (!item_new.text.equals(item_old.text))
        {
            return false
        }

        if (!item_new.url_html.equals(item_old.url_html))
        {
            return false
        }

        if (!item_new.url_video.equals(item_old.url_video))
        {
            return false
        }

        if (!item_new.url_pdf.equals(item_old.url_pdf))
        {
            return false
        }

        if (item_new.isLiked() != item_old.isLiked())
        {
            return false
        }

        return true
    }
}