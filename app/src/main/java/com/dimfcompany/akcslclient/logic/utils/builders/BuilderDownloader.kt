package com.dimfcompany.akcslclient.logic.utils.builders

import android.util.Log
import android.webkit.URLUtil
import androidx.lifecycle.viewModelScope
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.Constants
import com.dimfcompany.akcslclient.base.exceptions.MyError
import com.dimfcompany.akcslclient.base.exceptions.MyUnknownError
import com.dimfcompany.akcslclient.base.exceptions.NoInternetError
import com.dimfcompany.akcslclient.base.extensions.getBodyAsStr
import com.dimfcompany.akcslclient.base.extensions.isNetworkAvailable
import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import com.dimfcompany.akcslclient.logic.utils.files.FileManager
import com.dimfcompany.akcslclient.logic.utils.files.MyFileItem
import com.dimfcompany.akcslclient.networking.apis.ApiGlobal
import kotlinx.coroutines.*
import java.io.*
import java.lang.Exception
import java.lang.RuntimeException
import javax.inject.Inject

class BuilderDownloader()
{
    var file_destination: File? = null
        private set
    var url: String? = null
        private set
    var action_success: ((MyFileItem) -> Unit)? = null
        private set
    var action_error: ((Exception) -> Unit)? = null
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

    private var ins: InputStream? = null
    private var ops: OutputStream? = null

    @Inject
    lateinit var api_global: ApiGlobal

    init
    {
        AppClass.app_component.inject(this)
    }

    fun setFileDestination(file: File) = apply(
        {
            this.file_destination = file
        })

    fun setUrl(url: String) = apply(
        {
            this.url = url
        })

    fun setActionSuccess(action: ((MyFileItem) -> Unit)) = apply(
        {
            this.action_success = action
        })

    fun setActionError(action: ((Exception) -> Unit)) = apply(
        {
            this.action_error = action
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
                if (ins != null)
                {
                    try
                    {
                        ins?.close()
                    }
                    catch (ioex: IOException)
                    {
                        Log.e("FileManager", "Exception : " + ioex.message);
                    }

                }

                if (ops != null)
                {
                    try
                    {
                        ops?.close()
                    }
                    catch (ioex: IOException)
                    {
                        Log.e("FileManager", "Exception : " + ioex.message);
                    }
                }

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
        })

    fun run()
    {
        if (url == null)
        {
            throw RuntimeException("")
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

            if (file_destination == null)
            {
                val name = URLUtil.guessFileName(url, null, null)
                file_destination = FileManager.createFile(name, null, Constants.FOLDER_TEMP_FILES)
            }

            launch(Dispatchers.IO, block =
            {
                val reponse_body = api_global.downloadFile(url!!).body()
                if (reponse_body == null)
                {
                    throw MyUnknownError()
                }
                ins = reponse_body.byteStream()
                ops = FileOutputStream(file_destination!!)
                FileManager.copy(ins!!, ops!!)

                val my_file_item = MyFileItem.createFromFile(file_destination!!)

                action_success!!.invoke(my_file_item)
                finally_action()
            })
        })
    }
}
