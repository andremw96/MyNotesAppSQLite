package andreamw.com.mynoteapp

import andreamw.com.mynoteapp.db.NoteHelper
import andreamw.com.mynoteapp.entity.Note
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_note_add_update.*
import java.text.SimpleDateFormat
import java.util.*

class NoteAddUpdateActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
    }


    private var note : Note? = null
    private lateinit var noteHelper: NoteHelper
    private var isEdit : Boolean = false
    private var position : Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add_update)

        btn_submit.setOnClickListener(this)

        noteHelper = NoteHelper.getInstance(applicationContext)!!

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if(note != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            note = Note()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Ubah"
            btnTitle = "Update"
            if (note != null) {
                edt_title.setText(note?.title)
                edt_description.setText(note?.description)
            }
        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Simpan"
        }

        if (supportActionBar != null) {
            supportActionBar!!.setTitle(actionBarTitle)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        btn_submit.setText(btnTitle)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    val ALERT_DIALOG_CLOSE = 10
    val ALERT_DIALOG_DELETE = 20

    private fun showAlertDialog(type: Int) {
        val isDialogClose : Boolean = type == ALERT_DIALOG_CLOSE
        var dialogTitle: String
        var dialogMessage: String

        if(isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?";
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?";
            dialogTitle = "Hapus Note";
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, id: Int) {
                    if(isDialogClose) {
                        finish()
                    } else {
                        val result = noteHelper.deleteNote(note?.id)
                        if(result > 0) {
                            val intent = Intent()
                            intent.putExtra(EXTRA_POSITION, position)
                            setResult(RESULT_DELETE, intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            })
            .setNegativeButton("Tidak", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, id: Int) {
                    dialog?.cancel()
                }

            })

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onClick(p0: View?) {
        if(p0?.id == R.id.btn_submit) {
            val title = edt_title.text.toString().trim()
            val description = edt_description.text.toString().trim()

            if(TextUtils.isEmpty(title)) {
                edt_title.setError("Field can't be blank")
                return
            }

            note?.title = title
            note?.description = description

            val intent = Intent()
            intent.putExtra(EXTRA_NOTE, note)
            intent.putExtra(EXTRA_POSITION, position)

            if(isEdit) {
                val result = noteHelper.updateNote(note)
                if(result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show();
                }
            } else {
                note?.date = getCurrentDate()
                val result = noteHelper.insertNote(note)
                if (result > 0) {
                    note?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menambah data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private fun getCurrentDate() : String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }
}
