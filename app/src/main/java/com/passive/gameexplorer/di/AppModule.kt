package com.passive.gameexplorer.di

import com.passive.gameexplorer.repository.DeviceIdRepository
import com.passive.gameexplorer.repository.ProfileRepository
import android.content.Context
import com.passive.gameexplorer.repository.GameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    fun providesIoDispatcher() = Dispatchers.IO

    /*  @Singleton
      @Provides
      fun providesAppDB(@ApplicationContext context: Context): AppDatabase {
          return Room.databaseBuilder(
              context,
              AppDatabase::class.java,
              "typing_job.db"
          ).build()
      }*/


    /*    @Provides
        fun providesJobsRepository(
            appDatabase: AppDatabase,
            ioDispatcher: CoroutineDispatcher
        ): JobsRepository = JobsRepositoryImpl(ioDispatcher, appDatabase.jobsDao())*/

    @Provides
    fun providesGameRepository(): GameRepository {
        return GameRepository()
    }

    @Provides
    fun providesProfileRepository(): ProfileRepository {
        return ProfileRepository()
    }

    @Provides
    fun providesDeviceIdRepository(@ApplicationContext context: Context): DeviceIdRepository {
        return DeviceIdRepository(context)
    }


}