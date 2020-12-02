package com.dimfcompany.akcslclient.base


class Constants
{
    companion object
    {
        const val SHARED_PREFS = "SharedPrefs"
        const val EXTENSION_PNG = "png"
        const val EXTENSION_JPEG = "jpeg"

        const val FOLDER_APP_ROOT = "ak_csl_client"
        const val FOLDER_TEMP_FILES = FOLDER_APP_ROOT + "/temp_files"
        const val FOLDER_PDFS= FOLDER_APP_ROOT + "/pdfs"
    }

    object Urls
    {
        const val test_mode = false
        val URL_BASE: String
            get()
            {
                if (test_mode)
                {
                    return "http://192.168.1.68/akcsl/"
                }
                else
                {
                    return "https://ak-hr.ru/"
                }
            }

        const val REGISTER = "register"
        const val LOGIN = "login"
        const val FORGOT_PASS = "forgotpass"
        const val UPDATE_USER_INFO = "update_user_info"
        const val GET_CATEGORIES = "get_categories"
        const val GET_CATEGORIY_BY_ID = "get_category_by_id"
        const val GET_USER_BY_ID = "get_user_by_id"
        const val GET_DOCUMENTS = "get_documents"
        const val GET_DOCUMENT_BY_MULTI_IDS = "get_by_multi_ids"
    }

    object Extras
    {
        const val REGISTER_MADE = "register_made"
        const val EMAIL = "email"
        const val CATEGORY = "category"
        const val CATEGORY_ID = "category_id"
        const val DOCUMENT_ID = "document_id"
        const val FILTER_DATA = "filter_data"
        const val MY_PUSH = "extra_push"
    }
}