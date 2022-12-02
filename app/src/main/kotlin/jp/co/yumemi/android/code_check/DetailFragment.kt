/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.code_check.MainActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.FragmentDetailBinding

/**
 * fragment_detail の設定。
 */
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lastSearchDate?.let { date ->
            Log.d("検索した日時", date.toString())
        }

        val binding = FragmentDetailBinding.bind(view)

        binding.also {
            val item = args.item
            val context = requireContext()

            it.ownerIconView.load(item.ownerIconUrl)
            it.nameView.text = item.name
            it.languageView.text = context.getString(R.string.written_language, item.language)
            it.starsView.text = context.getString(R.string.detail_stars, item.stargazersCount)
            it.watchersView.text = context.getString(R.string.detail_watchers, item.watchersCount)
            it.forksView.text = context.getString(R.string.detail_forks, item.forksCount)
            it.openIssuesView.text = context.getString(R.string.detail_open_issues, item.openIssuesCount)
        }
    }
}
