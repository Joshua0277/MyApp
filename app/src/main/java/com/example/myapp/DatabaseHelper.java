package com.example.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "diet.db";
    private static final int DATABASE_VERSION = 1;

    // Summary Table
    public static final String SUMMARY_TABLE = "summary";
    public static final String SUMMARY_COLUMN_DATE = "date";
    public static final String SUMMARY_COLUMN_PROTEIN = "protein";
    public static final String SUMMARY_COLUMN_CARB = "carb";
    public static final String SUMMARY_COLUMN_OTHER = "other";

    // Details Table
    public static final String DETAILS_TABLE = "details";
    public static final String DETAILS_COLUMN_DATE = "date";
    public static final String DETAILS_COLUMN_CATEGORY = "category";
    public static final String DETAILS_COLUMN_NAME = "name";
    public static final String DETAILS_COLUMN_AMOUNT = "amount";
    public static final String DETAILS_COLUMN_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SUMMARY_TABLE + " (" +
                SUMMARY_COLUMN_DATE + " TEXT PRIMARY KEY, " +
                SUMMARY_COLUMN_PROTEIN + " TEXT, " +
                SUMMARY_COLUMN_CARB + " TEXT, " +
                SUMMARY_COLUMN_OTHER + " TEXT)");

        db.execSQL("CREATE TABLE " + DETAILS_TABLE + " (" +
                DETAILS_COLUMN_DATE + " TEXT, " +
                DETAILS_COLUMN_CATEGORY + " TEXT, " +
                DETAILS_COLUMN_NAME + " TEXT, " +
                DETAILS_COLUMN_AMOUNT + " TEXT, " +
                DETAILS_COLUMN_UNIT + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SUMMARY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DETAILS_TABLE);
        onCreate(db);
    }

    // ✅ Insert Summary
    public void insertSummaryRecord(String date, String protein, String carb, String other) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUMMARY_COLUMN_DATE, date);
        values.put(SUMMARY_COLUMN_PROTEIN, protein);
        values.put(SUMMARY_COLUMN_CARB, carb);
        values.put(SUMMARY_COLUMN_OTHER, other);
        db.insertWithOnConflict(SUMMARY_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    // ✅ Insert Detail
    public void insertDetailedRecord(String date, String category, String name, String amount, String unit) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DETAILS_COLUMN_DATE, date);
        values.put(DETAILS_COLUMN_CATEGORY, category);
        values.put(DETAILS_COLUMN_NAME, name);
        values.put(DETAILS_COLUMN_AMOUNT, amount);
        values.put(DETAILS_COLUMN_UNIT, unit);
        db.insert(DETAILS_TABLE, null, values);
        db.close();
    }

    // ✅ Get All Summary Records (for HistoryFragment)
    public List<HistoryRecord> getAllSummaryRecords() {
        List<HistoryRecord> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SUMMARY_TABLE + " ORDER BY date DESC", null);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndexOrThrow(SUMMARY_COLUMN_DATE));
                String protein = cursor.getString(cursor.getColumnIndexOrThrow(SUMMARY_COLUMN_PROTEIN));
                String carb = cursor.getString(cursor.getColumnIndexOrThrow(SUMMARY_COLUMN_CARB));
                String other = cursor.getString(cursor.getColumnIndexOrThrow(SUMMARY_COLUMN_OTHER));
                list.add(new HistoryRecord(date, protein, carb, other));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // ✅ Get All Detail Records for One Date
    public List<DetailRecord> getDetailsByDate(String date) {
        List<DetailRecord> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DETAILS_TABLE +
                " WHERE " + DETAILS_COLUMN_DATE + " = ?", new String[]{date});

        if (cursor.moveToFirst()) {
            do {
                list.add(new DetailRecord(
                        cursor.getString(cursor.getColumnIndexOrThrow(DETAILS_COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DETAILS_COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DETAILS_COLUMN_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DETAILS_COLUMN_UNIT))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // ✅ Delete all detail & summary of a date
    public void deleteRecordByDate(String date) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SUMMARY_TABLE, SUMMARY_COLUMN_DATE + " = ?", new String[]{date});
        db.delete(DETAILS_TABLE, DETAILS_COLUMN_DATE + " = ?", new String[]{date});
        db.close();
    }

    // 🔄 Update summary (for future edit)
    public void updateSummaryRecord(String date, String protein, String carb, String other) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUMMARY_COLUMN_PROTEIN, protein);
        values.put(SUMMARY_COLUMN_CARB, carb);
        values.put(SUMMARY_COLUMN_OTHER, other);
        db.update(SUMMARY_TABLE, values, SUMMARY_COLUMN_DATE + " = ?", new String[]{date});
        db.close();
    }

    // 🔄 Clear all records (for debug/reset)
    public void clearAllData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SUMMARY_TABLE, null, null);
        db.delete(DETAILS_TABLE, null, null);
        db.close();
    }
}
