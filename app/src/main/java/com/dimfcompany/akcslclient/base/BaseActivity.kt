package com.dimfcompany.akcslclient.base

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.extensions.*
import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import com.dimfcompany.akcslclient.base.mvvm.MyVmFactory
import com.dimfcompany.akcslclient.di.activity.ComponentActivity
import com.dimfcompany.akcslclient.di.activity.ModuleActivity
import com.dimfcompany.akcslclient.logic.MessagesManager
import com.justordercompany.barista.logic.images.ImageCameraManager
import com.justordercompany.barista.logic.utils.PermissionManager
import com.justordercompany.barista.ui.dialogs.DialogBottomSheetRounded
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception
import java.lang.RuntimeException
import javax.inject.Inject

open class BaseActivity : AppCompatActivity()
{
    @Inject
    lateinit var my_vm_factory: MyVmFactory

    @Inject
    lateinit var messages_manager: MessagesManager

    @Inject
    lateinit var permission_manager: PermissionManager

    @Inject
    lateinit var image_camera_manager: ImageCameraManager

    var color_status_bar: Int = getColorMy(R.color.red)
    var is_light_status_bar: Boolean = false
    var color_nav_bar: Int = getColorMy(R.color.red)
    var is_light_nav_bar: Boolean = false
    var is_full_screen: Boolean = false
    var is_below_nav_bar: Boolean = false

    val composite_disposable = CompositeDisposable()

    private var base_vms: ArrayList<BaseViewModel> = arrayListOf()

    fun getActivityComponent(): ComponentActivity
    {
        return AppClass.app_component.getActivityComponent(ModuleActivity(this))
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        makePortrait()
        applyStatusNavColors()
        super.onCreate(savedInstanceState)
    }

    override fun onResume()
    {
        super.onResume()
        AppClass.top_activity = this
    }

    override fun onPause()
    {
        clearTopActivity()
        super.onPause()
    }

    override fun onDestroy()
    {
        base_vms.clear()
        composite_disposable.dispose()
        clearTopActivity()
        super.onDestroy()
    }

    private fun clearTopActivity()
    {
        val current_top = AppClass.top_activity
        if (this.equals(current_top))
        {
            AppClass.top_activity = null
        }
    }

    fun applyStatusNavColors()
    {
        if (is_full_screen)
        {
            this.window?.makeFullScreen(is_below_nav_bar)
        }

        this.window?.setStatusBarColorMy(color_status_bar)
        this.window?.setNavBarColor(color_nav_bar)
        this.window?.setStatusLightDark(is_light_status_bar)
        this.window?.setNavBarLightDark(is_light_nav_bar)
    }

    fun setBaseVmActions(base_vm: BaseViewModel)
    {
        if (base_vms.contains(base_vm))
        {
            throw RuntimeException("**** Error base vm actions alredy setted")
        }

        base_vms.add(base_vm)

        base_vm.ps_show_hide_progress
                .mainThreaded()
                .subscribe(
                    {
                        if (it)
                        {
                            messages_manager.showProgressDialog()
                        }
                        else
                        {
                            messages_manager.dismissProgressDialog()
                        }
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_to_show_alerter
                .mainThreaded()
                .subscribe(
                    {

                        it.show(this)
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_disable_screen_touches
                .mainThreaded()
                .subscribe(
                    {
                        if (it)
                        {
                            messages_manager.disableScreenTouches()
                        }
                        else
                        {
                            messages_manager.enableScreenTouches(false)
                        }
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_intent_builded
                .mainThreaded()
                .subscribe(
                    {
                        it.startActivity(this)
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_to_finish
                .mainThreaded()
                .subscribe(
                    {
                        if (it.value != null)
                        {
                            setResult(Activity.RESULT_OK, it.value)
                        }
                        finish()
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_to_show_bottom_dialog
                .mainThreaded()
                .subscribe(
                    {

                        val dialog = DialogBottomSheetRounded(it)
                        dialog.show(this.supportFragmentManager, null)
                        dialog.isCancelable = it.cancel_on_touch_outside
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_request_permissions
                .mainThreaded()
                .subscribe(
                    {
                        it.build(this)
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_to_show_dialog
                .mainThreaded()
                .subscribe(
                    {
                        it.build(this)
                    })
                .disposeBy(composite_disposable)

        base_vm.ps_pick_action
                .mainThreaded()
                .subscribe(
                    {
                        when (it)
                        {
                            ImageCameraManager.TypePick.CAMERA_IMAGE ->
                            {
                                image_camera_manager.pickCameraImage(it.action_success)
                            }

                            ImageCameraManager.TypePick.GALLERY_IMAGE ->
                            {
                                image_camera_manager.pickGalleryImage(it.action_success)
                            }

                            ImageCameraManager.TypePick.PDF ->
                            {
                                image_camera_manager.pickPdfFile(it.action_success, it.action_error)
                            }
                        }
                    })
                .disposeBy(composite_disposable)

        base_vm.intent_extra = intent

        base_vm.viewAttached()
    }

    protected fun makePortrait()
    {
        try
        {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        catch (e: Exception)
        {

        }
    }
}