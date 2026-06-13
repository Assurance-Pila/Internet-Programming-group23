package com.netlinq.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.netlinq.data.local.dao.NetworkMetricDao;
import com.netlinq.data.local.dao.NetworkMetricDao_Impl;
import com.netlinq.data.local.dao.QoeFeedbackDao;
import com.netlinq.data.local.dao.QoeFeedbackDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NetLinqDatabase_Impl extends NetLinqDatabase {
  private volatile NetworkMetricDao _networkMetricDao;

  private volatile QoeFeedbackDao _qoeFeedbackDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `network_metrics` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `signalStrength` INTEGER, `signalQuality` INTEGER, `networkType` TEXT NOT NULL, `latencyMs` INTEGER, `deviceModel` TEXT NOT NULL, `androidVersion` TEXT NOT NULL, `recordedAt` INTEGER NOT NULL, `synced` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `qoe_feedback` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `overallRating` INTEGER NOT NULL, `responsivenessRating` INTEGER NOT NULL, `streamingRating` INTEGER NOT NULL, `callQualityRating` INTEGER NOT NULL, `satisfactionRating` INTEGER NOT NULL, `triggerEvent` TEXT, `networkType` TEXT, `networkMetricId` INTEGER, `metricRecordedAt` INTEGER, `signalStrengthSnapshot` INTEGER, `latencyMsSnapshot` INTEGER, `notes` TEXT, `recordedAt` INTEGER NOT NULL, `synced` INTEGER NOT NULL, FOREIGN KEY(`networkMetricId`) REFERENCES `network_metrics`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_qoe_feedback_networkMetricId` ON `qoe_feedback` (`networkMetricId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5b84416b24eda21d0b1c9c3ab03b3e96')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `network_metrics`");
        db.execSQL("DROP TABLE IF EXISTS `qoe_feedback`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsNetworkMetrics = new HashMap<String, TableInfo.Column>(9);
        _columnsNetworkMetrics.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNetworkMetrics.put("signalStrength", new TableInfo.Column("signalStrength", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNetworkMetrics.put("signalQuality", new TableInfo.Column("signalQuality", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNetworkMetrics.put("networkType", new TableInfo.Column("networkType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNetworkMetrics.put("latencyMs", new TableInfo.Column("latencyMs", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNetworkMetrics.put("deviceModel", new TableInfo.Column("deviceModel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNetworkMetrics.put("androidVersion", new TableInfo.Column("androidVersion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNetworkMetrics.put("recordedAt", new TableInfo.Column("recordedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNetworkMetrics.put("synced", new TableInfo.Column("synced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNetworkMetrics = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNetworkMetrics = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNetworkMetrics = new TableInfo("network_metrics", _columnsNetworkMetrics, _foreignKeysNetworkMetrics, _indicesNetworkMetrics);
        final TableInfo _existingNetworkMetrics = TableInfo.read(db, "network_metrics");
        if (!_infoNetworkMetrics.equals(_existingNetworkMetrics)) {
          return new RoomOpenHelper.ValidationResult(false, "network_metrics(com.netlinq.data.local.entity.NetworkMetricEntity).\n"
                  + " Expected:\n" + _infoNetworkMetrics + "\n"
                  + " Found:\n" + _existingNetworkMetrics);
        }
        final HashMap<String, TableInfo.Column> _columnsQoeFeedback = new HashMap<String, TableInfo.Column>(15);
        _columnsQoeFeedback.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQoeFeedback.put("overallRating", new TableInfo.Column("overallRating", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQoeFeedback.put("responsivenessRating", new TableInfo.Column("responsivenessRating", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQoeFeedback.put("streamingRating", new TableInfo.Column("streamingRating", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQoeFeedback.put("callQualityRating", new TableInfo.Column("callQualityRating", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQoeFeedback.put("satisfactionRating", new TableInfo.Column("satisfactionRating", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQoeFeedback.put("triggerEvent", new TableInfo.Column("triggerEvent", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQoeFeedback.put("networkType", new TableInfo.Column("networkType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQoeFeedback.put("networkMetricId", new TableInfo.Column("networkMetricId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQoeFeedback.put("metricRecordedAt", new TableInfo.Column("metricRecordedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQoeFeedback.put("signalStrengthSnapshot", new TableInfo.Column("signalStrengthSnapshot", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQoeFeedback.put("latencyMsSnapshot", new TableInfo.Column("latencyMsSnapshot", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQoeFeedback.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQoeFeedback.put("recordedAt", new TableInfo.Column("recordedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQoeFeedback.put("synced", new TableInfo.Column("synced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysQoeFeedback = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysQoeFeedback.add(new TableInfo.ForeignKey("network_metrics", "SET NULL", "NO ACTION", Arrays.asList("networkMetricId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesQoeFeedback = new HashSet<TableInfo.Index>(1);
        _indicesQoeFeedback.add(new TableInfo.Index("index_qoe_feedback_networkMetricId", false, Arrays.asList("networkMetricId"), Arrays.asList("ASC")));
        final TableInfo _infoQoeFeedback = new TableInfo("qoe_feedback", _columnsQoeFeedback, _foreignKeysQoeFeedback, _indicesQoeFeedback);
        final TableInfo _existingQoeFeedback = TableInfo.read(db, "qoe_feedback");
        if (!_infoQoeFeedback.equals(_existingQoeFeedback)) {
          return new RoomOpenHelper.ValidationResult(false, "qoe_feedback(com.netlinq.data.local.entity.QoeFeedbackEntity).\n"
                  + " Expected:\n" + _infoQoeFeedback + "\n"
                  + " Found:\n" + _existingQoeFeedback);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "5b84416b24eda21d0b1c9c3ab03b3e96", "68b1a4f6f926ea369f7173c5d53fa860");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "network_metrics","qoe_feedback");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `network_metrics`");
      _db.execSQL("DELETE FROM `qoe_feedback`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(NetworkMetricDao.class, NetworkMetricDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(QoeFeedbackDao.class, QoeFeedbackDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public NetworkMetricDao networkMetricDao() {
    if (_networkMetricDao != null) {
      return _networkMetricDao;
    } else {
      synchronized(this) {
        if(_networkMetricDao == null) {
          _networkMetricDao = new NetworkMetricDao_Impl(this);
        }
        return _networkMetricDao;
      }
    }
  }

  @Override
  public QoeFeedbackDao qoeFeedbackDao() {
    if (_qoeFeedbackDao != null) {
      return _qoeFeedbackDao;
    } else {
      synchronized(this) {
        if(_qoeFeedbackDao == null) {
          _qoeFeedbackDao = new QoeFeedbackDao_Impl(this);
        }
        return _qoeFeedbackDao;
      }
    }
  }
}
