package com.dimfcompany.akcslclient.logic.models.responses

import com.dimfcompany.akcslclient.logic.models.ModelUser
import com.google.gson.annotations.SerializedName

class RespUsers(
        @SerializedName("data")
        val users: ArrayList<ModelUser>? = null
) : BaseResponse()