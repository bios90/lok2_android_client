package com.dimfcompany.akcslclient.networking

import com.dimfcompany.akcsl.logic.models.ModelCategory
import com.dimfcompany.akcsl.logic.models.ModelDocument
import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import com.dimfcompany.akcslclient.logic.models.ModelUser
import com.dimfcompany.akcslclient.logic.models.responses.*
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderNet
import com.dimfcompany.akcslclient.logic.utils.files.MyFileItem
import com.dimfcompany.akcslclient.logic.utils.files.toBase64
import java.lang.Exception

class BaseNetworker(private val base_vm: BaseViewModel)
{
    fun makeRegister(first_name: String, last_name: String, email: String, password: String, avatar: MyFileItem?, action_success: () -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        BuilderNet<RespUserSingle>()
                .setActionResponseBody(
                    {
                        val avatar_as_base64 = avatar?.getFile()?.toBase64(true)
                        base_vm.api_auth.register(first_name, last_name, email, password, avatar_as_base64)
                    })
                .setObjClass(RespUserSingle::class.java)
                .setBaseVm(base_vm)
                .setActionParseChecker(
                    {
                        it.user != null
                    })
                .setActionSuccess(
                    {
                        action_success()
                    })
                .setActionError(
                    {
                        action_error?.invoke(it)
                    })
                .run()
    }

    fun makePassReset(email: String, action_success: () -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        BuilderNet<BaseResponse>()
                .setActionResponseBody(
                    {
                        base_vm.api_auth.forgotPass(email)
                    })
                .setObjClass(BaseResponse::class.java)
                .setBaseVm(base_vm)
                .setActionSuccess(
                    {
                        action_success()
                    })
                .setActionError(
                    {
                        action_error?.invoke(it)
                    })
                .run()
    }

    fun makeLogin(email: String, password: String, fb_token: String?, action_success: (ModelUser) -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        BuilderNet<RespUserSingle>()
                .setActionResponseBody(
                    {
                        base_vm.api_auth.login(email, password, fb_token)
                    })
                .setObjClass(RespUserSingle::class.java)
                .setBaseVm(base_vm)
                .setActionParseChecker(
                    {
                        it.user != null
                    })
                .setActionSuccess(
                    {
                        action_success(it.user!!)
                    })
                .setActionError(
                    {
                        action_error?.invoke(it)
                    })
                .run()
    }

    fun getAllCategories(action_success: (ArrayList<ModelCategory>) -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        BuilderNet<RespCategories>()
                .setActionResponseBody(
                    {
                        base_vm.api_categories.getAllCategories()
                    })
                .setObjClass(RespCategories::class.java)
                .setBaseVm(base_vm)
                .setActionParseChecker(
                    {
                        it.categories != null
                    })
                .setActionSuccess(
                    {
                        action_success(it.categories!!)
                    })
                .setActionError(
                    {
                        action_error?.invoke(it)
                    })
                .run()
    }

    fun updateUserInfo(user_id: Long, first_name: String?, last_name: String?, password: String?, avatar: MyFileItem?, action_success: (ModelUser) -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        BuilderNet<RespUserSingle>()
                .setActionResponseBody(
                    {
                        val avatar_as_base64 = avatar?.getFile()?.toBase64(true)
                        base_vm.api_auth.updateUserInfo(user_id, first_name, last_name, password, avatar_as_base64)
                    })
                .setObjClass(RespUserSingle::class.java)
                .setBaseVm(base_vm)
                .setActionParseChecker(
                    {
                        it.user != null
                    })
                .setActionSuccess(
                    {
                        action_success(it.user!!)
                    })
                .setActionError(
                    {
                        action_error?.invoke(it)
                    })
                .run()
    }


    fun getUserById(user_id: Long, action_success: (ModelUser) -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        BuilderNet<RespUserSingle>()
                .setActionResponseBody(
                    {
                        base_vm.api_auth.getUserById(user_id)
                    })
                .setObjClass(RespUserSingle::class.java)
                .setBaseVm(base_vm)
                .setActionParseChecker(
                    {
                        it.user != null
                    })
                .setActionSuccess(
                    {
                        action_success(it.user!!)
                    })
                .setActionError(
                    {
                        action_error?.invoke(it)
                    })
                .run()
    }

    fun getCategoryById(category_id: Long, action_success: (ModelCategory) -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        BuilderNet<RespCategorySingle>()
                .setActionResponseBody(
                    {
                        base_vm.api_categories.getCategoryById(category_id)
                    })
                .setObjClass(RespCategorySingle::class.java)
                .setBaseVm(base_vm)
                .setActionParseChecker(
                    {
                        it.category != null
                    })
                .setActionSuccess(
                    {
                        action_success(it.category!!)
                    })
                .setActionError(
                    {
                        action_error?.invoke(it)
                    })
                .run()
    }

    fun getDocuments(category_id: Long?, search: String?, action_success: (ArrayList<ModelDocument>) -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        BuilderNet<RespDocuments>()
                .setActionResponseBody(
                    {
                        base_vm.api_categories.getDocuments(category_id, search)
                    })
                .setObjClass(RespDocuments::class.java)
                .setBaseVm(base_vm)
                .setActionParseChecker(
                    {
                        it.documents != null
                    })
                .setActionSuccess(
                    {
                        action_success(it.documents!!)
                    })
                .setActionError(
                    {
                        action_error?.invoke(it)
                    })
                .run()
    }

    fun getDocumentByMultiIds(ids: String, action_success: (ArrayList<ModelDocument>) -> Unit, action_error: ((Throwable) -> Unit)? = null)
    {
        BuilderNet<RespDocuments>()
                .setActionResponseBody(
                    {
                        base_vm.api_categories.getDocumentByMultiIds(ids)
                    })
                .setObjClass(RespDocuments::class.java)
                .setBaseVm(base_vm)
                .setActionParseChecker(
                    {
                        it.documents != null
                    })
                .setActionSuccess(
                    {
                        action_success(it.documents!!)
                    })
                .setActionError(
                    {
                        action_error?.invoke(it)
                    })
                .run()
    }
}