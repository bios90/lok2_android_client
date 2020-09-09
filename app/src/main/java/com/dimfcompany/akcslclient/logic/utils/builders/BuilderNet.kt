package com.dimfcompany.akcslclient.logic.utils.builders

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dimfcompany.akcslclient.base.exceptions.*
import com.dimfcompany.akcslclient.base.extensions.getBodyAsStr
import com.dimfcompany.akcslclient.base.extensions.isNetworkAvailable
import com.dimfcompany.akcslclient.base.extensions.toObjOrNullGson
import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import com.dimfcompany.akcslclient.logic.models.responses.BaseResponse
import com.dimfcompany.akcslclient.logic.utils.files.MyFileItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.reflect.Type

class BuilderNet<T : Any>
{
    var action_response_body: (suspend () -> Response<ResponseBody>)? = null
        private set
    var obj_class: Class<T>? = null
        private set
    var obj_type: Type? = null
        private set
    var action_success: ((T) -> Unit)? = null
        private set
    var action_error: ((Throwable) -> Unit)? = null
        private set
    var action_parse_checker: ((T) -> Boolean)? = null
        private set
    var disable_screen: Boolean = true
        private set
    var show_progress: Boolean = true
        private set
    var add_error_cather: Boolean = true
        private set
    var base_vm: BaseViewModel? = null
        private set
    var scope: CoroutineScope? = null
        private set

    fun setActionResponseBody(action_response_body: (suspend () -> Response<ResponseBody>)) = apply(
        {
            this.action_response_body = action_response_body
        })

    fun setObjClass(obj_class: Class<T>) = apply(
        {
            this.obj_class = obj_class
        })

    fun setObjType(obj_type: Type) = apply(
        {
            this.obj_type = obj_type
        })

    fun setActionSuccess(action: ((T) -> Unit)) = apply(
        {
            this.action_success = action
        })

    fun setActionError(action: ((Throwable) -> Unit)) = apply(
        {
            this.action_error = action
        })

    fun setActionParseChecker(action: ((T) -> Boolean)) = apply(
        {
            this.action_parse_checker = action
        })

    fun setDisableScreen(disable_screen: Boolean) = apply(
        {
            this.disable_screen = disable_screen
        })

    fun setShowProgress(show_progress: Boolean) = apply(
        {
            this.show_progress = show_progress
        })

    fun setAddErrorCathcer(add_error_cather: Boolean) = apply(
        {
            this.add_error_cather = add_error_cather
        })

    fun setBaseVm(base_vm: BaseViewModel) = apply(
        {
            this.base_vm = base_vm
        })

    fun setScope(scope: CoroutineScope) = apply(
        {
            this.scope = scope
        })

    private var finally_action =
            {
                if (base_vm != null)
                {
                    if (show_progress)
                    {
                        scope!!.launch(Dispatchers.Main, block =
                        {
                            base_vm!!.ps_show_hide_progress.onNext(false)
                        })
                    }

                    if (disable_screen)
                    {
                        scope!!.launch(Dispatchers.Main, block =
                        {
                            base_vm!!.ps_disable_screen_touches.onNext(false)
                        })
                    }
                }
            }

    private var exception_handler = CoroutineExceptionHandler(
        { context, throwable ->

            throwable.printStackTrace()

            if (!add_error_cather)
            {
                return@CoroutineExceptionHandler
            }

            if (base_vm == null)
            {
                return@CoroutineExceptionHandler
            }

            val message: String
            if (throwable is MyError)
            {
                message = throwable.error_text
            }
            else
            {
                message = "Неизвестная ошибка"
            }


            val builder = BuilderAlerter.getRedBuilder(message)
            scope!!.launch(Dispatchers.Main, block =
            {
                base_vm!!.ps_to_show_alerter.onNext(builder)
            })

            finally_action()

            if(action_error != null)
            {
                action_error?.invoke(throwable)
            }
        })


    fun run()
    {

        if (action_success == null || obj_class == null || action_response_body == null)
        {
            throw RuntimeException("**** No needed data setted 1 ****")
        }

        if ((disable_screen != false || show_progress != false || add_error_cather != false) && base_vm == null)
        {
            throw RuntimeException("**** No Base vm setted ****")
        }

        if (scope == null)
        {
            scope = base_vm?.viewModelScope
        }

        if (scope == null)
        {
            throw RuntimeException("**** No Scope setted ****")
        }

        scope!!.launch(exception_handler, block =
        {
            if (!isNetworkAvailable())
            {
                throw NoInternetError()
            }

            if (show_progress)
            {
                launch(Dispatchers.Main, block =
                {
                    base_vm!!.ps_show_hide_progress.onNext(true)
                }).join()
            }

            if (disable_screen)
            {
                launch(Dispatchers.Main, block =
                {
                    base_vm!!.ps_disable_screen_touches.onNext(true)
                }).join()
            }

            val response_as_str = async(Dispatchers.IO, block =
            {
                action_response_body!!().getBodyAsStr()
            }).await()


            if (response_as_str == null)
            {
                throw MyUnknownError()
            }

            val base_response = response_as_str.toObjOrNullGson(BaseResponse::class.java)

            if (base_response == null)
            {
                throw ParsingError()
            }

            if (base_response.isFailed())
            {
                val message = base_response.getErrorMessage()
                throw MyServerError(message)
            }

            val obj = response_as_str.toObjOrNullGson(obj_class!!)

            if (obj == null)
            {
                throw ParsingError()
            }

            if (action_parse_checker != null)
            {
                if (!action_parse_checker!!(obj))
                {
                    throw ParsingError()
                }
            }

            action_success!!(obj)

            finally_action()
        })
    }
}