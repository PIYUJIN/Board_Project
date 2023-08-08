package com.test.board_project.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.test.board_project.PostDataClass
import com.test.board_project.repository.PostRepository
import com.test.board_project.repository.UserRepository
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class PostViewModel() : ViewModel() {

    var postSubject = MutableLiveData<String>()
    var postText = MutableLiveData<String>()
    var postImage = MutableLiveData<Bitmap>()
    var postNickname = MutableLiveData<String>()
    var postWriteDate = MutableLiveData<String>()
    // 이미지 파일 이름
    var postFileName = MutableLiveData<String>()

    // 게시글 목록
    var postDataList = MutableLiveData<MutableList<PostDataClass>>()
    // 게시글 작성자 닉네임
    var postWriterNicknameList = MutableLiveData<MutableList<String>>()

    init {
        postDataList.value = mutableListOf<PostDataClass>()
        postWriterNicknameList.value = mutableListOf<String>()
    }

    // 게시글 읽기 화면
    fun setPostReadData(postIdx:Double) {
        // 게시글 데이터를 가져온다
        PostRepository.getPostInfo(postIdx) {
            for(c1 in it.result.children) {
                // 게시글 제목
                postSubject.value = c1.child("postSubject").value as String
                // 게시글 내용
                postText.value = c1.child("postText").value as String
                // 게시글 작성 날짜
                postWriteDate.value = c1.child("postWriteDate").value as String
                // 이미지 파일 이름
                postFileName.value = c1.child("postImage").value as String

                val postWriterIdx = c1.child("postWriterIdx").value as Long
                UserRepository.getUserInfoByUserIdx(postWriterIdx){
                    for(c2 in it.result.children){
                        // 작성자 닉네임
                        postNickname.value = c2.child("userNickname").value as String
                    }
                }
                // 이미지가 있다면
                if(postFileName.value != "None") {
                    PostRepository.getPostImage(postFileName.value!!) {
                        thread {
                            // 파일에 접근할 수 있는 경로를 이용해 URL 객체를 생성한다.
                            val url = URL(it.result.toString())
                            // 접속한다.
                            val httpURLConnection = url.openConnection() as HttpURLConnection
                            // 이미지 객체를 생성한다.
                            val bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)
                            postImage.postValue(bitmap)
                        }
                    }
                }
            }
        }
    }

    // 게시글 목록
    fun getPostAll(getPostType:Long) {
        val tempList = mutableListOf<PostDataClass>()
        val tempList2 = mutableListOf<String>()

        PostRepository.getPostAll {
            for(c1 in it.result.children) {
                val postIdx = c1.child("postIdx").value as Long
                val postImage = c1.child("postImage").value as String
                val postSubject = c1.child("postSubject").value as String
                val postText = c1.child("postText").value as String
                val postType = c1.child("postType").value as Long
                val postWriteDate = c1.child("postWriteDate").value as String
                val postWriterIdx = c1.child("postWriterIdx").value as Long

                if(getPostType != 0L && getPostType != postType) {
                    continue
                }

                val p1 = PostDataClass(postIdx, postType, postSubject, postText, postWriteDate, postImage, postWriterIdx)
                tempList.add(p1)

                UserRepository.getUserInfoByUserIdx(postWriterIdx) {
                    for(c2 in it.result.children) {
                        val postWriterNickname = c2.child("userNickname").value as String
                        tempList2.add(postWriterNickname)
                    }

                }
            }
            // 데이터가 postIdx를 기준으로 오름 차순 정렬 -> 순서 반대로 설정
            tempList.reverse()
            tempList2.reverse()

            postDataList.value = tempList
            postWriterNicknameList.value = tempList2
        }
    }

    // postDataList 초기화
    fun resetPostList() {
        postDataList.value = mutableListOf<PostDataClass>()
        postWriterNicknameList.value = mutableListOf<String>()
    }

    // 검색 결과를 가져온다.
    fun getSearchPostList(getPostType:Long, keyword:String) {
        // 검색 결과 리스트
        val tempList = mutableListOf<PostDataClass>()
        val tempList2 = mutableListOf<String>()

        PostRepository.getPostAll {
            for(c1 in it.result.children) {
                val postIdx = c1.child("postIdx").value as Long
                val postImage = c1.child("postImage").value as String
                val postSubject = c1.child("postSubject").value as String
                val postText = c1.child("postText").value as String
                val postType = c1.child("postType").value as Long
                val postWriteDate = c1.child("postWriteDate").value as String
                val postWriterIdx = c1.child("postWriterIdx").value as Long

                if(getPostType != 0L && getPostType != postType) {
                    continue
                }

                if(!postSubject.contains(keyword) && !postText.contains(keyword)) {
                    continue
                }

                val p1 = PostDataClass(postIdx, postType, postSubject, postText, postWriteDate, postImage, postWriterIdx)
                tempList.add(p1)

                UserRepository.getUserInfoByUserIdx(postWriterIdx) {
                    for(c2 in it.result.children) {
                        val postWriterNickname = c2.child("userNickname").value as String
                        tempList2.add(postWriterNickname)
                    }

                }
            }
            // 데이터가 postIdx를 기준으로 오름 차순 정렬 -> 순서 반대로 설정
            tempList.reverse()
            tempList2.reverse()

            postDataList.value = tempList
            postWriterNicknameList.value = tempList2
        }

    }
}