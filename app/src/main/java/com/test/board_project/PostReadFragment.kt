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
                            if(!textInputEditTextPostReadSubject.isEnabled) {
                                textInputEditTextPostReadSubject.isEnabled = true
                                textInputEditTextPostReadContent.isEnabled = true
                            } else {
                                textInputEditTextPostReadSubject.isEnabled = false
                                textInputEditTextPostReadContent.isEnabled = false
                            }
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
        }

        return fragmentPostReadBinding.root
    }
}