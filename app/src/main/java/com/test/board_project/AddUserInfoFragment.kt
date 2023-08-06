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
import androidx.core.view.children
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.test.board_project.databinding.FragmentAddUserInfoBinding
import com.test.board_project.repository.UserRepository


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

            // 취미 전체 체크박스
            materialCheckBoxAddUserInfoAll.run{
                // b : materialCheckBoxAddUserInfoAll
                setOnCheckedChangeListener { compoundButton, b ->
                    // 각 체크박스를 가지고 있는 레이아웃을 통해 그 안에 있는 View 체크상태 변경
                    for(v1 in materialCheckBoxGroupUserInfo1.children){
                        // 형변환
                        v1 as MaterialCheckBox
                        // 취미 전체가 체크 되어 있는 경우
                        if(b){
                            v1.checkedState = MaterialCheckBox.STATE_CHECKED
                        } else {
                            v1.checkedState = MaterialCheckBox.STATE_UNCHECKED
                        }
                    }

                    for(v1 in materialCheckBoxGroupUserInfo2.children){
                        // 형변환
                        v1 as MaterialCheckBox
                        // 취미 전체가 체크 되어 있는 경우
                        if(b){
                            v1.checkedState = MaterialCheckBox.STATE_CHECKED
                        } else {
                            v1.checkedState = MaterialCheckBox.STATE_UNCHECKED
                        }
                    }
                }
            }

            for(v1 in materialCheckBoxGroupUserInfo1.children){
                v1 as MaterialCheckBox
                v1.setOnCheckedChangeListener { compoundButton, b ->
                    setParentCheckBoxState()
                }
            }
            for(v1 in materialCheckBoxGroupUserInfo2.children){
                v1 as MaterialCheckBox
                v1.setOnCheckedChangeListener { compoundButton, b ->
                    setParentCheckBoxState()
                }
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

                    UserRepository.getUserIdx {
                        // 현재 사용자의 순서값 가져오기
                        var userIdx = it.result.value as Long

                        val userId = arguments?.getString("UserId")!!
                        val userPw = arguments?.getString("UserPw")!!

                        userIdx++

                        val userClass = UserClass(
                            userIdx, userId, userPw, userName, userAge.toLong(),
                            materialCheckBoxAddUserInfoHobby1.isChecked,
                            materialCheckBoxAddUserInfoHobby2.isChecked,
                            materialCheckBoxAddUserInfoHobby3.isChecked,
                            materialCheckBoxAddUserInfoHobby4.isChecked,
                            materialCheckBoxAddUserInfoHobby5.isChecked,
                            materialCheckBoxAddUserInfoHobby6.isChecked
                        )


                        UserRepository.addUserInfo(userClass) {
                            UserRepository.setUserIdx(userIdx) {
                                Snackbar.make(
                                    fragmentAddUserInfoBinding.root,
                                    "가입이 완료되었습니다",
                                    Snackbar.LENGTH_SHORT
                                ).show()

                                mainActivity.removeFragment(MainActivity.ADD_USER_INFO_FRAGMENT)
                                mainActivity.removeFragment(MainActivity.JOIN_FRAGMENT)
                            }
                        }
                    }
                }
            }
        }
        return fragmentAddUserInfoBinding.root
    }

    // 하위의 체크박스 상태로 상위 체크 박스 상태 설정
    fun setParentCheckBoxState(){
        fragmentAddUserInfoBinding.run {
            // 체크박스 개수
            val checkBoxCount = materialCheckBoxGroupUserInfo1.childCount + materialCheckBoxGroupUserInfo2.childCount

            // 체크되어 있는 체크박스 개수
            var checkedCount = 0

            for(v1 in materialCheckBoxGroupUserInfo1.children){
                v1 as MaterialCheckBox
                if(v1.checkedState == MaterialCheckBox.STATE_CHECKED){
                    checkedCount++
                }
            }
            for(v1 in materialCheckBoxGroupUserInfo2.children){
                v1 as MaterialCheckBox
                if(v1.checkedState == MaterialCheckBox.STATE_CHECKED){
                    checkedCount++
                }
            }

            // 만약 체크되어 있는 것이 없다면
            if(checkedCount == 0){
                materialCheckBoxAddUserInfoAll.checkedState = MaterialCheckBox.STATE_UNCHECKED
            }
            // 모두 체크되어 있다면
            else if(checkedCount == checkBoxCount){
                materialCheckBoxAddUserInfoAll.checkedState = MaterialCheckBox.STATE_CHECKED
            }
            // 일부만 체크되어 있다면
            else {
                materialCheckBoxAddUserInfoAll.checkedState = MaterialCheckBox.STATE_INDETERMINATE
            }
        }
    }
}