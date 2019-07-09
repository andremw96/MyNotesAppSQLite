package andreamw.com.mynoteapp.db

import android.provider.BaseColumns

class DatabaseContract {

    companion object {
        val TABLE_NOTE = "note"
    }

    class NoteColumns : BaseColumns {
        companion object : KBaseColumns() {
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val DATE = "date"
        }
    }
}

open class KBaseColumns {
    val _ID = "_id"
}