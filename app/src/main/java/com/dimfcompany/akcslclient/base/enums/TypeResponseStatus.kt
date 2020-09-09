package com.dimfcompany.akcslclient.base.enums

import com.google.gson.annotations.SerializedName

enum class TypeResponseStatus
{
    @SerializedName("success")
    SUCCESS,
    @SerializedName("failed")
    FAILED;
}