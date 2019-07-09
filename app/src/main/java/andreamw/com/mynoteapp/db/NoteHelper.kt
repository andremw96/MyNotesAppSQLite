package andreamw.com.mynoteapp.db

import andreamw.com.mynoteapp.db.DatabaseContract.Companion.TABLE_NOTE
import andreamw.com.mynoteapp.db.DatabaseContract.NoteColumns.Companion.DATE
import andreamw.com.mynoteapp.db.DatabaseContract.NoteColumns.Companion.DESCRIPTION
import andreamw.com.mynoteapp.db.DatabaseContract.NoteColumns.Companion.TITLE
import andreamw.com.mynoteapp.entity.Note
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID


class NoteHelper private constructor(context: Context) {

    init {
        databaseHelper = DatabaseHelper(context)
    }

    companion object {
        private val DATABASE_TABLE = TABLE_NOTE
        private var databaseHelper: DatabaseHelper? = null
        private var INSTANCE : NoteHelper? = null
        private var database: SQLiteDatabase? = null

        fun getInstance(context: Context) : NoteHelper? {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = NoteHelper(context)
                    }
                }
            }
            return INSTANCE
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper?.getWritableDatabase()
    }

    fun close() {
        databaseHelper?.close()
        if (database!!.isOpen)
            database?.close()
    }

    fun getAllNotes() : ArrayList<Note> {
        var arrayList : ArrayList<Note> = arrayListOf()
        val cursor : Cursor = database!!.query(DATABASE_TABLE, null,
            null,
            null,
            null,
            null,
            _ID + " ASC",
            null)
        cursor.moveToFirst()
        var note : Note
        if(cursor.count > 0 ){
            do {
                note = Note()
                note.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                note.title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                note.description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                note.date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))

                arrayList.add(note)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()

        return arrayList
    }

    fun insertNote(note: Note?) : Long {
        val args = ContentValues()
        args.put(TITLE, note?.title)
        args.put(DESCRIPTION, note?.description)
        args.put(DATE, note?.date)
        return database!!.insert(DATABASE_TABLE, null, args)
    }

    fun updateNote(note: Note?) : Int {
        val args = ContentValues()
        args.put(TITLE, note?.title)
        args.put(DESCRIPTION, note?.description)
        args.put(DATE, note?.date)
        return database!!.update(DATABASE_TABLE, args, _ID + "= '" + note?.id + "'", null)
    }

    fun deleteNote(id: Int?) : Int {
        return database!!.delete(TABLE_NOTE, _ID + "= '" + id + "'", null)
    }
}

