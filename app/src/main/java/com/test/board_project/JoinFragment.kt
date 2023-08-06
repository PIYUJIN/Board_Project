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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.board_project.MainActivity.Companion.ADD_USER_INFO_FRAGMENT
import com.test.board_project.MainActivity.Companion.JOIN_FRAGMENT
import com.test.board_project.databinding.FragmentJoinBinding
import com.test.board_project.vm.UserViewModel

class JoinFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentJoinBinding: FragmentJoinBinding

    lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainActivity = activity as MainActivity
        fragmentJoinBinding = FragmentJoinBinding.inflate(inflater)

        userViewModel = ViewModelProvider(mainActivity)[UserViewModel::class.java]

        userViewModel.run {
            userId.observe(mainActivity) {
                fragmentJoinBinding.textInputEditTextJoinUserId.setText(it)
            }
            userPw.observe(mainActivity) {
                fragmentJoinBinding.textInputEditTextJoinUserPassword.setText(it)
            }
            userPwCheck.observe(mainActivity) {
                fragmentJoinBinding.textInputEditTextJoinUserPasswordCheck.setText(it)
            }
        }

        fragmentJoinBinding.run {
            mainActivity.showSoftInput(textInputEditTextJoinUserId)
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

            textInputEditTextJoinUserId.run {
                requestFocus()
            }

            textInputEditTextJoinUserPasswordCheck.run {
                setOnEditorActionListener { v, actionId, event ->
                    checkText()
                    true
                }
            }

            buttonNext.setOnClickListener {
                checkText()
            }
        }
        return fragmentJoinBinding.root
    }

    override fun onResume() {
        super.onResume()
        userViewModel.reset()
    }

    fun checkText() {
        fragmentJoinBinding.run {
            var userId = textInputEditTextJoinUserId.text.toString()
            var userPassword = textInputEditTextJoinUserPassword.text.toString()
            var userPasswordCheck = textInputEditTextJoinUserPasswordCheck.text.toString()
            if(userId.isEmpty()) {
                val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                    setTitle("회원가입 입력 오류")
                    setMessage("ID가 입력되어 있지 않습니다.")

                    setPositiveButton("확인", null)
                }
                builder.show()
            }
            else if(userPassword.isEmpty()) {
                val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                    setTitle("회원가입 입력 오류")
                    setMessage("PASSWORD가 입력되어 있지 않습니다.")

                    setPositiveButton("확인", null)
                }
                builder.show()
            }
            else if(userPasswordCheck.isEmpty()) {
                val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                    setTitle("회원가입 입력 오류")
                    setMessage("비밀번호 확인이 입력되어 있지 않습니다.")

                    setPositiveButton("확인", null)
                }
                builder.show()
            }
            else if(userPassword != userPasswordCheck) {
                val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                    setTitle("회원가입 비밀번호 오류")
                    setMessage("PASSWORD가 일치하지 않습니다.")

                    setPositiveButton("확인", null)
                }
                builder.show()
            }
            else {
                val newBundle = Bundle()
                newBundle.putString("UserId", userId)
                newBundle.putString("UserPw", userPassword)

                mainActivity.replaceFragment(ADD_USER_INFO_FRAGMENT,true,newBundle)
            }
        }
    }
}