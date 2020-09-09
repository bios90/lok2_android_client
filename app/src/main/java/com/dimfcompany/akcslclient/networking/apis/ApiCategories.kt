package com.dimfcompany.akcslclient.networking.apis

import com.dimfcompany.akcslclient.base.Constants
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ApiCategories
{
    @GET(Constants.Urls.GET_CATEGORIES)
    suspend fun getAllCategories(): Response<ResponseBody>

    @GET(Constants.Urls.GET_CATEGORIY_BY_ID)
    suspend fun getCategoryById(@Query("category_id") category_id: Long): Response<ResponseBody>

    @GET(Constants.Urls.GET_DOCUMENTS)
    suspend fun getDocuments(
            @Query("category_id") category_id: Long? = null,
            @Query("search") search: String? = null
    ): Response<ResponseBody>

    @GET(Constants.Urls.GET_DOCUMENT_BY_MULTI_IDS)
    suspend fun getDocumentByMultiIds(
            @Query("ids") ids: String
    ): Response<ResponseBody>
}