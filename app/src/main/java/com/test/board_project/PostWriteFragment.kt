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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.board_project.MainActivity.Companion.POST_READ_FRAGMENT
import com.test.board_project.MainActivity.Companion.POST_WRITE_FRAGMENT
import com.test.board_project.databinding.FragmentPostWriteBinding

class PostWriteFragment : Fragment() {

    lateinit var fragmentPostWriteBinding: FragmentPostWriteBinding
    lateinit var mainActivity : MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = activity as MainActivity
        fragmentPostWriteBinding = FragmentPostWriteBinding.inflate(inflater)

        fragmentPostWriteBinding.run {
            toolbarPostWrite.run {
                title = "게시글 작성"

                setNavigationIcon(R.drawable.arrow_back_24px)

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    navigationIcon?.colorFilter = BlendModeColorFilter(Color.DKGRAY, BlendMode.SRC_ATOP)
                } else {
                    navigationIcon?.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)
                }

                setNavigationOnClickListener {
                    mainActivity.removeFragment(POST_WRITE_FRAGMENT)
                }

                inflateMenu(R.menu.menu_post_write)

                setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.item_post_write_camera -> {

                        }
                        R.id.item_post_write_album -> {

                        }
                        R.id.item_post_write_done -> {
                            if(textInputEditTextPostWriteSubject.text.toString() == "" || textInputEditTextPostWriteContent.text.toString() == "") {
                                val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                                    setTitle("게시글 작성 입력 오류")
                                    setMessage("게시글 제목이나 내용이 입력되어 있지 않습니다.")

                                    setPositiveButton("확인", null)
                                }
                                builder.show()
                            } else {
                                mainActivity.replaceFragment(POST_READ_FRAGMENT, true, null)
                            }
                        }
                    }

                    true
                }
            }
            textInputEditTextPostWriteSubject.run {
                requestFocus()
            }
        }
        return fragmentPostWriteBinding.root
    }

}