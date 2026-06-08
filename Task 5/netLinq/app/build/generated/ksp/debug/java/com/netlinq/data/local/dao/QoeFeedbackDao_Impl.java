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
import com.netlinq.data.local.entity.QoeFeedbackEntity;
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
public final class QoeFeedbackDao_Impl implements QoeFeedbackDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<QoeFeedbackEntity> __insertionAdapterOfQoeFeedbackEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSyncedOlderThan;

  public QoeFeedbackDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfQoeFeedbackEntity = new EntityInsertionAdapter<QoeFeedbackEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `qoe_feedback` (`id`,`overallRating`,`responsivenessRating`,`streamingRating`,`callQualityRating`,`satisfactionRating`,`triggerEvent`,`networkType`,`networkMetricId`,`metricRecordedAt`,`signalStrengthSnapshot`,`latencyMsSnapshot`,`notes`,`recordedAt`,`synced`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final QoeFeedbackEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getOverallRating());
        statement.bindLong(3, entity.getResponsivenessRating());
        statement.bindLong(4, entity.getStreamingRating());
        statement.bindLong(5, entity.getCallQualityRating());
        statement.bindLong(6, entity.getSatisfactionRating());
        if (entity.getTriggerEvent() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getTriggerEvent());
        }
        if (entity.getNetworkType() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getNetworkType());
        }
        if (entity.getNetworkMetricId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getNetworkMetricId());
        }
        if (entity.getMetricRecordedAt() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getMetricRecordedAt());
        }
        if (entity.getSignalStrengthSnapshot() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getSignalStrengthSnapshot());
        }
        if (entity.getLatencyMsSnapshot() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getLatencyMsSnapshot());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getNotes());
        }
        statement.bindLong(14, entity.getRecordedAt());
        final int _tmp = entity.getSynced() ? 1 : 0;
        statement.bindLong(15, _tmp);
      }
    };
    this.__preparedStmtOfDeleteSyncedOlderThan = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM qoe_feedback WHERE synced = 1 AND recordedAt < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final QoeFeedbackEntity feedback,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfQoeFeedbackEntity.insertAndReturnId(feedback);
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
  public Flow<List<QoeFeedbackEntity>> observeAll() {
    final String _sql = "SELECT * FROM qoe_feedback ORDER BY recordedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"qoe_feedback"}, new Callable<List<QoeFeedbackEntity>>() {
      @Override
      @NonNull
      public List<QoeFeedbackEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOverallRating = CursorUtil.getColumnIndexOrThrow(_cursor, "overallRating");
          final int _cursorIndexOfResponsivenessRating = CursorUtil.getColumnIndexOrThrow(_cursor, "responsivenessRating");
          final int _cursorIndexOfStreamingRating = CursorUtil.getColumnIndexOrThrow(_cursor, "streamingRating");
          final int _cursorIndexOfCallQualityRating = CursorUtil.getColumnIndexOrThrow(_cursor, "callQualityRating");
          final int _cursorIndexOfSatisfactionRating = CursorUtil.getColumnIndexOrThrow(_cursor, "satisfactionRating");
          final int _cursorIndexOfTriggerEvent = CursorUtil.getColumnIndexOrThrow(_cursor, "triggerEvent");
          final int _cursorIndexOfNetworkType = CursorUtil.getColumnIndexOrThrow(_cursor, "networkType");
          final int _cursorIndexOfNetworkMetricId = CursorUtil.getColumnIndexOrThrow(_cursor, "networkMetricId");
          final int _cursorIndexOfMetricRecordedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "metricRecordedAt");
          final int _cursorIndexOfSignalStrengthSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "signalStrengthSnapshot");
          final int _cursorIndexOfLatencyMsSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "latencyMsSnapshot");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfRecordedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "recordedAt");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<QoeFeedbackEntity> _result = new ArrayList<QoeFeedbackEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QoeFeedbackEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpOverallRating;
            _tmpOverallRating = _cursor.getInt(_cursorIndexOfOverallRating);
            final int _tmpResponsivenessRating;
            _tmpResponsivenessRating = _cursor.getInt(_cursorIndexOfResponsivenessRating);
            final int _tmpStreamingRating;
            _tmpStreamingRating = _cursor.getInt(_cursorIndexOfStreamingRating);
            final int _tmpCallQualityRating;
            _tmpCallQualityRating = _cursor.getInt(_cursorIndexOfCallQualityRating);
            final int _tmpSatisfactionRating;
            _tmpSatisfactionRating = _cursor.getInt(_cursorIndexOfSatisfactionRating);
            final String _tmpTriggerEvent;
            if (_cursor.isNull(_cursorIndexOfTriggerEvent)) {
              _tmpTriggerEvent = null;
            } else {
              _tmpTriggerEvent = _cursor.getString(_cursorIndexOfTriggerEvent);
            }
            final String _tmpNetworkType;
            if (_cursor.isNull(_cursorIndexOfNetworkType)) {
              _tmpNetworkType = null;
            } else {
              _tmpNetworkType = _cursor.getString(_cursorIndexOfNetworkType);
            }
            final Long _tmpNetworkMetricId;
            if (_cursor.isNull(_cursorIndexOfNetworkMetricId)) {
              _tmpNetworkMetricId = null;
            } else {
              _tmpNetworkMetricId = _cursor.getLong(_cursorIndexOfNetworkMetricId);
            }
            final Long _tmpMetricRecordedAt;
            if (_cursor.isNull(_cursorIndexOfMetricRecordedAt)) {
              _tmpMetricRecordedAt = null;
            } else {
              _tmpMetricRecordedAt = _cursor.getLong(_cursorIndexOfMetricRecordedAt);
            }
            final Integer _tmpSignalStrengthSnapshot;
            if (_cursor.isNull(_cursorIndexOfSignalStrengthSnapshot)) {
              _tmpSignalStrengthSnapshot = null;
            } else {
              _tmpSignalStrengthSnapshot = _cursor.getInt(_cursorIndexOfSignalStrengthSnapshot);
            }
            final Integer _tmpLatencyMsSnapshot;
            if (_cursor.isNull(_cursorIndexOfLatencyMsSnapshot)) {
              _tmpLatencyMsSnapshot = null;
            } else {
              _tmpLatencyMsSnapshot = _cursor.getInt(_cursorIndexOfLatencyMsSnapshot);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpRecordedAt;
            _tmpRecordedAt = _cursor.getLong(_cursorIndexOfRecordedAt);
            final boolean _tmpSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp != 0;
            _item = new QoeFeedbackEntity(_tmpId,_tmpOverallRating,_tmpResponsivenessRating,_tmpStreamingRating,_tmpCallQualityRating,_tmpSatisfactionRating,_tmpTriggerEvent,_tmpNetworkType,_tmpNetworkMetricId,_tmpMetricRecordedAt,_tmpSignalStrengthSnapshot,_tmpLatencyMsSnapshot,_tmpNotes,_tmpRecordedAt,_tmpSynced);
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
  public Object getUnsynced(final Continuation<? super List<QoeFeedbackEntity>> $completion) {
    final String _sql = "SELECT * FROM qoe_feedback WHERE synced = 0 ORDER BY recordedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<QoeFeedbackEntity>>() {
      @Override
      @NonNull
      public List<QoeFeedbackEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOverallRating = CursorUtil.getColumnIndexOrThrow(_cursor, "overallRating");
          final int _cursorIndexOfResponsivenessRating = CursorUtil.getColumnIndexOrThrow(_cursor, "responsivenessRating");
          final int _cursorIndexOfStreamingRating = CursorUtil.getColumnIndexOrThrow(_cursor, "streamingRating");
          final int _cursorIndexOfCallQualityRating = CursorUtil.getColumnIndexOrThrow(_cursor, "callQualityRating");
          final int _cursorIndexOfSatisfactionRating = CursorUtil.getColumnIndexOrThrow(_cursor, "satisfactionRating");
          final int _cursorIndexOfTriggerEvent = CursorUtil.getColumnIndexOrThrow(_cursor, "triggerEvent");
          final int _cursorIndexOfNetworkType = CursorUtil.getColumnIndexOrThrow(_cursor, "networkType");
          final int _cursorIndexOfNetworkMetricId = CursorUtil.getColumnIndexOrThrow(_cursor, "networkMetricId");
          final int _cursorIndexOfMetricRecordedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "metricRecordedAt");
          final int _cursorIndexOfSignalStrengthSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "signalStrengthSnapshot");
          final int _cursorIndexOfLatencyMsSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "latencyMsSnapshot");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfRecordedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "recordedAt");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<QoeFeedbackEntity> _result = new ArrayList<QoeFeedbackEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QoeFeedbackEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpOverallRating;
            _tmpOverallRating = _cursor.getInt(_cursorIndexOfOverallRating);
            final int _tmpResponsivenessRating;
            _tmpResponsivenessRating = _cursor.getInt(_cursorIndexOfResponsivenessRating);
            final int _tmpStreamingRating;
            _tmpStreamingRating = _cursor.getInt(_cursorIndexOfStreamingRating);
            final int _tmpCallQualityRating;
            _tmpCallQualityRating = _cursor.getInt(_cursorIndexOfCallQualityRating);
            final int _tmpSatisfactionRating;
            _tmpSatisfactionRating = _cursor.getInt(_cursorIndexOfSatisfactionRating);
            final String _tmpTriggerEvent;
            if (_cursor.isNull(_cursorIndexOfTriggerEvent)) {
              _tmpTriggerEvent = null;
            } else {
              _tmpTriggerEvent = _cursor.getString(_cursorIndexOfTriggerEvent);
            }
            final String _tmpNetworkType;
            if (_cursor.isNull(_cursorIndexOfNetworkType)) {
              _tmpNetworkType = null;
            } else {
              _tmpNetworkType = _cursor.getString(_cursorIndexOfNetworkType);
            }
            final Long _tmpNetworkMetricId;
            if (_cursor.isNull(_cursorIndexOfNetworkMetricId)) {
              _tmpNetworkMetricId = null;
            } else {
              _tmpNetworkMetricId = _cursor.getLong(_cursorIndexOfNetworkMetricId);
            }
            final Long _tmpMetricRecordedAt;
            if (_cursor.isNull(_cursorIndexOfMetricRecordedAt)) {
              _tmpMetricRecordedAt = null;
            } else {
              _tmpMetricRecordedAt = _cursor.getLong(_cursorIndexOfMetricRecordedAt);
            }
            final Integer _tmpSignalStrengthSnapshot;
            if (_cursor.isNull(_cursorIndexOfSignalStrengthSnapshot)) {
              _tmpSignalStrengthSnapshot = null;
            } else {
              _tmpSignalStrengthSnapshot = _cursor.getInt(_cursorIndexOfSignalStrengthSnapshot);
            }
            final Integer _tmpLatencyMsSnapshot;
            if (_cursor.isNull(_cursorIndexOfLatencyMsSnapshot)) {
              _tmpLatencyMsSnapshot = null;
            } else {
              _tmpLatencyMsSnapshot = _cursor.getInt(_cursorIndexOfLatencyMsSnapshot);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpRecordedAt;
            _tmpRecordedAt = _cursor.getLong(_cursorIndexOfRecordedAt);
            final boolean _tmpSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp != 0;
            _item = new QoeFeedbackEntity(_tmpId,_tmpOverallRating,_tmpResponsivenessRating,_tmpStreamingRating,_tmpCallQualityRating,_tmpSatisfactionRating,_tmpTriggerEvent,_tmpNetworkType,_tmpNetworkMetricId,_tmpMetricRecordedAt,_tmpSignalStrengthSnapshot,_tmpLatencyMsSnapshot,_tmpNotes,_tmpRecordedAt,_tmpSynced);
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
        _stringBuilder.append("UPDATE qoe_feedback SET synced = 1 WHERE id IN (");
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
