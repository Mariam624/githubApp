package com.example.github.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.github.databinding.FragmentUserDetailsBinding
import com.example.github.datasource.ApiResponse
import com.example.github.presentation.data.User
import com.example.github.presentation.viewmodel.UserDetailsViewModel

class UserDetailsFragment : Fragment() {

    companion object {
        const val ACTION_USER_NAME = "actionUserName"
    }

    private var binding: FragmentUserDetailsBinding? = null

    private val viewModel by viewModels<UserDetailsViewModel>()

    private val userObserver = Observer<ApiResponse<User>> { response ->
        if (response is ApiResponse.Success) {
            binding?.initViews(response.data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userLiveData.observe(viewLifecycleOwner, userObserver)

        val name = arguments?.getString(ACTION_USER_NAME, "") ?: ""
        viewModel.getUser(name)
    }

    private fun FragmentUserDetailsBinding.initViews(user: User?) {
        user?.let {
            Glide.with(requireContext())
                .load(it.avatarUrl)
                .circleCrop()
                .into(ivAvatar)

            tvLogin.text = it.login
            tvLocation.text = it.location
            tvFollowers.text = "${it.followers} Followers"
            tvFollowing.text = "${it.following} Followers"
            setColoredBio(tvBio, it.bio)
        }
    }

    private fun setColoredBio(textView: TextView, bio: String?) {
        if (bio.isNullOrEmpty()) {
            textView.text = ""
            return
        }

        val bioText = SpannableStringBuilder()
        val bioLabel = "Bio: "
        bioText.append(bioLabel)
        bioText.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            bioLabel.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        bioText.append(bio)
        textView.text = bioText
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}

