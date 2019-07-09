package andreamw.com.mynoteapp

import android.view.View

open class CustomOnItemClickListener(private val position: Int, private val onItemClickCallback: OnItemClickCallback) : View.OnClickListener {

    override fun onClick(p0: View?) {
        onItemClickCallback.onItemClicked(p0, position)
    }

    interface OnItemClickCallback {
        fun onItemClicked(view: View?, position: Int)
    }
}
