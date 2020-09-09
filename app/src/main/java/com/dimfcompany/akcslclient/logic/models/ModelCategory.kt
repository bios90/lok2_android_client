package com.dimfcompany.akcsl.logic.models

import com.dimfcompany.akcslclient.base.ObjWithImageUrl
import com.dimfcompany.akcslclient.base.ObjectWithDates
import com.dimfcompany.akcslclient.base.ObjectWithId
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class ModelCategory(
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
        var documents: ArrayList<ModelDocument>? = null
) : Serializable, ObjectWithId, ObjectWithDates, ObjWithImageUrl
{
    override val image_url: String?
        get() = url_logo
}