package com.test.board_project

import android.content.DialogInterface
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
                    mainActivity.removeFragment(MainActivity.ADD_USER_INFO_FRAGMENT)
                }
            }

            textInputEditTextAddUserInfoUserName.run {
                requestFocus()
            }

            buttonCompleteJoin.setOnClickListener {
                var userName = textInputEditTextAddUserInfoUserName.text.toString()
                var userAge = textInputEditTextAddUserInfoUserAge.text.toString()
                if(userName.isEmpty()) {
                    val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                        setTitle("회원가입 입력 오류")
                        setMessage("닉네임이 입력되어 있지 않습니다.")

                        setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                            mainActivity.showSoftInput(textInputEditTextAddUserInfoUserName)
                        }
                    }
                    builder.show()
                }
                else if(userAge.isEmpty()) {
                    val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                        setTitle("회원가입 입력 오류")
                        setMessage("나이가 입력되어 있지 않습니다.")

                        setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                            mainActivity.showSoftInput(textInputEditTextAddUserInfoUserAge)
                        }
                    }
                    builder.show()
                }
                else {
                    mainActivity.removeFragment(MainActivity.ADD_USER_INFO_FRAGMENT)
                    mainActivity.removeFragment(MainActivity.JOIN_FRAGMENT)
                }
            }
        }
        return fragmentAddUserInfoBinding.root
    }
}