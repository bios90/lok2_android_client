package com.dimfcompany.akcsl.logic.models

import com.dimfcompany.akcslclient.base.ObjWithImageUrl
import com.dimfcompany.akcslclient.base.ObjectWithDates
import com.dimfcompany.akcslclient.base.ObjectWithId
import com.dimfcompany.akcslclient.logic.SharedPrefsManager
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class ModelDocument(
        override var id: Long?,
        @SerializedName("created_at")
        override var created: Date?,
        @SerializedName("updated_at")
        override var updated: Date?,
        @SerializedName("deleted_at")
        override var deleted: Date?,
        var title: String? = null,
        var text: String? = null,
        var url_logo: String?,
        var url_pdf: String?,
        var pdf_file_name: String?,
        var url_video: String?,
        var url_html: String?
) : Serializable, ObjectWithId, ObjectWithDates, ObjWithImageUrl
{
    override val image_url: String?
        get() = url_logo

    fun isLiked(): Boolean
    {
        if (id == null)
        {
            return false
        }

        return SharedPrefsManager.hasInFavorites(id!!)
    }
}