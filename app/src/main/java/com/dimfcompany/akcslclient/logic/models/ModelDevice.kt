package com.dimfcompany.akcslclient.logic.models

import com.dimfcompany.akcslclient.base.ObjectWithDates
import com.dimfcompany.akcslclient.base.ObjectWithId
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class ModelDevice
    (
        override var id: Long?,
        @SerializedName("created_at")
        override var created: Date?,
        @SerializedName("updated_at")
        override var updated: Date?,
        @SerializedName("deleted_at")
        override var deleted: Date?,
        var env: String? = null,
        var device_name: String? = null
) : Serializable, ObjectWithId, ObjectWithDates