package andreamw.com.mynoteapp.db

import android.database.sqlite.SQLiteDatabase
import andreamw.com.mynoteapp.db.DatabaseContract.NoteColumns
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "dbnoteapp"
        const val DATABASE_VERSION = 1
    }

    val SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s"
        + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
        " %s TEXT NOT NULL," +
        " %s TEXT NOT NULL," +
        " %s TEXT NOT NULL)",
        DatabaseContract.TABLE_NOTE,
        NoteColumns._ID,
        NoteColumns.TITLE,
        NoteColumns.DESCRIPTION,
        NoteColumns.DATE
    )

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_NOTE);
        onCreate(db);
    }
}

