package by.bsuir.myapplication

import android.app.Application
import by.bsuir.myapplication.database.entity.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(appModule)
        }
    }
}