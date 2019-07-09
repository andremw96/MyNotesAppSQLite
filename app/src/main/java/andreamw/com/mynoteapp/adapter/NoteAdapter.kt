package andreamw.com.mynoteapp.adapter

import andreamw.com.mynoteapp.CustomOnItemClickListener
import andreamw.com.mynoteapp.R
import andreamw.com.mynoteapp.entity.Note
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView
import android.widget.TextView
import android.content.Intent
import andreamw.com.mynoteapp.NoteAddUpdateActivity





class NoteAdapter(private var activity : Activity) : RecyclerView.Adapter<NoteViewHolder>() {

    private var listNotes : ArrayList<Note> = arrayListOf()

    fun getListNotes() : ArrayList<Note> {
        return listNotes
    }

    fun setListNotes(listNotes : ArrayList<Note>) {
        if(listNotes.size > 0) {
            this.listNotes.clear()
        }
        this.listNotes.addAll(listNotes)
        notifyDataSetChanged()
    }

    fun addItem(note: Note) {
        this.listNotes.add(note)
        notifyItemInserted(listNotes.size - 1)
    }

    fun updateItem(position: Int, note: Note) {
        this.listNotes.set(position, note)
        notifyItemChanged(position, note)
    }

    fun deleteItem(position: Int) {
        this.listNotes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listNotes.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = listNotes.size


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.tvTitle.text = listNotes.get(position).title
        holder.tvDate.text = listNotes.get(position).date
        holder.tvDescription.text = listNotes.get(position).description

        holder.cvNote.setOnClickListener(
            CustomOnItemClickListener(position, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View?, position: Int) {
                        val intent = Intent(activity, NoteAddUpdateActivity::class.java)
                        intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, position)
                        intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, listNotes[position])
                        activity.startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_UPDATE)
                    }
                })
        )
    }

}

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvTitle: TextView = itemView.findViewById(R.id.tv_item_title)
    var tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
    var tvDate: TextView = itemView.findViewById(R.id.tv_item_date)
    var cvNote: CardView = itemView.findViewById(R.id.cv_item_note)
}