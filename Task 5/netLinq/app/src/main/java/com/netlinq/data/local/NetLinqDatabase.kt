package com.netlinq.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.netlinq.data.local.dao.NetworkMetricDao
import com.netlinq.data.local.dao.QoeFeedbackDao
import com.netlinq.data.local.entity.NetworkMetricEntity
import com.netlinq.data.local.entity.QoeFeedbackEntity

@Database(
    entities = [NetworkMetricEntity::class, QoeFeedbackEntity::class],
    version = 2,
    exportSchema = false
)
abstract class NetLinqDatabase : RoomDatabase() {
    abstract fun networkMetricDao(): NetworkMetricDao
    abstract fun qoeFeedbackDao(): QoeFeedbackDao
}
