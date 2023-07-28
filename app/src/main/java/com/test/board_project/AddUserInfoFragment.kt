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
import com.test.board_project.databinding.FragmentAddUserInfoBinding


class AddUserInfoFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentAddUserInfoBinding: FragmentAddUserInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentAddUserInfoBinding = FragmentAddUserInfoBinding.inflate(inflater)

        fragmentAddUserInfoBinding.run {
            toolbarAddUserInfo.run {
                title = "유저 정보"

                setNavigationIcon(R.drawable.arrow_back_24px)

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    navigationIcon?.colorFilter = BlendModeColorFilter(Color.DKGRAY, BlendMode.SRC_ATOP)
                } else {
                    navigationIcon?.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)
                }

                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.JOIN_FRAGMENT)
                }
            }

            buttonCompleteJoin.setOnClickListener {
                mainActivity.removeFragment(MainActivity.ADD_USER_INFO_FRAGMENT)
                mainActivity.removeFragment(MainActivity.JOIN_FRAGMENT)
            }
        }
        return fragmentAddUserInfoBinding.root
    }
}