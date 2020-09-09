package com.dimfcompany.akcslclient.di.application

import com.dimfcompany.akcslclient.base.BusMainEvents
import com.dimfcompany.akcslclient.logic.utils.NotificationManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ModuleGlobal
{
    @Singleton
    @Provides
    fun provideBusMainEvents(): BusMainEvents
    {
        return BusMainEvents()
    }

    @Singleton
    @Provides
    fun provideNotificationManager():NotificationManager
    {
        return NotificationManager();
    }
}