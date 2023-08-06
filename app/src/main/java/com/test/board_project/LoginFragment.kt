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
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.test.board_project.MainActivity.Companion.BOARD_MAIN_FRAGMENT
import com.test.board_project.MainActivity.Companion.JOIN_FRAGMENT
import com.test.board_project.databinding.FragmentLoginBinding
import com.test.board_project.repository.UserRepository
import com.test.board_project.vm.UserViewModel

class LoginFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentLoginBinding: FragmentLoginBinding

    lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        userViewModel = ViewModelProvider(mainActivity)[UserViewModel::class.java]
        userViewModel.run {
            userId.observe(mainActivity) {
                fragmentLoginBinding.textInputEditTextLoginUserId.setText(it)
            }
            userPw.observe(mainActivity) {
                fragmentLoginBinding.textInputEditTextLoginUserPassword.setText(it)
            }
        }

        fragmentLoginBinding.run{
            toolbarLogin.run {
                title = "로그인"
            }

            textInputEditTextLoginUserPassword.run {
                setOnEditorActionListener { v, actionId, event ->
                    loginSubmit()
                    true
                }
            }

            buttonLogin.setOnClickListener {
                loginSubmit()
            }

            buttonJoin.setOnClickListener {
                mainActivity.replaceFragment(JOIN_FRAGMENT,true,null)
            }
        }

        return fragmentLoginBinding.root

    }

    override fun onResume() {
        super.onResume()
        userViewModel.reset()
    }

    fun loginSubmit() {
        fragmentLoginBinding.run {

            var userId = textInputEditTextLoginUserId.text.toString()
            var userPassword = textInputEditTextLoginUserPassword.text.toString()

            if(userId.isEmpty()) {
                val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                    setTitle("로그인 입력 오류")
                    setMessage("ID가 입력되어 있지 않습니다.")

                    setPositiveButton("확인", null)
                }
                builder.show()
            }
            else if(userPassword.isEmpty()) {
                val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                    setTitle("로그인 입력 오류")
                    setMessage("PASSWORD가 입력되어 있지 않습니다.")

                    setPositiveButton("확인", null)
                }
                builder.show()
            }
            else {
               UserRepository.getUserInfoByUserId(userId) {

                   // 가져온 유저 정보가 없는 경우
                    if(it.result.exists() == false){
                        val builder = MaterialAlertDialogBuilder(mainActivity)
                        builder.setTitle("로그인 오류")
                        builder.setMessage("존재하지 않는 아이디 입니다.")
                        builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                            userViewModel.reset()
                            mainActivity.showSoftInput(textInputEditTextLoginUserId)
                        }
                        builder.show()
                    }

                    else {
                        for(c1 in it.result.children){

                            val userPw = c1.child("userPw").value as String

                            if(userPassword != userPw){
                                val builder = MaterialAlertDialogBuilder(mainActivity)
                                builder.setTitle("로그인 오류")
                                builder.setMessage("잘못된 비밀번호 입니다.")
                                builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                    textInputEditTextLoginUserPassword.setText("")
                                    mainActivity.showSoftInput(textInputEditTextLoginUserPassword)
                                }
                                builder.show()
                            }

                            else {
                                // 로그인한 사용자 정보 가져오기
                                val userIdx = c1.child("userIdx").value as Long
                                val userId = c1.child("userId").value as String
                                val userPw = c1.child("userPw").value as String
                                val userNickname = c1.child("userNickname").value as String
                                val userAge = c1.child("userAge").value as Long
                                val hobby1 = c1.child("hobby1").value as Boolean
                                val hobby2 = c1.child("hobby2").value as Boolean
                                val hobby3 = c1.child("hobby3").value as Boolean
                                val hobby4 = c1.child("hobby4").value as Boolean
                                val hobby5 = c1.child("hobby5").value as Boolean
                                val hobby6 = c1.child("hobby6").value as Boolean

                                mainActivity.loginUserClass = UserClass(userIdx, userId, userPw, userNickname, userAge, hobby1, hobby2, hobby3, hobby4, hobby5, hobby6)
                                Snackbar.make(fragmentLoginBinding.root, "로그인 되었습니다.", Snackbar.LENGTH_SHORT).show()

                                mainActivity.replaceFragment(MainActivity.BOARD_MAIN_FRAGMENT, false, null)
                            }
                        }
                    }
                }
            }
        }
    }
}