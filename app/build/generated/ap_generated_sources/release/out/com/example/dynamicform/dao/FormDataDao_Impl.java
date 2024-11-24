package com.example.dynamicform.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
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

  public FormDataDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFormData = new EntityInsertionAdapter<FormData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `form_data` (`id`,`formId`,`formTitle`,`fieldName`,`fieldValue`) VALUES (nullif(?, 0),?,?,?,?)";
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
  public List<FormData> getFormData(final String formTitle) {
    final String _sql = "SELECT * FROM form_data WHERE formTitle = ?";
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
