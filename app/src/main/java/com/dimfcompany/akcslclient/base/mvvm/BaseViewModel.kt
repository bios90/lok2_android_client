package com.dimfcompany.akcslclient.base.mvvm

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.BusMainEvents
import com.dimfcompany.akcslclient.base.extensions.Optional
import com.dimfcompany.akcslclient.base.extensions.disposeBy
import com.dimfcompany.akcslclient.base.extensions.getStringMy
import com.dimfcompany.akcslclient.logic.BtnAction
import com.dimfcompany.akcslclient.logic.MessagesManager
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderAlerter
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderDialogBottom
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderDialogMy
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderIntent
import com.dimfcompany.akcslclient.logic.utils.files.MyFileItem
import com.dimfcompany.akcslclient.networking.BaseNetworker
import com.dimfcompany.akcslclient.networking.apis.ApiAuth
import com.dimfcompany.akcslclient.networking.apis.ApiCategories
import com.justordercompany.barista.logic.images.ImageCameraManager
import com.justordercompany.barista.logic.utils.BuilderPermRequest
import com.justordercompany.barista.logic.utils.PermissionManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

open class BaseViewModel : ViewModel()
{
    @Inject
    lateinit var bus_main_events: BusMainEvents

    @Inject
    lateinit var api_auth: ApiAuth

    @Inject
    lateinit var api_categories: ApiCategories

    val composite_disposable = CompositeDisposable()

    val base_networker = BaseNetworker(this)
    val ps_intent_builded: PublishSubject<BuilderIntent> = PublishSubject.create()
    val ps_to_show_alerter: PublishSubject<BuilderAlerter> = PublishSubject.create()
    val ps_show_hide_progress: PublishSubject<Boolean> = PublishSubject.create()
    val ps_disable_screen_touches: PublishSubject<Boolean> = PublishSubject.create()
    val bs_is_keyboard_visible: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    val ps_to_finish: PublishSubject<Optional<Intent>> = PublishSubject.create()
    val ps_to_show_bottom_dialog: PublishSubject<BuilderDialogBottom> = PublishSubject.create()
    val ps_request_permissions: PublishSubject<BuilderPermRequest> = PublishSubject.create()
    val ps_to_show_dialog: PublishSubject<BuilderDialogMy> = PublishSubject.create()
    val ps_pick_action: PublishSubject<ImageCameraManager.TypePick> = PublishSubject.create()
    lateinit var intent_extra: Intent

    open fun viewAttached()
    {
        Log.e("BaseViewModel", "**** viewAttached Base Implementation ****")
    }


    fun setBaseVmEvents()
    {

    }

    fun checkPermissions(permissions: List<String>, action_success: () -> Unit)
    {
        val text_blocked = getStringMy(R.string.for_full_work_need_permissions)
        BuilderPermRequest()
                .setPermissions(permissions)
                .setActionBlockedNow(
                    {
                        val dialog = MessagesManager.getBuilderPermissionsBlockedNow(text_blocked,
                            {
                                checkPermissions(permissions, action_success)
                            })
                        ps_to_show_dialog.onNext(dialog)
                    })
                .setActionBlockedFinally(
                    {
                        val dialog = MessagesManager.getBuilderPermissionsBlockedFinally(text_blocked)
                        ps_to_show_dialog.onNext(dialog)
                    })
                .setActionAvailable(
                    {
                        action_success()
                    })
                .sendInVm(this)
    }

    fun checkAndPickImage(action_picked: (MyFileItem) -> Unit)
    {
        checkPermissions(PermissionManager.permissions_camera,
            {
                BuilderDialogBottom()
                        .addBtn(BtnAction(getStringMy(R.string.gallery),
                            {
                                val pick = ImageCameraManager.TypePick.GALLERY_IMAGE
                                pick.action_success = action_picked
                                ps_pick_action.onNext(pick)
                            }, getStringMy(R.string.faw_images)))
                        .addBtn(BtnAction(getStringMy(R.string.camera),
                            {
                                val pick = ImageCameraManager.TypePick.CAMERA_IMAGE
                                pick.action_success = action_picked
                                ps_pick_action.onNext(pick)
                            }, getStringMy(R.string.faw_camera)))
                        .sendInVm(this)
            })
    }

    fun showPrivacyPolicy()
    {
        BuilderDialogMy()
                .setViewId(R.layout.la_dialog_scrollable_tv)
                .setTitle(getStringMy(R.string.privacy_policy))
                .setText(getStringMy(R.string.privacy_policy_text))
                .setBtnOk(BtnAction(getStringMy(R.string.its_clear), {}))
                .sendInVm(this)
    }
}