package `in`.encrust.writersden

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ImageAdapter internal constructor(private val context: Context, private val imageList: ArrayList<Drawable>?) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.images, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val image = imageList?.get(i)
        viewHolder.imageView.setImageDrawable(image)

        viewHolder.imageView.setOnClickListener {
            if (context is Home) {
                if (i == 0) {
                    context.openGallery()
                } else {
                    context.selectImage(i)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return imageList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.images)

    }
}
