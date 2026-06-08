package com.netlinq.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.netlinq.data.local.NetLinqDatabase
import com.netlinq.data.local.dao.NetworkMetricDao
import com.netlinq.data.local.dao.QoeFeedbackDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NetLinqDatabase =
        Room.databaseBuilder(
            context,
            NetLinqDatabase::class.java,
            "netlinq.db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE qoe_feedback ADD COLUMN networkMetricId INTEGER")
            db.execSQL("ALTER TABLE qoe_feedback ADD COLUMN metricRecordedAt INTEGER")
            db.execSQL("ALTER TABLE qoe_feedback ADD COLUMN signalStrengthSnapshot INTEGER")
            db.execSQL("ALTER TABLE qoe_feedback ADD COLUMN latencyMsSnapshot INTEGER")
        }
    }

    @Provides
    fun provideNetworkMetricDao(database: NetLinqDatabase): NetworkMetricDao =
        database.networkMetricDao()

    @Provides
    fun provideQoeFeedbackDao(database: NetLinqDatabase): QoeFeedbackDao =
        database.qoeFeedbackDao()
}
