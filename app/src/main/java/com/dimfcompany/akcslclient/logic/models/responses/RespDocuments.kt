package com.dimfcompany.akcslclient.logic.models.responses

import com.dimfcompany.akcsl.logic.models.ModelCategory
import com.dimfcompany.akcsl.logic.models.ModelDocument
import com.google.gson.annotations.SerializedName

class RespDocuments(
        @SerializedName("data")
        var documents: ArrayList<ModelDocument>? = null
) : BaseResponse()