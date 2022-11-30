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

    private var binding: FragmentDetailBinding? = null
    private val _binding get() = binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())

        binding = FragmentDetailBinding.bind(view)

        val item = args.item

        val context = requireContext()

        _binding.ownerIconView.load(item.ownerIconUrl)
        _binding.nameView.text = item.name
        _binding.languageView.text = item.language
        _binding.starsView.text = context.getString(R.string.detail_stars, item.stargazersCount)
        _binding.watchersView.text = context.getString(R.string.detail_watchers, item.watchersCount)
        _binding.forksView.text = context.getString(R.string.detail_forks, item.forksCount)
        _binding.openIssuesView.text = context.getString(R.string.detail_open_issues, item.openIssuesCount)
    }
}
