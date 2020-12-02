package com.dimfcompany.akcslclient.base

import android.util.Log
import android.view.View
import com.dimfcompany.akcslclient.logic.utils.DateManager
import com.dimfcompany.akcslclient.logic.utils.getHourMinuteDiff
import java.util.*

interface TabView
{
    fun getView(): View
}

interface ObjectWithId
{
    var id: Long?
}

fun ArrayList<out ObjectWithId>.getPosOfObject(id: Long): Int?
{
    this.forEachIndexed(
        { index, obj ->
            if (obj.id == id)
            {
                return@getPosOfObject index
            }
        })

    return null
}




interface ObjectWithDates
{
    var created: Date?
    var updated: Date?
    var deleted: Date?

    fun isNew(): Boolean
    {
        if (updated == null)
        {
            return false
        }

        val diff_hours = getHourMinuteDiff(updated!!, Date()).first
        return diff_hours < 48
    }
}

interface ObjWithImageUrl
{
    val image_url: String?

    companion object
    {
        fun fromString(str: String): ObjWithImageUrl
        {
            return object : ObjWithImageUrl
            {
                override val image_url: String = str
            }
        }
    }
}
