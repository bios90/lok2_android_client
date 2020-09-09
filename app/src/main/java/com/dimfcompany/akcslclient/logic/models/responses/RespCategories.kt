package com.dimfcompany.akcslclient.logic.models.responses

import com.dimfcompany.akcsl.logic.models.ModelCategory
import com.dimfcompany.akcslclient.logic.models.responses.BaseResponse
import com.google.gson.annotations.SerializedName

class RespCategories(
        @SerializedName("data")
        var categories: ArrayList<ModelCategory>? = arrayListOf()
) : BaseResponse()