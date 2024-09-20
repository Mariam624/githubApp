package com.example.github.presentation.fragment

import UserListAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.github.R
import com.example.github.databinding.FragmentUserListBinding
import com.example.github.databinding.UserItemBinding
import com.example.github.datasource.ApiResponse
import com.example.github.presentation.data.User
import com.example.github.presentation.viewmodel.UserListViewModel

class UserListFragment : Fragment() {

    private var binding: FragmentUserListBinding? = null

    private val viewModel by viewModels<UserListViewModel>()

    private val listener = { name: String ->
        val bundle = bundleOf(UserDetailsFragment.ACTION_USER_NAME to name)
        findNavController().navigate(R.id.action_userListFragment_to_userDetailsFragment, bundle)
    }

    private val action = { user: User, binding: UserItemBinding ->
        Glide.with(requireContext())
            .load(user.avatarUrl)
            .circleCrop()
            .into(binding.ivAvatar)
    }

    private val userListAdapter: UserListAdapter by lazy {
        UserListAdapter(listener, action)
    }

    private var fullUserList: List<User> = listOf()

    private val userListObserver = Observer<ApiResponse<List<User>>> { response ->
        if (response is ApiResponse.Success) {
            fullUserList = response.data
            userListAdapter.submitList(fullUserList)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchBar()
        binding?.recyclerView?.apply {
            adapter = userListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding?.clear?.setOnClickListener {
            binding?.searchBar?.removeLastCharacter()
        }

        initObservers()
        viewModel.getUserList()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

    private fun initObservers() {
        viewModel.userListLiveData.observe(viewLifecycleOwner, userListObserver)
    }

    private fun setupSearchBar() {
        binding?.searchBar?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterUserList(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun EditText.removeLastCharacter() {
        val currentText = text.toString()

        if (currentText.isNotEmpty()) {
            setText(currentText.dropLast(1))
            setSelection(text.length)
        }
    }

    private fun filterUserList(query: String) {
        val filteredList = if (query.isEmpty()) {
            fullUserList
        } else {
            val exactMatches = fullUserList.filter { user ->
                user.login.equals(query, ignoreCase = true)
            }
            val partialMatches = fullUserList.filter { user ->
                !user.login.equals(query, ignoreCase = true)
                        && (user.login.startsWith(query, ignoreCase = true)
                        || user.login.contains(query, ignoreCase = true)
                        )
            }
            exactMatches + partialMatches
        }
        userListAdapter.submitList(filteredList)
    }
}
