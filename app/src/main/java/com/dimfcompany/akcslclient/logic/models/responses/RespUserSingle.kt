package com.dimfcompany.akcslclient.logic.models.responses

import com.dimfcompany.akcslclient.logic.models.ModelUser
import com.google.gson.annotations.SerializedName

class RespUserSingle(
        @SerializedName("data")
        val user: ModelUser? = null
) : BaseResponse()