package com.tranphuc.cleanerkt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tranphuc.cleanerkt.R
import com.tranphuc.cleanerkt.model.ItemHeaderJunkFiles
import kotlinx.android.synthetic.main.item_junk_file.view.*

class JunkFilesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private lateinit var listHeaderJunkFiles: MutableList<ItemHeaderJunkFiles>
    private lateinit var context: Context
    /////

    constructor(listHeaderJunkFiles: MutableList<ItemHeaderJunkFiles>, context: Context) : super() {
        this.listHeaderJunkFiles = listHeaderJunkFiles
        this.context = context
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        var layoutInflater = LayoutInflater.from(p0?.context)

        var viewHolderHeaderJunkFiles: View = layoutInflater.inflate(R.layout.item_junk_file, p0, false)

        viewHolder = when (viewType) {
            0 -> ViewHolderHeaderJunkFiles(viewHolderHeaderJunkFiles)
            else -> {
                ViewHolderHeaderJunkFiles(viewHolderHeaderJunkFiles)
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return listHeaderJunkFiles.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, position: Int) {
        when (p0?.itemViewType) {
            0 -> {
                val holder: ViewHolderHeaderJunkFiles = p0 as ViewHolderHeaderJunkFiles
                holder.itemView.textview_title.text = listHeaderJunkFiles.get(position).Title
                holder.itemView.textview_total.text = listHeaderJunkFiles.get(position).Total
            }
        }
    }

    inner class ViewHolderHeaderJunkFiles : RecyclerView.ViewHolder {
        private lateinit var textview_title: TextView
        private lateinit var textview_total: TextView

        constructor(itemView: View?) : super(itemView!!) {
            textview_title = itemView?.findViewById(R.id.textview_title) as TextView
            textview_total = itemView?.findViewById(R.id.textview_total) as TextView
        }
    }
}
