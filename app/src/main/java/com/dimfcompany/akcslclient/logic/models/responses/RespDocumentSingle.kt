package com.dimfcompany.akcslclient.logic.models.responses

import com.dimfcompany.akcsl.logic.models.ModelCategory
import com.dimfcompany.akcsl.logic.models.ModelDocument
import com.google.gson.annotations.SerializedName

class RespDocumentSingle(
        @SerializedName("data")
        var document: ModelDocument? = null
) : BaseResponse()