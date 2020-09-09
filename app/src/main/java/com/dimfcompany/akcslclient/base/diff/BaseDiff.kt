package com.dimfcompany.akcslclient.base.diff

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.dimfcompany.akcslclient.base.ObjectWithId

abstract class BaseDiff<T : ObjectWithId>(val items_new: List<T>, val items_old: List<T>) : DiffUtil.Callback()
{
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
    {
        val item_new = items_new.get(newItemPosition)
        val item_old = items_old.get(oldItemPosition)

        if (item_new.id == null || item_old.id == null)
        {
            return false
        }

        return item_new.id == item_old.id
    }

    override fun getOldListSize(): Int
    {
        return items_old.size
    }

    override fun getNewListSize(): Int
    {
        return items_new.size
    }
}
