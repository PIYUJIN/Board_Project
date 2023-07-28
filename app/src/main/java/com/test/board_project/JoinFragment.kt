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
import com.test.board_project.MainActivity.Companion.ADD_USER_INFO_FRAGMENT
import com.test.board_project.MainActivity.Companion.JOIN_FRAGMENT
import com.test.board_project.databinding.FragmentJoinBinding

class JoinFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentJoinBinding: FragmentJoinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainActivity = activity as MainActivity
        fragmentJoinBinding = FragmentJoinBinding.inflate(inflater)

        fragmentJoinBinding.run {
            toolbarJoin.run {
                title = "회원가입"

                setNavigationIcon(R.drawable.arrow_back_24px)

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    navigationIcon?.colorFilter = BlendModeColorFilter(Color.DKGRAY, BlendMode.SRC_ATOP)
                } else {
                    navigationIcon?.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)
                }

                setNavigationOnClickListener {
                    mainActivity.removeFragment(JOIN_FRAGMENT)
                }
            }
            buttonNext.setOnClickListener {
                mainActivity.replaceFragment(ADD_USER_INFO_FRAGMENT,true,null)
            }
        }
        return fragmentJoinBinding.root
    }

}