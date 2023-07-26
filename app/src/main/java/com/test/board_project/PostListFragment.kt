package com.test.board_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.board_project.databinding.FragmentPostListBinding

class PostListFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentPostListBinding: FragmentPostListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = activity as MainActivity
        fragmentPostListBinding = FragmentPostListBinding.inflate(inflater)

        return fragmentPostListBinding.root
    }
}


