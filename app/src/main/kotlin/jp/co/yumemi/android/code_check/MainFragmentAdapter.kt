package jp.co.yumemi.android.code_check

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.util.diff_util

/**
 * MainFragment の Adapter クラス。
 */
class MainFragmentAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<Repository, MainFragmentAdapter.ViewHolder>(diff_util) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface OnItemClickListener {
        fun itemClick(item: Repository)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.findViewById<TextView>(R.id.repositoryNameView).text = item.name

        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(item)
        }
    }
}
