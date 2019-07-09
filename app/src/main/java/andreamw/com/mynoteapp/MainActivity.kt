package andreamw.com.mynoteapp

import andreamw.com.mynoteapp.adapter.NoteAdapter
import andreamw.com.mynoteapp.db.NoteHelper
import andreamw.com.mynoteapp.entity.Note
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), View.OnClickListener, LoadNotesCallback {

    val EXTRA_STATE = "EXTRA_STATE"
    private lateinit var adapter: NoteAdapter
    private lateinit var noteHelper: NoteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (getSupportActionBar() != null)
            getSupportActionBar()?.setTitle("Notes")

        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.setHasFixedSize(true)

        noteHelper = NoteHelper.getInstance(this)!!

        noteHelper.open()

        fab_add.setOnClickListener(this)

        adapter = NoteAdapter(this)
        rv_notes.adapter = adapter

        if(savedInstanceState == null) {
            LoadNotesAsync(noteHelper, this).execute()
        } else {
            val list : ArrayList<Note> = savedInstanceState.getParcelableArrayList(EXTRA_STATE)!!
            if (list != null) {
                adapter.setListNotes(list)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        noteHelper.close()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListNotes())
    }

    override fun onClick(p0: View?) {
        if(p0?.id == R.id.fab_add) {
            val intent = Intent(this, NoteAddUpdateActivity::class.java)
            startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_ADD)
        }
    }


    inner class LoadNotesAsync(noteHelper: NoteHelper, callback: LoadNotesCallback) : AsyncTask<Void, Void, ArrayList<Note>>() {

        private val weakNoteHelper : WeakReference<NoteHelper> = WeakReference(noteHelper)
        private val weakCallback : WeakReference<LoadNotesCallback> = WeakReference(callback)

        override fun onPreExecute() {
            super.onPreExecute()
            weakCallback.get()?.preExecute()
        }

        override fun doInBackground(vararg p0: Void?): ArrayList<Note>? {
            return weakNoteHelper.get()?.getAllNotes()
        }

        override fun onPostExecute(result: ArrayList<Note>) {
            super.onPostExecute(result)
            weakCallback.get()?.postExecute(result)
        }

    }

    override fun preExecute() {
        runOnUiThread { progressbar.setVisibility(View.VISIBLE) }
    }

    override fun postExecute(notes: ArrayList<Note>) {
        progressbar.visibility = View.GONE
        adapter.setListNotes(notes)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data != null) {
            if(requestCode == NoteAddUpdateActivity.REQUEST_ADD) {
                if(resultCode == NoteAddUpdateActivity.RESULT_ADD) {
                    val note : Note = data.getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE)
                    adapter.addItem(note)
                    rv_notes.smoothScrollToPosition(adapter.itemCount - 1)
                    showSnackbarMessage("Satu item berhasil ditambahkan")
                }
            } else if (requestCode == NoteAddUpdateActivity.REQUEST_UPDATE) {
                if(resultCode == NoteAddUpdateActivity.RESULT_UPDATE) {
                    val note : Note = data.getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE)
                    val position : Int = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0)
                    adapter.updateItem(position, note)
                    rv_notes.smoothScrollToPosition(position)
                    showSnackbarMessage("Satu item berhasil diubah")
                } else if (resultCode == NoteAddUpdateActivity.RESULT_DELETE) {
                    val position : Int = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0)
                    adapter.deleteItem(position)
                    showSnackbarMessage("Satu item berhasil dihapus")
                }
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_notes, message, Snackbar.LENGTH_SHORT).show()
    }
}
