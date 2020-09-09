package com.dimfcompany.akcslclient.base.enums

import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.extensions.getColorMy
import com.google.gson.annotations.SerializedName

enum class TypeAccountStatus
{
    @SerializedName("wait_admin_approve")
    WAIT_ADMIN_APPROVE,
    @SerializedName("active")
    ACTIVE,
    @SerializedName("banned")
    BANNED;

    companion object
    {
        fun initFromRadioPos(pos: Int): TypeAccountStatus?
        {
            return when (pos)
            {
                1 -> ACTIVE
                2 -> WAIT_ADMIN_APPROVE
                3 -> BANNED
                else -> return null
            }
        }
    }

    fun getTextForServer(): String
    {
        return when (this)
        {
            WAIT_ADMIN_APPROVE -> "wait_admin_approve"
            ACTIVE -> "active"
            BANNED -> "banned"
        }
    }

    fun getTextForDsiplay(): String
    {
        return when (this)
        {
            WAIT_ADMIN_APPROVE -> "ожидает одобрения"
            ACTIVE -> "активный"
            BANNED -> "заблокирован"
        }
    }

    fun getColor(): Int
    {
        return when (this)
        {
            WAIT_ADMIN_APPROVE -> getColorMy(R.color.yellow)
            ACTIVE -> getColorMy(R.color.green)
            BANNED -> getColorMy(R.color.red)
        }
    }
}

