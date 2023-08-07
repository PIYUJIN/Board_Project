package com.test.board_project

import android.content.DialogInterface
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
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout.END_ICON_CLEAR_TEXT
import com.test.board_project.databinding.FragmentPostReadBinding
import com.test.board_project.vm.PostViewModel

class PostReadFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentPostReadBinding: FragmentPostReadBinding

    lateinit var postViewModel: PostViewModel

    var readPostIdx = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = activity as MainActivity
        fragmentPostReadBinding = FragmentPostReadBinding.inflate(inflater)

        postViewModel = ViewModelProvider(mainActivity)[PostViewModel::class.java]
        postViewModel.run{
            postSubject.observe(mainActivity) {
                fragmentPostReadBinding.textInputEditTextPostReadSubject.setText(it)
            }
            postText.observe(mainActivity) {
                fragmentPostReadBinding.textInputEditTextPostReadContent.setText(it)
            }
            postNickname.observe(mainActivity) {
                fragmentPostReadBinding.textInputEditTextPostReadUserName.setText(it)
            }
            postWriteDate.observe(mainActivity) {
                fragmentPostReadBinding.textInputEditTextPostReadWriteDate.setText(it)
            }
            postFileName.observe(mainActivity) {
                if(it == "None"){
                    fragmentPostReadBinding.imageViewPostRead.visibility = View.GONE
                }
            }
            postImage.observe(mainActivity) {
                fragmentPostReadBinding.imageViewPostRead.visibility = View.VISIBLE
                fragmentPostReadBinding.imageViewPostRead.setImageBitmap(it)
            }
        }

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
                            toolbarPostRead.title = "게시글 수정"
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

            textInputEditTextPostReadUserName.run{
                setTextColor(Color.BLACK)
            }

            textInputEditTextPostReadWriteDate.run{
                setTextColor(Color.BLACK)
            }

            buttonModifyPost.run {
                setOnClickListener {
                    checkModify()
                }
            }
        }

        readPostIdx = arguments?.getLong("readPostIdx")!!
        // 게시글 정보 가져오기
        postViewModel.setPostReadData(readPostIdx.toDouble())

        return fragmentPostReadBinding.root
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v != null) {
            when (v.id) {
                R.id.imageViewPostRead -> {
                    mainActivity.menuInflater.inflate(R.menu.menu_post_modify, menu)
                }
            }
        }

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_post_modify_camera -> {
                val newIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                // 사진이 저장될 파일 이름
                val fileName = "/temp_upload.jpg"
                // 경로
                val filePath = mainActivity.getExternalFilesDir(null).toString()
                // 경로 + 파일이름
                val picPath = "${filePath}/${fileName}"

                // 사진이 저장될 경로를 관리할 Uri객체를 만들어준다.
                // 업로드할 때 사용할 Uri이다.
                val file = File(picPath)
                uploadUri = FileProvider.getUriForFile(mainActivity,
                    "com.test.mini02_boardproject02.file_provider", file)

                newIntent.putExtra(MediaStore.EXTRA_OUTPUT, uploadUri)
                cameraLauncher.launch(newIntent)
            }
            R.id.item_post_modify_album -> {
                val newIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                newIntent.setType("image/*")
                val mimeType = arrayOf("image/*")
                newIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                albumLauncher.launch(newIntent)
            }
        }
        return super.onContextItemSelected(item)
    }

    fun checkModify() {
        fragmentPostReadBinding.run {

            var postSubject = textInputEditTextPostReadSubject.text.toString()
            var postContent = textInputEditTextPostReadContent.text.toString()
            if(!textInputEditTextPostReadSubject.isEnabled) {
                registerForContextMenu(imageViewPostRead)
                buttonModifyPost.visibility = View.VISIBLE
                textInputEditTextPostReadSubject.isEnabled = true
                textInputEditTextPostReadContent.isEnabled = true
            } else {
                if(postSubject.isEmpty()) {
                    val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                        setTitle("게시글 수정 입력 오류")
                        setMessage("게시글 제목이 입력되어 있지 않습니다.")
                        setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                            mainActivity.showSoftInput(textInputEditTextPostReadSubject)
                        }
                    }
                    builder.show()
                }

                else if (postContent.isEmpty()) {
                    val builder = MaterialAlertDialogBuilder(mainActivity).apply {
                        setTitle("게시글 수정 입력 오류")
                        setMessage("게시글 내용이 입력되어 있지 않습니다.")
                        setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                            mainActivity.showSoftInput(textInputEditTextPostReadContent)
                        }
                    }
                    builder.show()
                }
                else {
                    val t1 = Toast.makeText(mainActivity, "게시글 수정 완료", Toast.LENGTH_LONG)
                    t1.show()

                    toolbarPostRead.title = "게시글 보기"
                    buttonModifyPost.visibility = View.GONE
                    textInputEditTextPostReadSubject.isEnabled = false
                    textInputEditTextPostReadContent.isEnabled = false
                }
            }
        }
    }
}