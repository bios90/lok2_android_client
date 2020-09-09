package com.dimfcompany.akcslclient.logic.models.responses

import com.dimfcompany.akcsl.logic.models.ModelCategory
import com.google.gson.annotations.SerializedName

class RespCategorySingle(
        @SerializedName("data")
        var category: ModelCategory? = null
) : BaseResponse()