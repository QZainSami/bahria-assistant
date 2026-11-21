package com.bahria.portal.data.local;

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
import androidx.sqlite.db.SupportSQLiteStatement;
import com.bahria.portal.data.model.Assignment;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class AssignmentDao_Impl implements AssignmentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Assignment> __insertionAdapterOfAssignment;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public AssignmentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAssignment = new EntityInsertionAdapter<Assignment>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `assignments` (`id`,`title`,`courseName`,`dueDateString`,`dueTimestamp`,`status`,`updatedAt`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Assignment entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getCourseName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCourseName());
        }
        if (entity.getDueDateString() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDueDateString());
        }
        statement.bindLong(5, entity.getDueTimestamp());
        if (entity.getStatus() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getStatus());
        }
        statement.bindLong(7, entity.getUpdatedAt());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM assignments";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<Assignment> assignments,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAssignment.insert(assignments);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
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
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Assignment>> getAllAssignments() {
    final String _sql = "SELECT * FROM assignments ORDER BY dueTimestamp ASC, updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"assignments"}, new Callable<List<Assignment>>() {
      @Override
      @NonNull
      public List<Assignment> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfDueDateString = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateString");
          final int _cursorIndexOfDueTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "dueTimestamp");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Assignment> _result = new ArrayList<Assignment>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Assignment _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpCourseName;
            if (_cursor.isNull(_cursorIndexOfCourseName)) {
              _tmpCourseName = null;
            } else {
              _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            }
            final String _tmpDueDateString;
            if (_cursor.isNull(_cursorIndexOfDueDateString)) {
              _tmpDueDateString = null;
            } else {
              _tmpDueDateString = _cursor.getString(_cursorIndexOfDueDateString);
            }
            final long _tmpDueTimestamp;
            _tmpDueTimestamp = _cursor.getLong(_cursorIndexOfDueTimestamp);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Assignment(_tmpId,_tmpTitle,_tmpCourseName,_tmpDueDateString,_tmpDueTimestamp,_tmpStatus,_tmpUpdatedAt);
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
  public Flow<List<Assignment>> getPendingAssignments() {
    final String _sql = "SELECT * FROM assignments WHERE status = 'Pending' ORDER BY dueTimestamp ASC, updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"assignments"}, new Callable<List<Assignment>>() {
      @Override
      @NonNull
      public List<Assignment> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfDueDateString = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateString");
          final int _cursorIndexOfDueTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "dueTimestamp");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Assignment> _result = new ArrayList<Assignment>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Assignment _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpCourseName;
            if (_cursor.isNull(_cursorIndexOfCourseName)) {
              _tmpCourseName = null;
            } else {
              _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            }
            final String _tmpDueDateString;
            if (_cursor.isNull(_cursorIndexOfDueDateString)) {
              _tmpDueDateString = null;
            } else {
              _tmpDueDateString = _cursor.getString(_cursorIndexOfDueDateString);
            }
            final long _tmpDueTimestamp;
            _tmpDueTimestamp = _cursor.getLong(_cursorIndexOfDueTimestamp);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Assignment(_tmpId,_tmpTitle,_tmpCourseName,_tmpDueDateString,_tmpDueTimestamp,_tmpStatus,_tmpUpdatedAt);
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
  public Object getAllAssignmentsSync(final Continuation<? super List<Assignment>> $completion) {
    final String _sql = "SELECT * FROM assignments";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Assignment>>() {
      @Override
      @NonNull
      public List<Assignment> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfDueDateString = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateString");
          final int _cursorIndexOfDueTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "dueTimestamp");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Assignment> _result = new ArrayList<Assignment>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Assignment _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpCourseName;
            if (_cursor.isNull(_cursorIndexOfCourseName)) {
              _tmpCourseName = null;
            } else {
              _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            }
            final String _tmpDueDateString;
            if (_cursor.isNull(_cursorIndexOfDueDateString)) {
              _tmpDueDateString = null;
            } else {
              _tmpDueDateString = _cursor.getString(_cursorIndexOfDueDateString);
            }
            final long _tmpDueTimestamp;
            _tmpDueTimestamp = _cursor.getLong(_cursorIndexOfDueTimestamp);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Assignment(_tmpId,_tmpTitle,_tmpCourseName,_tmpDueDateString,_tmpDueTimestamp,_tmpStatus,_tmpUpdatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
