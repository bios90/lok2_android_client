package com.dimfcompany.akcslclient.networking.apis

import com.dimfcompany.akcslclient.base.Constants
import com.dimfcompany.akcslclient.base.extensions.getDeviceName
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiAuth
{
    @FormUrlEncoded
    @POST(Constants.Urls.REGISTER)
    suspend fun register(
            @Field("first_name") first_name: String,
            @Field("last_name") last_name: String,
            @Field("email") email: String,
            @Field("password") password: String,
            @Field("avatar") avatar: String?
    ): Response<ResponseBody>

    @FormUrlEncoded
    @POST(Constants.Urls.LOGIN)
    suspend fun login(
            @Field("email") email: String,
            @Field("password") password: String,
            @Field("fb_token") fb_token: String?,
            @Field("env") env: String = "android",
            @Field("device_name") device_name: String? = getDeviceName()
    ): Response<ResponseBody>

    @FormUrlEncoded
    @POST(Constants.Urls.FORGOT_PASS)
    suspend fun forgotPass(
            @Field("email") email: String
    ): Response<ResponseBody>

    @FormUrlEncoded
    @POST(Constants.Urls.UPDATE_USER_INFO)
    suspend fun updateUserInfo(
            @Field("user_id") user_id: Long?,
            @Field("first_name") first_name: String?,
            @Field("last_name") last_name: String?,
            @Field("password") password: String?,
            @Field("avatar") avatar: String?
    ): Response<ResponseBody>

    @GET(Constants.Urls.GET_USER_BY_ID)
    suspend fun getUserById(
            @Query("user_id") search: Long
    ): Response<ResponseBody>
}