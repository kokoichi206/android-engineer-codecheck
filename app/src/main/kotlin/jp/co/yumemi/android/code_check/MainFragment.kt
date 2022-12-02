/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import jp.co.yumemi.android.code_check.MainActivity.Companion.updateLastSearchDate
import jp.co.yumemi.android.code_check.data.GitHubAPI
import jp.co.yumemi.android.code_check.databinding.FragmentMainBinding
import jp.co.yumemi.android.code_check.models.Repository
import kotlinx.coroutines.launch

/**
 * fragment_main の設定。
 */
class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMainBinding.bind(view)

        val viewModel = MainViewModel(GitHubAPI())

        // RecyclerView の登場人物を取得
        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemClickListener {
            override fun itemClick(item: Repository) {
                gotoRepositoryFragment(item)
            }
        })

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    adapter.submitList(uiState.repositories)
                }
            }
        }

        // サーチボタンが押された時のリスナーの設定
        binding.searchInputText.setOnEditorActionListener { editText, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                // 入力されたテキストで検索を行う
                viewModel.searchResults(editText.text.toString())
                // 最終検索日時を更新する
                updateLastSearchDate()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        // RecyclerView に設定
        binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }
    }

    fun gotoRepositoryFragment(item: Repository) {
        val action = MainFragmentDirections.actionRepositoriesFragmentToRepositoryFragment(item = item)
        findNavController().navigate(action)
    }
}
