package `in`.encrust.writersden

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class FontAdapter internal constructor(private val context: Context, private val fontList: ArrayList<Typeface>?, private val fontNameList: List<String>?) : RecyclerView.Adapter<FontAdapter.FontHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FontHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fonts, viewGroup, false)
        return FontHolder(view)
    }

    override fun onBindViewHolder(fontHolder: FontHolder, i: Int) {
        val font = fontList?.get(i)
        val name = fontNameList?.get(i)

        fontHolder.fontView.typeface = font
        fontHolder.fontView.text = name
        fontHolder.fontView.setOnClickListener {
            val intent = Intent("intent").putExtra("position", i)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            if (context is Home) {

                context.updateBroadCast(i)
            }
        }

    }

    override fun getItemCount(): Int {
        return fontList!!.size
    }

    inner class FontHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fontView: TextView

        init {

            fontView = itemView.findViewById(R.id.fonts_fontview)
        }
    }
}
