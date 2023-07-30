package com.test.board_project

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout.END_ICON_CLEAR_TEXT
import com.test.board_project.databinding.FragmentPostReadBinding

class PostReadFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentPostReadBinding: FragmentPostReadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = activity as MainActivity
        fragmentPostReadBinding = FragmentPostReadBinding.inflate(inflater)

        fragmentPostReadBinding.run{

            buttonModifyPost.visibility = View.GONE

            toolbarPostRead.run{
                title = "게시글 보기"

                setNavigationIcon(R.drawable.arrow_back_24px)

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    navigationIcon?.colorFilter = BlendModeColorFilter(Color.DKGRAY, BlendMode.SRC_ATOP)
                } else {
                    navigationIcon?.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)
                }

                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.POST_READ_FRAGMENT)
                    mainActivity.removeFragment(MainActivity.POST_WRITE_FRAGMENT)
                }

                inflateMenu(R.menu.menu_post_read)

                setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.item_post_read_modify -> {
                            checkModify()
                        }
                        R.id.item_post_read_delete -> {
                            mainActivity.removeFragment(MainActivity.POST_READ_FRAGMENT)
                            mainActivity.removeFragment(MainActivity.POST_WRITE_FRAGMENT)
                        }
                    }

                    true
                }
            }

            textInputEditTextPostReadSubject.run{
                setTextColor(Color.BLACK)
            }

            textInputEditTextPostReadContent.run{
                setTextColor(Color.BLACK)
            }

            buttonModifyPost.run {
                setOnClickListener {
                    checkModify()
                }
            }
        }

        return fragmentPostReadBinding.root
    }

    fun checkModify() {
        fragmentPostReadBinding.run {
            if(!textInputEditTextPostReadSubject.isEnabled) {
                buttonModifyPost.visibility = View.VISIBLE
                textInputEditTextPostReadSubject.isEnabled = true
                textInputEditTextPostReadContent.isEnabled = true
            } else {
                if(textInputEditTextPostReadSubject.text.toString() == "" || textInputEditTextPostReadContent.text.toString() == "") {
                    val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                        setTitle("게시글 수정 입력 오류")
                        setMessage("게시글 제목이나 내용이 입력되어 있지 않습니다.")

                        setPositiveButton("확인", null)
                    }
                    builder.show()
                } else {
                    val t1 = Toast.makeText(mainActivity, "게시글 수정 완료", Toast.LENGTH_LONG)
                    t1.show()

                    buttonModifyPost.visibility = View.GONE
                    textInputEditTextPostReadSubject.isEnabled = false
                    textInputEditTextPostReadContent.isEnabled = false
                }
            }
        }
    }
}