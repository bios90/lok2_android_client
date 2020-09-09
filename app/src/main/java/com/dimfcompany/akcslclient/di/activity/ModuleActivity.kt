package com.dimfcompany.akcslclient.di.activity

import androidx.appcompat.app.AppCompatActivity
import com.dimfcompany.akcslclient.base.mvvm.MyVmFactory
import com.dimfcompany.akcslclient.logic.MessagesManager
import com.justordercompany.barista.logic.images.ImageCameraManager
import com.justordercompany.barista.logic.utils.PermissionManager
import dagger.Module
import dagger.Provides

@Module
class ModuleActivity(private val activity: AppCompatActivity)
{
    private val messagesManager: MessagesManager

    init
    {
        messagesManager = MessagesManager(activity)
    }

    @Provides
    fun provideActivity(): AppCompatActivity
    {
        return activity
    }

    @Provides
    fun provideMyVmFactory(): MyVmFactory
    {
        return MyVmFactory(activity)
    }

    @Provides
    fun provideMessagesManager(): MessagesManager
    {
        return messagesManager
    }

    @Provides
    fun providePermissionManager(): PermissionManager
    {
        return PermissionManager(activity)
    }

    @Provides
    fun provideImageCameraManager(): ImageCameraManager
    {
        return ImageCameraManager(activity,messagesManager)
    }
}