package com.dimfcompany.akcslclient.logic.models.responses

import com.google.gson.annotations.SerializedName
import com.dimfcompany.akcslclient.base.enums.TypeResponseStatus
import com.dimfcompany.akcslclient.logic.utils.strings.StringManager


open class BaseResponse(

        var status: TypeResponseStatus = TypeResponseStatus.FAILED,
        var errors: ArrayList<String>? = null
)
{
    fun isFailed(): Boolean
    {
        return status == TypeResponseStatus.FAILED
    }

    fun getErrorMessage(): String
    {
        if (this.errors != null && this.errors!!.size > 0)
        {
            return StringManager.listOfStringToSingle(this.errors!!)
        }

        return "Неизвестная ошибка"
    }
}

open class BaseResponseWithData<T>(
        @SerializedName("data")
        val data: T? = null
) : BaseResponse()


