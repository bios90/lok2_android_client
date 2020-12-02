package com.dimfcompany.akcslclient.base.diff

import android.util.Log
import com.dimfcompany.akcsl.logic.models.ModelCategory
import com.dimfcompany.akcslclient.logic.utils.areDatesEqualForDiff

class DiffCategories(items_new: List<ModelCategory>, items_old: List<ModelCategory>) : BaseDiff<ModelCategory>(items_new, items_old)
{
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        val item_new = items_new.get(newItemPosition)
        val item_old = items_old.get(oldItemPosition)

        if(!areDatesEqualForDiff(item_new.updated,item_old.updated))
        {
            return false
        }

        if(!item_new.url_logo.equals(item_old.url_logo))
        {
            return false
        }

        if(!item_new.title.equals(item_old.title))
        {
            return false
        }

        if(!item_new.text.equals(item_old.text))
        {
            return false
        }

        if(item_new.documents?.size != item_old.documents?.size)
        {
            return false
        }

        return true
    }
}