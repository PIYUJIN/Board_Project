package com.test.board_project

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.graphics.PorterDuff
import android.media.ExifInterface
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout.END_ICON_CLEAR_TEXT
import com.test.board_project.databinding.FragmentPostReadBinding
import com.test.board_project.repository.PostRepository
import com.test.board_project.vm.PostViewModel
import java.io.File

class PostReadFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentPostReadBinding: FragmentPostReadBinding

    lateinit var postViewModel: PostViewModel

    var readPostIdx = 0L

    // 업로드할 이미지의 Uri
    var uploadUri: Uri? = null

    lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    // 새로운 이미지 설정 여부
    var isSelectNewImage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = activity as MainActivity
        fragmentPostReadBinding = FragmentPostReadBinding.inflate(inflater)

        // 카메라 설정
        cameraLauncher = cameraSetting(fragmentPostReadBinding.imageViewPostRead)
        // 앨범 설정
        albumLauncher = albumSetting(fragmentPostReadBinding.imageViewPostRead)

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
                    // 용량이 큰 이미지를 받아오기 전까지 보여줄 이미지 설정
                    fragmentPostReadBinding.imageViewPostRead.setImageResource(R.mipmap.app_icon_foreground)
                }
            }
            postImage.observe(mainActivity) {
                fragmentPostReadBinding.imageViewPostRead.visibility = View.VISIBLE
                fragmentPostReadBinding.imageViewPostRead.setImageBitmap(it)
            }
        }

        fragmentPostReadBinding.run {

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
                            // 글 삭제
                            PostRepository.removePost(readPostIdx) {
                                // 이미지가 있는 경우 삭제
                                if(postViewModel.postFileName.value != "None") {
                                    PostRepository.removeImage(postViewModel.postFileName.value!!) {
                                        mainActivity.removeFragment(MainActivity.POST_WRITE_FRAGMENT)
                                        mainActivity.removeFragment(MainActivity.POST_READ_FRAGMENT)
                                    }
                                }
                                else {
                                    mainActivity.removeFragment(MainActivity.POST_READ_FRAGMENT)
                                    mainActivity.removeFragment(MainActivity.POST_WRITE_FRAGMENT)
                                }
                            }
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

                    val fileName = if(!isSelectNewImage) {
                        "None"
                    } else {
                        if(postViewModel.postFileName.value == "None") {
                            "image/img_${System.currentTimeMillis()}.jpg"
                        } else {
                            postViewModel.postFileName.value
                        }
                    }

                    val postDataClass = PostDataClass(readPostIdx, 0, postSubject,
                        postContent, "", fileName!!, mainActivity.loginUserClass.userIdx)

                    PostRepository.modifyPost(postDataClass, isSelectNewImage) {
                        if(isSelectNewImage) {
                            PostRepository.uploadImage(uploadUri!!,fileName) {
                                val t1 = Toast.makeText(mainActivity, "게시글 수정 완료", Toast.LENGTH_LONG)
                                t1.show()

                                toolbarPostRead.title = "게시글 보기"
                                unregisterForContextMenu(imageViewPostRead)
                                buttonModifyPost.visibility = View.GONE
                                textInputEditTextPostReadSubject.isEnabled = false
                                textInputEditTextPostReadContent.isEnabled = false
                            }
                        } else {
                            val t1 = Toast.makeText(mainActivity, "게시글 수정 완료", Toast.LENGTH_LONG)
                            t1.show()

                            toolbarPostRead.title = "게시글 보기"
                            unregisterForContextMenu(imageViewPostRead)
                            buttonModifyPost.visibility = View.GONE
                            textInputEditTextPostReadSubject.isEnabled = false
                            textInputEditTextPostReadContent.isEnabled = false
                        }
                    }
                }
            }
        }
    }

    // 카메라 관련 설정
    fun cameraSetting(previewImageView: ImageView) : ActivityResultLauncher<Intent> {

        // 사진 촬영을 위한 런처
        val cameraContract = ActivityResultContracts.StartActivityForResult()
        val cameraLauncher = registerForActivityResult(cameraContract) {
            if(it?.resultCode == AppCompatActivity.RESULT_OK) {

                // 새로운 이미지 선택 여부 설정
                isSelectNewImage = true

                // Uri를 이용해 이미지에 접근하여 Bitmap 객체 생성
                val bitmap = BitmapFactory.decodeFile(uploadUri?.path)

                // 이미지 크기 조정
                val ratio = 1024.0 / bitmap.width
                val targetHeight = (bitmap.height * ratio).toInt()
                val bitmap2 = Bitmap.createScaledBitmap(bitmap, 1024, targetHeight, false)

                // 회전 각도값
                val degree = getDegree(uploadUri!!)

                // 회전 이미지 생성
                val matrix = Matrix()
                matrix.postRotate(degree.toFloat())
                val bitmap3 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.width, bitmap2.height, matrix, false)
                previewImageView.setImageBitmap(bitmap3)
            }
        }

        return cameraLauncher
    }

    // 이미지 파일에 기록되어 있는 회전 정보를 가져온다.
    fun getDegree(uri:Uri) : Int {

        var exifInterface: ExifInterface? = null

        // 사진 파일로 부터 tag 정보를 관리하는 객체를 추출한다.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val photoUri = MediaStore.setRequireOriginal(uri)
            // 스트림을 추출한다.
            val inputStream = mainActivity.contentResolver.openInputStream(photoUri)
            // ExifInterface 정보를 읽엉돈다.
            exifInterface = ExifInterface(inputStream!!)
        } else {
            exifInterface = ExifInterface(uri.path!!)
        }

        var degree = 0
        if(exifInterface != null){
            // 각도 값을 가지고온다.
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)

            when(orientation){
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        }
        return degree
    }

    // 앨범 관련 설정
    fun albumSetting(previewImageView: ImageView) : ActivityResultLauncher<Intent> {

        val albumContract = ActivityResultContracts.StartActivityForResult()
        val albumLauncher = registerForActivityResult(albumContract) {

            if(it.resultCode == AppCompatActivity.RESULT_OK) {
                // 선택한 이미지에 접근할 수 있는 Uri 객체를 추출한다.
                if(it.data?.data != null){

                    // 새로운 이미지 선택 여부 설정
                    isSelectNewImage = true

                    uploadUri = it.data?.data

                    // 안드로이드 10 (Q) 이상이라면...
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        // 이미지를 생성할 수 있는 디코더를 생성한다.
                        val source = ImageDecoder.createSource(mainActivity.contentResolver, uploadUri!!)
                        // Bitmap객체를 생성한다.
                        val bitmap = ImageDecoder.decodeBitmap(source)

                        previewImageView.setImageBitmap(bitmap)
                    } else {
                        // 컨텐츠 프로바이더를 통해 이미지 데이터 정보를 가져온다.
                        val cursor = mainActivity.contentResolver.query(uploadUri!!, null, null, null, null)
                        if(cursor != null){
                            cursor.moveToNext()

                            // 이미지의 경로를 가져온다.
                            val idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                            val source = cursor.getString(idx)

                            // 이미지를 생성하여 보여준다.
                            val bitmap = BitmapFactory.decodeFile(source)
                            previewImageView.setImageBitmap(bitmap)
                        }
                    }
                }
            }
        }

        return albumLauncher
    }
}