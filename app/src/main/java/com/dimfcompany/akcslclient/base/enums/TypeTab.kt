package com.dimfcompany.akcslclient.base.enums

enum class TypeTab
{
    CATEGORIES,
    FAVORITE,
    SEARCH,
    PROFILE;

    fun getPos(): Int
    {
        return when (this)
        {
            CATEGORIES -> 0
            FAVORITE -> 1
            SEARCH -> 2
            PROFILE -> 3
        }
    }
}