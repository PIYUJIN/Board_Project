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
import com.test.board_project.MainActivity.Companion.BOARD_MAIN_FRAGMENT
import com.test.board_project.MainActivity.Companion.JOIN_FRAGMENT
import com.test.board_project.databinding.FragmentLoginBinding

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

            textInputEditTextLoginUserPassword.run {
                setOnEditorActionListener { v, actionId, event ->
                    if(textInputEditTextLoginUserId.text.toString() == "" || textInputEditTextLoginUserPassword.text.toString() == "") {
                        val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                            setTitle("로그인 입력 오류")
                            setMessage("ID나 PASSWORD가 입력되어 있지 않습니다.")

                            setPositiveButton("확인", null)
                        }
                        builder.show()
                    } else {
                        mainActivity.replaceFragment(BOARD_MAIN_FRAGMENT, false, null)
                    }
                    false
                }
            }

            buttonLogin.setOnClickListener {
                if(textInputEditTextLoginUserId.text.toString() == "" || textInputEditTextLoginUserPassword.text.toString() == "") {
                    val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                        setTitle("로그인 입력 오류")
                        setMessage("ID나 PASSWORD가 입력되어 있지 않습니다.")

                        setPositiveButton("확인", null)
                    }
                    builder.show()
                } else {
                    mainActivity.replaceFragment(BOARD_MAIN_FRAGMENT, false, null)
                }
            }

            buttonJoin.setOnClickListener {
                mainActivity.replaceFragment(JOIN_FRAGMENT,true,null)
            }
        }

        return fragmentLoginBinding.root

    }



}