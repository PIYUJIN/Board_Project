package com.test.board_project

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.board_project.MainActivity.Companion.JOIN_FRAGMENT
import com.test.board_project.MainActivity.Companion.MAIN_FRAGMENT
import com.test.board_project.databinding.FragmentLoginBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentLoginBinding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentLoginBinding.run{
            toolbarLogin.run {
                title = "로그인"
            }

            buttonLogin.setOnClickListener {
                mainActivity.replaceFragment(MAIN_FRAGMENT, true, null)
            }

            buttonJoin.setOnClickListener {
                mainActivity.replaceFragment(JOIN_FRAGMENT,true,null)
            }
        }

        return fragmentLoginBinding.root

    }



}