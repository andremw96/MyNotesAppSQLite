package andreamw.com.mynoteapp

import andreamw.com.mynoteapp.entity.Note

interface LoadNotesCallback {
    fun preExecute()
    fun postExecute(notes: ArrayList<Note>)
}