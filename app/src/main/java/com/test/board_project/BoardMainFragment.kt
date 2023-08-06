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
import androidx.fragment.app.FragmentManager
import com.google.android.material.transition.MaterialSharedAxis
import com.test.board_project.databinding.FragmentBoardMainBinding
import com.test.board_project.databinding.HeaderBoardMainBinding


class BoardMainFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentBoardMainBinding: FragmentBoardMainBinding

    var newFragment:Fragment? = null
    var oldFragment:Fragment? = null

    companion object{
        val POST_LIST_FRAGMENT = "PostListFragment"
        val MODIFY_USER_FRAGMENT = "ModifyUserFragment"
    }

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
                            toolBarMain.title = "전체게시판"
                            val newBundle = Bundle()
                            newBundle.putInt("postType", 0)
                            replaceFragment(POST_LIST_FRAGMENT, false, false, newBundle)
                            drawerLayoutBoardMain.close()
                        }

                        R.id.item_board_freetalk -> {
                            toolBarMain.title = "자유게시판"
                            val newBundle = Bundle()
                            newBundle.putInt("postType", 1)
                            replaceFragment(POST_LIST_FRAGMENT, false, false, newBundle)
                            drawerLayoutBoardMain.close()
                        }

                        R.id.item_board_humor -> {
                            toolBarMain.title = "유머게시판"
                            val newBundle = Bundle()
                            newBundle.putInt("postType", 2)
                            replaceFragment(POST_LIST_FRAGMENT, false, false, newBundle)
                            drawerLayoutBoardMain.close()
                        }

                        R.id.item_board_question -> {
                            toolBarMain.title = "질문게시판"
                            val newBundle = Bundle()
                            newBundle.putInt("postType", 3)
                            replaceFragment(POST_LIST_FRAGMENT, false, false, newBundle)
                            drawerLayoutBoardMain.close()
                        }

                        R.id.item_board_sports -> {
                            toolBarMain.title = "스포츠게시판"
                            val newBundle = Bundle()
                            newBundle.putInt("postType", 4)
                            replaceFragment(POST_LIST_FRAGMENT, false, false, newBundle)
                            drawerLayoutBoardMain.close()
                        }

                        R.id.item_user_info -> {
                            replaceFragment(MODIFY_USER_FRAGMENT, false, false, null)
                            drawerLayoutBoardMain.close()
                        }

                        R.id.item_logout -> {
                            mainActivity.replaceFragment(MainActivity.LOGIN_FRAGMENT, false, null)
                        }

                        R.id.item_sign_out -> {
                            mainActivity.replaceFragment(MainActivity.LOGIN_FRAGMENT, false, null)
                        }
                    }

                    // navigation 닫아주기
//                    drawerLayoutBoardMain.close()

                    false
                }
            }

            // 첫 화면 설정
            val newBundle = Bundle()
            newBundle.putInt("postType", 0)
            replaceFragment(POST_LIST_FRAGMENT,false,false,newBundle)
        }

        return fragmentBoardMainBinding.root
    }

    // 지정한 Fragment를 보여주는 메서드
    fun replaceFragment(name:String, addToBackStack:Boolean, animate:Boolean, bundle:Bundle?){
        // Fragment 교체 상태로 설정
        val fragmentTransaction = mainActivity.supportFragmentManager.beginTransaction()

        // newFragment에 Fragment가 있는 경우 oldFragment에 넣어준다.
        if(newFragment != null){
            oldFragment = newFragment
        }

        newFragment = when(name){
            POST_LIST_FRAGMENT -> PostListFragment()
            MODIFY_USER_FRAGMENT -> ModifyUserFragment()
            else -> Fragment()
        }

        newFragment?.arguments = bundle

        if(newFragment != null) {

            if(animate) {
                // 애니메이션 설정
                if (oldFragment != null) {
                    oldFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                    oldFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
                    oldFragment?.enterTransition = null
                    oldFragment?.returnTransition = null
                }

                newFragment?.exitTransition = null
                newFragment?.reenterTransition = null
                newFragment?.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                newFragment?.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
            } else {
                if (oldFragment != null) {
                    oldFragment?.exitTransition = null
                    oldFragment?.reenterTransition = null
                    oldFragment?.enterTransition = null
                    oldFragment?.returnTransition = null
                }

                newFragment?.exitTransition = null
                newFragment?.reenterTransition = null
                newFragment?.enterTransition = null
                newFragment?.returnTransition = null
            }

            // Fragment 교체
            fragmentTransaction.replace(R.id.boardMainContainer, newFragment!!)

            if (addToBackStack) {
                // Fragment를 Backstack에 넣어 이전으로 돌아가는 기능이 동작할 수 있도록 한다.
                fragmentTransaction.addToBackStack(name)
            }

            // 교체 명령이 동작
            fragmentTransaction.commit()
        }
    }

    // Fragment BackStack에서 제거
    fun removeFragment(name:String) {
        mainActivity.supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}