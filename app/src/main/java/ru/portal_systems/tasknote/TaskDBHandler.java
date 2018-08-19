package ru.portal_systems.tasknote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ru.portal_systems.tasknote.model.Task;

/**
 * Created by 111 on 23.08.2017.
 */

public class TaskDBHandler extends SQLiteOpenHelper implements InterfaceTaskDB {
    public static final String DB_NAME = "task_db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "task";

    //поля
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String TERM = "date";
    public static final String PRIORITET = "prioritet";
    public static final String COMPLETE = "complete";

    //Костанты запросов:
    public static final String ID_SELECT = ID + " =?";

    public TaskDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY, "
                + NAME + " TEXT,"
                + DESCRIPTION + " TEXT,"
                + TERM + " TEXT,"
                + PRIORITET + " INTEGER,"
                + COMPLETE + " INTEGER"
                + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO заполнить
    }

    @Override
    public void createItem(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, getValuesFromTask(task));
        db.close();
    }

    @Override
    public Task readItem(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                null,
                ID+"=?",
                new String[]{String.valueOf(id)},
                null,null,null,null
        );
        if (cursor.moveToFirst()){
            return getTaskFromCursor(cursor);
        }
        return null;//todo переписать под Exception
    }

    @Override
    public int updateItem(long id, Task task) {
        SQLiteDatabase db = getWritableDatabase();
        return db.update(TABLE_NAME,
                getValuesFromTask(task),
                ID_SELECT,
                new String[]{String.valueOf(id)});
    }

    @Override
    public void deleteItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, ID_SELECT, new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public Task readLastItem() {
        SQLiteDatabase db = getWritableDatabase();
        //// TODO: 23.08.2017 переписать под конструктор запросов
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " +
                ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            return getTaskFromCursor(cursor);
        }
        return null;
    }

    @Override
    public List<Task> readAllItems() {
        List<Task> rList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                rList.add(getTaskFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return rList;
    }

    private Task getTaskFromCursor(Cursor cursor){
        Task rTask = new Task();
        rTask.setId(cursor.getInt(cursor.getColumnIndex(ID)));
        rTask.setName(cursor.getString(cursor.getColumnIndex(NAME)));
        rTask.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
        rTask.setTerm(TaskManager.getDateFromString(cursor.getString(cursor.getColumnIndex(TERM))));
        rTask.setPrioritet(cursor.getInt(cursor.getColumnIndex(PRIORITET)));
        rTask.setComplete(cursor.getInt(cursor.getColumnIndex(COMPLETE)) == 1);
//        cursor.close();
        return rTask;
    }

    private ContentValues getValuesFromTask(Task task) {
        ContentValues rValues = new ContentValues();
        rValues.put(NAME, task.getName());
        rValues.put(DESCRIPTION, task.getDescription());
        rValues.put(TERM, TaskManager.getStringFromDate(task.getTerm()));
        rValues.put(PRIORITET, task.getPrioritet());
        rValues.put(COMPLETE, task.isComplete() ? 1 : 0);
        return rValues;
    }
}
