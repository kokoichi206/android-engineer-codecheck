package jp.co.yumemi.android.code_check.util

import androidx.recyclerview.widget.DiffUtil
import jp.co.yumemi.android.code_check.models.Repository

/**
 * [Repository] に対する diffCallback の定義。
 */
val diff_util = object : DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem == newItem
    }
}
