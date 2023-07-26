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
import com.test.board_project.databinding.FragmentBoardMainBinding
import com.test.board_project.databinding.HeaderBoardMainBinding


class BoardMainFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentBoardMainBinding: FragmentBoardMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentBoardMainBinding = FragmentBoardMainBinding.inflate(inflater)

        fragmentBoardMainBinding.run {
            toolBarMain.run {
                title = "게시판"

                setNavigationIcon(R.drawable.menu_24px)
                setNavigationOnClickListener{
                    // navigation 보여주기
                    drawerLayoutBoardMain.open()
                }
            }

            // DrawerView
            navigationViewBoardMain.run{

                // 헤더설정
                val headerBoardMainBinding = HeaderBoardMainBinding.inflate(inflater)
                headerBoardMainBinding.textViewHeaderBoardMainNickName.text  = "pujin님"
                addHeaderView(headerBoardMainBinding.root)

                // 항목 선택시 동작
                setNavigationItemSelectedListener {

                    // 누른 메뉴를 체크상태로 설정해두는 경우
//                    it.isChecked = true

                    when(it.itemId) {
                        R.id.item_board_all -> {

                        }

                        R.id.item_board_freetalk -> {

                        }

                        R.id.item_board_humor -> {

                        }

                        R.id.item_board_question -> {

                        }

                        R.id.item_board_sports -> {

                        }

                        R.id.item_user_info -> {

                        }

                        R.id.item_logout -> {

                        }

                        R.id.item_sign_out -> {

                        }
                    }

                    // navigation 닫아주기
                    drawerLayoutBoardMain.close()

                    true
                }
            }
        }

        return fragmentBoardMainBinding.root
    }

}