package com.test.board_project

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.board_project.databinding.FragmentModifyUserBinding

class ModifyUserFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentModifyUserBinding: FragmentModifyUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = activity as MainActivity
        fragmentModifyUserBinding = FragmentModifyUserBinding.inflate(inflater)

        fragmentModifyUserBinding.run {
            buttonModifyUserInfoAccept.setOnClickListener {
                var modifyUserPW = textInputEditTextModifyUserPassword.text.toString()
                var modifyUserPWCheck = textInputEditTextModifyUserPasswordCheck.text.toString()
                var modifyUserName = textInputEditTextModifyUserInfoUserName.text.toString()
                var modifyUserAge = textInputEditTextModifyUserInfoUserAge.text.toString()
                if(modifyUserPW.isNotEmpty() || modifyUserPWCheck.isNotEmpty()) {
                    if (modifyUserPW != modifyUserPWCheck) {
                        val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                            setTitle("개인 정보 수정 비밀번호 오류")
                            setMessage("비밀번호가 일치하지 않습니다.")

                            setPositiveButton("확인", null)
                        }
                        builder.show()
                    }
                    else {
                        val t1 = Toast.makeText(mainActivity,"비밀번호 수정이 완료되었습니다.", Toast.LENGTH_LONG)
                        t1.show()
                    }
                }
                else if(modifyUserName.isEmpty()) {
                    val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                        setTitle("개인 정보 수정 입력 오류")
                        setMessage("닉네임이 입력되어 있지 않습니다.")

                        setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                            mainActivity.showSoftInput(textInputEditTextModifyUserInfoUserName)
                        }
                    }
                    builder.show()
                }
                else if(modifyUserAge.isEmpty()) {
                    val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                        setTitle("개인 정보 수정 입력 오류")
                        setMessage("나이가 입력되어 있지 않습니다.")

                        setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                            mainActivity.showSoftInput(textInputEditTextModifyUserInfoUserAge)
                        }
                    }
                    builder.show()
                }
                else {
                    val t1 = Toast.makeText(mainActivity,"개인 정보 수정이 완료되었습니다.", Toast.LENGTH_LONG)
                    t1.show()
                }
            }
        }
        return fragmentModifyUserBinding.root
    }
}