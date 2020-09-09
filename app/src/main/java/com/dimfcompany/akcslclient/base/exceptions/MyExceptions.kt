package com.dimfcompany.akcslclient.base.exceptions

import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.extensions.getStringMy
import java.lang.RuntimeException

interface MyError
{
    val error_text: String
}

class MyUnknownError : RuntimeException(getStringMy(R.string.default_error)), MyError
{
    override val error_text: String
        get() = message!!
}

class ParsingError : RuntimeException(getStringMy(R.string.parsing_error)), MyError
{
    override val error_text: String
        get() = message!!
}

class NoInternetError : RuntimeException(getStringMy(R.string.no_internet_error)), MyError
{
    override val error_text: String
        get() = message!!
}

class MyServerError(str: String) : RuntimeException(str), MyError
{
    override val error_text: String
        get() = message!!
}