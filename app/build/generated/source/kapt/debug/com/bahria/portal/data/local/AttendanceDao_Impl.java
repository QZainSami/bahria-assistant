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
import com.bahria.portal.data.model.CourseAttendance;
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
public final class AttendanceDao_Impl implements AttendanceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CourseAttendance> __insertionAdapterOfCourseAttendance;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public AttendanceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCourseAttendance = new EntityInsertionAdapter<CourseAttendance>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `attendance` (`courseName`,`courseCode`,`present`,`absent`,`total`,`percentage`,`updatedAt`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CourseAttendance entity) {
        if (entity.getCourseName() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getCourseName());
        }
        if (entity.getCourseCode() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCourseCode());
        }
        statement.bindDouble(3, entity.getPresent());
        statement.bindDouble(4, entity.getAbsent());
        statement.bindDouble(5, entity.getTotal());
        statement.bindDouble(6, entity.getPercentage());
        statement.bindLong(7, entity.getUpdatedAt());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM attendance";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<CourseAttendance> courses,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCourseAttendance.insert(courses);
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
  public Flow<List<CourseAttendance>> getAllAttendance() {
    final String _sql = "SELECT * FROM attendance ORDER BY courseName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attendance"}, new Callable<List<CourseAttendance>>() {
      @Override
      @NonNull
      public List<CourseAttendance> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfCourseCode = CursorUtil.getColumnIndexOrThrow(_cursor, "courseCode");
          final int _cursorIndexOfPresent = CursorUtil.getColumnIndexOrThrow(_cursor, "present");
          final int _cursorIndexOfAbsent = CursorUtil.getColumnIndexOrThrow(_cursor, "absent");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "percentage");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<CourseAttendance> _result = new ArrayList<CourseAttendance>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CourseAttendance _item;
            final String _tmpCourseName;
            if (_cursor.isNull(_cursorIndexOfCourseName)) {
              _tmpCourseName = null;
            } else {
              _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            }
            final String _tmpCourseCode;
            if (_cursor.isNull(_cursorIndexOfCourseCode)) {
              _tmpCourseCode = null;
            } else {
              _tmpCourseCode = _cursor.getString(_cursorIndexOfCourseCode);
            }
            final double _tmpPresent;
            _tmpPresent = _cursor.getDouble(_cursorIndexOfPresent);
            final double _tmpAbsent;
            _tmpAbsent = _cursor.getDouble(_cursorIndexOfAbsent);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final double _tmpPercentage;
            _tmpPercentage = _cursor.getDouble(_cursorIndexOfPercentage);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new CourseAttendance(_tmpCourseName,_tmpCourseCode,_tmpPresent,_tmpAbsent,_tmpTotal,_tmpPercentage,_tmpUpdatedAt);
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
  public Object getAllAttendanceSync(
      final Continuation<? super List<CourseAttendance>> $completion) {
    final String _sql = "SELECT * FROM attendance ORDER BY courseName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CourseAttendance>>() {
      @Override
      @NonNull
      public List<CourseAttendance> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfCourseCode = CursorUtil.getColumnIndexOrThrow(_cursor, "courseCode");
          final int _cursorIndexOfPresent = CursorUtil.getColumnIndexOrThrow(_cursor, "present");
          final int _cursorIndexOfAbsent = CursorUtil.getColumnIndexOrThrow(_cursor, "absent");
          final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "percentage");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<CourseAttendance> _result = new ArrayList<CourseAttendance>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CourseAttendance _item;
            final String _tmpCourseName;
            if (_cursor.isNull(_cursorIndexOfCourseName)) {
              _tmpCourseName = null;
            } else {
              _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            }
            final String _tmpCourseCode;
            if (_cursor.isNull(_cursorIndexOfCourseCode)) {
              _tmpCourseCode = null;
            } else {
              _tmpCourseCode = _cursor.getString(_cursorIndexOfCourseCode);
            }
            final double _tmpPresent;
            _tmpPresent = _cursor.getDouble(_cursorIndexOfPresent);
            final double _tmpAbsent;
            _tmpAbsent = _cursor.getDouble(_cursorIndexOfAbsent);
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            final double _tmpPercentage;
            _tmpPercentage = _cursor.getDouble(_cursorIndexOfPercentage);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new CourseAttendance(_tmpCourseName,_tmpCourseCode,_tmpPresent,_tmpAbsent,_tmpTotal,_tmpPercentage,_tmpUpdatedAt);
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
