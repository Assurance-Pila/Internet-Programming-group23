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
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE qoe_feedback ADD COLUMN networkMetricId INTEGER")
            db.execSQL("ALTER TABLE qoe_feedback ADD COLUMN metricRecordedAt INTEGER")
            db.execSQL("ALTER TABLE qoe_feedback ADD COLUMN signalStrengthSnapshot INTEGER")
            db.execSQL("ALTER TABLE qoe_feedback ADD COLUMN latencyMsSnapshot INTEGER")
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS qoe_feedback_new (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    overallRating INTEGER NOT NULL,
                    responsivenessRating INTEGER NOT NULL,
                    streamingRating INTEGER NOT NULL,
                    callQualityRating INTEGER NOT NULL,
                    satisfactionRating INTEGER NOT NULL,
                    triggerEvent TEXT,
                    networkType TEXT,
                    networkMetricId INTEGER,
                    metricRecordedAt INTEGER,
                    signalStrengthSnapshot INTEGER,
                    latencyMsSnapshot INTEGER,
                    notes TEXT,
                    recordedAt INTEGER NOT NULL,
                    synced INTEGER NOT NULL,
                    FOREIGN KEY(networkMetricId) REFERENCES network_metrics(id) ON DELETE SET NULL
                )
                """.trimIndent()
            )
            db.execSQL(
                """
                INSERT INTO qoe_feedback_new (
                    id, overallRating, responsivenessRating, streamingRating,
                    callQualityRating, satisfactionRating, triggerEvent, networkType,
                    networkMetricId, metricRecordedAt, signalStrengthSnapshot,
                    latencyMsSnapshot, notes, recordedAt, synced
                )
                SELECT
                    id, overallRating, responsivenessRating, streamingRating,
                    callQualityRating, satisfactionRating, triggerEvent, networkType,
                    networkMetricId, metricRecordedAt, signalStrengthSnapshot,
                    latencyMsSnapshot, notes, recordedAt, synced
                FROM qoe_feedback
                """.trimIndent()
            )
            db.execSQL("DROP TABLE qoe_feedback")
            db.execSQL("ALTER TABLE qoe_feedback_new RENAME TO qoe_feedback")
            db.execSQL("CREATE INDEX IF NOT EXISTS index_qoe_feedback_networkMetricId ON qoe_feedback (networkMetricId)")
        }
    }

    @Provides
    fun provideNetworkMetricDao(database: NetLinqDatabase): NetworkMetricDao =
        database.networkMetricDao()

    @Provides
    fun provideQoeFeedbackDao(database: NetLinqDatabase): QoeFeedbackDao =
        database.qoeFeedbackDao()
}
