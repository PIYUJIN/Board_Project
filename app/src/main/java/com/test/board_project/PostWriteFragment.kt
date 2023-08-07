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
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.test.board_project.MainActivity.Companion.POST_READ_FRAGMENT
import com.test.board_project.MainActivity.Companion.POST_WRITE_FRAGMENT
import com.test.board_project.databinding.FragmentPostWriteBinding
import com.test.board_project.repository.PostRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostWriteFragment : Fragment() {

    lateinit var fragmentPostWriteBinding: FragmentPostWriteBinding
    lateinit var mainActivity : MainActivity
    // 업로드할 이미지의 Uri
    var uploadUri: Uri? = null

    lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    lateinit var albumLauncher: ActivityResultLauncher<Intent>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = activity as MainActivity
        fragmentPostWriteBinding = FragmentPostWriteBinding.inflate(inflater)

        // 카메라 설정
        cameraLauncher = cameraSetting(fragmentPostWriteBinding.imageViewPostWriteImage)
        // 앨범 설정
        albumLauncher = albumSetting(fragmentPostWriteBinding.imageViewPostWriteImage)

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
                    when (it.itemId) {
                        R.id.item_post_write_camera -> {
                            val newIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                            // 사진이 저장될 파일 이름
                            val fileName = "/temp_upload.jpg"
                            // 경로
                            val filePath = mainActivity.getExternalFilesDir(null).toString()
                            // 경로 + 파일이름
                            val picPath = "${filePath}/${fileName}"

                            // 사진이 저장될 경로를 관리할 Uri객체 생성
                            // 업로드시 사용할 Uri
                            val file = File(picPath)
                            uploadUri = FileProvider.getUriForFile(mainActivity,
                                "com.test.board_project.file_provider", file)

                            newIntent.putExtra(MediaStore.EXTRA_OUTPUT, uploadUri)
                            cameraLauncher.launch(newIntent)
                        }

                        R.id.item_post_write_album -> {
                            val newIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            newIntent.setType("image/*")
                            val mimeType = arrayOf("image/*")
                            newIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                            albumLauncher.launch(newIntent)
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

            // 게시판 종류 버튼
            buttonPostWriteType.run{
                setOnClickListener {
                    val builder = MaterialAlertDialogBuilder(mainActivity)
                    builder.setTitle("게시판 종류")
                    builder.setItems(mainActivity.boardTypeList){ dialogInterface: DialogInterface, i: Int ->
                        boardType = i + 1
                        text = mainActivity.boardTypeList[i]
                    }
                    builder.setNegativeButton("취소", null)
                    builder.show()
                }
            }
        return fragmentPostWriteBinding.root
    }


    // 카메라 관련 설정
    fun cameraSetting(previewImageView: ImageView) : ActivityResultLauncher<Intent> {
        // 사진 촬영을 위한 런처
        val cameraContract = ActivityResultContracts.StartActivityForResult()
        val cameraLauncher = registerForActivityResult(cameraContract) {
            if(it?.resultCode == AppCompatActivity.RESULT_OK) {
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