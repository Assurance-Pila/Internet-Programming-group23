package com.netlinq.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.netlinq.data.local.entity.NetworkMetricEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NetworkMetricDao_Impl implements NetworkMetricDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<NetworkMetricEntity> __insertionAdapterOfNetworkMetricEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSyncedOlderThan;

  public NetworkMetricDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNetworkMetricEntity = new EntityInsertionAdapter<NetworkMetricEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `network_metrics` (`id`,`signalStrength`,`signalQuality`,`networkType`,`latencyMs`,`deviceModel`,`androidVersion`,`recordedAt`,`synced`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NetworkMetricEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getSignalStrength() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getSignalStrength());
        }
        if (entity.getSignalQuality() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getSignalQuality());
        }
        statement.bindString(4, entity.getNetworkType());
        if (entity.getLatencyMs() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getLatencyMs());
        }
        statement.bindString(6, entity.getDeviceModel());
        statement.bindString(7, entity.getAndroidVersion());
        statement.bindLong(8, entity.getRecordedAt());
        final int _tmp = entity.getSynced() ? 1 : 0;
        statement.bindLong(9, _tmp);
      }
    };
    this.__preparedStmtOfDeleteSyncedOlderThan = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM network_metrics WHERE synced = 1 AND recordedAt < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final NetworkMetricEntity metric,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfNetworkMetricEntity.insertAndReturnId(metric);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSyncedOlderThan(final long cutoff,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSyncedOlderThan.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cutoff);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteSyncedOlderThan.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<NetworkMetricEntity>> observeAll() {
    final String _sql = "SELECT * FROM network_metrics ORDER BY recordedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"network_metrics"}, new Callable<List<NetworkMetricEntity>>() {
      @Override
      @NonNull
      public List<NetworkMetricEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSignalStrength = CursorUtil.getColumnIndexOrThrow(_cursor, "signalStrength");
          final int _cursorIndexOfSignalQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "signalQuality");
          final int _cursorIndexOfNetworkType = CursorUtil.getColumnIndexOrThrow(_cursor, "networkType");
          final int _cursorIndexOfLatencyMs = CursorUtil.getColumnIndexOrThrow(_cursor, "latencyMs");
          final int _cursorIndexOfDeviceModel = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceModel");
          final int _cursorIndexOfAndroidVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "androidVersion");
          final int _cursorIndexOfRecordedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "recordedAt");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<NetworkMetricEntity> _result = new ArrayList<NetworkMetricEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NetworkMetricEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Integer _tmpSignalStrength;
            if (_cursor.isNull(_cursorIndexOfSignalStrength)) {
              _tmpSignalStrength = null;
            } else {
              _tmpSignalStrength = _cursor.getInt(_cursorIndexOfSignalStrength);
            }
            final Integer _tmpSignalQuality;
            if (_cursor.isNull(_cursorIndexOfSignalQuality)) {
              _tmpSignalQuality = null;
            } else {
              _tmpSignalQuality = _cursor.getInt(_cursorIndexOfSignalQuality);
            }
            final String _tmpNetworkType;
            _tmpNetworkType = _cursor.getString(_cursorIndexOfNetworkType);
            final Integer _tmpLatencyMs;
            if (_cursor.isNull(_cursorIndexOfLatencyMs)) {
              _tmpLatencyMs = null;
            } else {
              _tmpLatencyMs = _cursor.getInt(_cursorIndexOfLatencyMs);
            }
            final String _tmpDeviceModel;
            _tmpDeviceModel = _cursor.getString(_cursorIndexOfDeviceModel);
            final String _tmpAndroidVersion;
            _tmpAndroidVersion = _cursor.getString(_cursorIndexOfAndroidVersion);
            final long _tmpRecordedAt;
            _tmpRecordedAt = _cursor.getLong(_cursorIndexOfRecordedAt);
            final boolean _tmpSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp != 0;
            _item = new NetworkMetricEntity(_tmpId,_tmpSignalStrength,_tmpSignalQuality,_tmpNetworkType,_tmpLatencyMs,_tmpDeviceModel,_tmpAndroidVersion,_tmpRecordedAt,_tmpSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getUnsynced(final Continuation<? super List<NetworkMetricEntity>> $completion) {
    final String _sql = "SELECT * FROM network_metrics WHERE synced = 0 ORDER BY recordedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<NetworkMetricEntity>>() {
      @Override
      @NonNull
      public List<NetworkMetricEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSignalStrength = CursorUtil.getColumnIndexOrThrow(_cursor, "signalStrength");
          final int _cursorIndexOfSignalQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "signalQuality");
          final int _cursorIndexOfNetworkType = CursorUtil.getColumnIndexOrThrow(_cursor, "networkType");
          final int _cursorIndexOfLatencyMs = CursorUtil.getColumnIndexOrThrow(_cursor, "latencyMs");
          final int _cursorIndexOfDeviceModel = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceModel");
          final int _cursorIndexOfAndroidVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "androidVersion");
          final int _cursorIndexOfRecordedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "recordedAt");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<NetworkMetricEntity> _result = new ArrayList<NetworkMetricEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NetworkMetricEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Integer _tmpSignalStrength;
            if (_cursor.isNull(_cursorIndexOfSignalStrength)) {
              _tmpSignalStrength = null;
            } else {
              _tmpSignalStrength = _cursor.getInt(_cursorIndexOfSignalStrength);
            }
            final Integer _tmpSignalQuality;
            if (_cursor.isNull(_cursorIndexOfSignalQuality)) {
              _tmpSignalQuality = null;
            } else {
              _tmpSignalQuality = _cursor.getInt(_cursorIndexOfSignalQuality);
            }
            final String _tmpNetworkType;
            _tmpNetworkType = _cursor.getString(_cursorIndexOfNetworkType);
            final Integer _tmpLatencyMs;
            if (_cursor.isNull(_cursorIndexOfLatencyMs)) {
              _tmpLatencyMs = null;
            } else {
              _tmpLatencyMs = _cursor.getInt(_cursorIndexOfLatencyMs);
            }
            final String _tmpDeviceModel;
            _tmpDeviceModel = _cursor.getString(_cursorIndexOfDeviceModel);
            final String _tmpAndroidVersion;
            _tmpAndroidVersion = _cursor.getString(_cursorIndexOfAndroidVersion);
            final long _tmpRecordedAt;
            _tmpRecordedAt = _cursor.getLong(_cursorIndexOfRecordedAt);
            final boolean _tmpSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp != 0;
            _item = new NetworkMetricEntity(_tmpId,_tmpSignalStrength,_tmpSignalQuality,_tmpNetworkType,_tmpLatencyMs,_tmpDeviceModel,_tmpAndroidVersion,_tmpRecordedAt,_tmpSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object markSynced(final List<Long> ids, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("UPDATE network_metrics SET synced = 1 WHERE id IN (");
        final int _inputSize = ids.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (long _item : ids) {
          _stmt.bindLong(_argIndex, _item);
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
