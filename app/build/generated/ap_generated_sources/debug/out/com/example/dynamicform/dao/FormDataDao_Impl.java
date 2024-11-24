package com.example.dynamicform.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.dynamicform.models.FormData;
import com.example.dynamicform.models.FormSummary;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FormDataDao_Impl implements FormDataDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FormData> __insertionAdapterOfFormData;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsSynced;

  public FormDataDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFormData = new EntityInsertionAdapter<FormData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `form_data` (`id`,`formId`,`formTitle`,`fieldName`,`fieldValue`,`inserted_on`,`synced`,`synced_on`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FormData value) {
        stmt.bindLong(1, value.id);
        if (value.formId == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.formId);
        }
        if (value.formTitle == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.formTitle);
        }
        if (value.fieldName == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.fieldName);
        }
        if (value.fieldValue == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.fieldValue);
        }
        stmt.bindLong(6, value.insertedOn);
        stmt.bindLong(7, value.synced);
        stmt.bindLong(8, value.syncedOn);
      }
    };
    this.__preparedStmtOfMarkAsSynced = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE form_data SET synced = ?, synced_on = ? WHERE formId = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final FormData formData) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFormData.insert(formData);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void markAsSynced(final String formId, final boolean synced, final long syncedOn) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsSynced.acquire();
    int _argIndex = 1;
    final int _tmp = synced ? 1 : 0;
    _stmt.bindLong(_argIndex, _tmp);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, syncedOn);
    _argIndex = 3;
    if (formId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, formId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfMarkAsSynced.release(_stmt);
    }
  }

  @Override
  public List<FormData> getFormData(final String formTitle) {
    final String _sql = "SELECT * FROM form_data WHERE formTitle = ? ORDER BY inserted_on DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (formTitle == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, formTitle);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFormId = CursorUtil.getColumnIndexOrThrow(_cursor, "formId");
      final int _cursorIndexOfFormTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "formTitle");
      final int _cursorIndexOfFieldName = CursorUtil.getColumnIndexOrThrow(_cursor, "fieldName");
      final int _cursorIndexOfFieldValue = CursorUtil.getColumnIndexOrThrow(_cursor, "fieldValue");
      final int _cursorIndexOfInsertedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "inserted_on");
      final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
      final int _cursorIndexOfSyncedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "synced_on");
      final List<FormData> _result = new ArrayList<FormData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final FormData _item;
        _item = new FormData();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfFormId)) {
          _item.formId = null;
        } else {
          _item.formId = _cursor.getString(_cursorIndexOfFormId);
        }
        if (_cursor.isNull(_cursorIndexOfFormTitle)) {
          _item.formTitle = null;
        } else {
          _item.formTitle = _cursor.getString(_cursorIndexOfFormTitle);
        }
        if (_cursor.isNull(_cursorIndexOfFieldName)) {
          _item.fieldName = null;
        } else {
          _item.fieldName = _cursor.getString(_cursorIndexOfFieldName);
        }
        if (_cursor.isNull(_cursorIndexOfFieldValue)) {
          _item.fieldValue = null;
        } else {
          _item.fieldValue = _cursor.getString(_cursorIndexOfFieldValue);
        }
        _item.insertedOn = _cursor.getLong(_cursorIndexOfInsertedOn);
        _item.synced = _cursor.getInt(_cursorIndexOfSynced);
        _item.syncedOn = _cursor.getLong(_cursorIndexOfSyncedOn);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<FormSummary> getFormSummaries() {
    final String _sql = "SELECT DISTINCT formId, formTitle FROM form_data";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfFormId = 0;
      final int _cursorIndexOfFormTitle = 1;
      final List<FormSummary> _result = new ArrayList<FormSummary>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final FormSummary _item;
        _item = new FormSummary();
        _item.formId = _cursor.getInt(_cursorIndexOfFormId);
        if (_cursor.isNull(_cursorIndexOfFormTitle)) {
          _item.formTitle = null;
        } else {
          _item.formTitle = _cursor.getString(_cursorIndexOfFormTitle);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<FormData> getFormDataByFormId(final int formId) {
    final String _sql = "SELECT * FROM form_data WHERE formId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, formId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFormId = CursorUtil.getColumnIndexOrThrow(_cursor, "formId");
      final int _cursorIndexOfFormTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "formTitle");
      final int _cursorIndexOfFieldName = CursorUtil.getColumnIndexOrThrow(_cursor, "fieldName");
      final int _cursorIndexOfFieldValue = CursorUtil.getColumnIndexOrThrow(_cursor, "fieldValue");
      final int _cursorIndexOfInsertedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "inserted_on");
      final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
      final int _cursorIndexOfSyncedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "synced_on");
      final List<FormData> _result = new ArrayList<FormData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final FormData _item;
        _item = new FormData();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfFormId)) {
          _item.formId = null;
        } else {
          _item.formId = _cursor.getString(_cursorIndexOfFormId);
        }
        if (_cursor.isNull(_cursorIndexOfFormTitle)) {
          _item.formTitle = null;
        } else {
          _item.formTitle = _cursor.getString(_cursorIndexOfFormTitle);
        }
        if (_cursor.isNull(_cursorIndexOfFieldName)) {
          _item.fieldName = null;
        } else {
          _item.fieldName = _cursor.getString(_cursorIndexOfFieldName);
        }
        if (_cursor.isNull(_cursorIndexOfFieldValue)) {
          _item.fieldValue = null;
        } else {
          _item.fieldValue = _cursor.getString(_cursorIndexOfFieldValue);
        }
        _item.insertedOn = _cursor.getLong(_cursorIndexOfInsertedOn);
        _item.synced = _cursor.getInt(_cursorIndexOfSynced);
        _item.syncedOn = _cursor.getLong(_cursorIndexOfSyncedOn);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<FormData> getUnsyncedData() {
    final String _sql = "SELECT * FROM form_data WHERE synced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFormId = CursorUtil.getColumnIndexOrThrow(_cursor, "formId");
      final int _cursorIndexOfFormTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "formTitle");
      final int _cursorIndexOfFieldName = CursorUtil.getColumnIndexOrThrow(_cursor, "fieldName");
      final int _cursorIndexOfFieldValue = CursorUtil.getColumnIndexOrThrow(_cursor, "fieldValue");
      final int _cursorIndexOfInsertedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "inserted_on");
      final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
      final int _cursorIndexOfSyncedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "synced_on");
      final List<FormData> _result = new ArrayList<FormData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final FormData _item;
        _item = new FormData();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfFormId)) {
          _item.formId = null;
        } else {
          _item.formId = _cursor.getString(_cursorIndexOfFormId);
        }
        if (_cursor.isNull(_cursorIndexOfFormTitle)) {
          _item.formTitle = null;
        } else {
          _item.formTitle = _cursor.getString(_cursorIndexOfFormTitle);
        }
        if (_cursor.isNull(_cursorIndexOfFieldName)) {
          _item.fieldName = null;
        } else {
          _item.fieldName = _cursor.getString(_cursorIndexOfFieldName);
        }
        if (_cursor.isNull(_cursorIndexOfFieldValue)) {
          _item.fieldValue = null;
        } else {
          _item.fieldValue = _cursor.getString(_cursorIndexOfFieldValue);
        }
        _item.insertedOn = _cursor.getLong(_cursorIndexOfInsertedOn);
        _item.synced = _cursor.getInt(_cursorIndexOfSynced);
        _item.syncedOn = _cursor.getLong(_cursorIndexOfSyncedOn);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
