package com.test.board_project.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.test.board_project.PostDataClass

class PostRepository {

    companion object{

        // 게시글 인덱스 번호를 가져온다.
        fun getPostIdx(callback1:(Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            // 게시글 인덱스 번호
            val postIdxRef = database.getReference("PostIdx")
            postIdxRef.get().addOnCompleteListener(callback1)
        }

        // 게시글 번호를 저장한다.
        fun setPostIdx(postIdx:Long, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val postIdxRef = database.getReference("PostIdx")
            // 게시글 인덱스번호 저장
            postIdxRef.get().addOnCompleteListener {
                it.result.ref.setValue(postIdx).addOnCompleteListener(callback1)
            }
        }

        // 게시글 정보를 저장한다.
        fun addPostInfo(postDataClass:PostDataClass, callback1:(Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("PostData")
            postDataRef.push().setValue(postDataClass).addOnCompleteListener(callback1)
        }

        // 이미지 업로드
        fun uploadImage(uploadUri: Uri, fileName:String, callback1:(Task<UploadTask.TaskSnapshot>) -> Unit) {
            val storage = FirebaseStorage.getInstance()
            val imageRef = storage.reference.child(fileName)
            imageRef.putFile(uploadUri).addOnCompleteListener(callback1)
        }

        // 게시글 정보를 가져온다.
        fun getPostInfo(postIdx:Double, callback1 : (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("PostData")
            postDataRef.orderByChild("postIdx").equalTo(postIdx).get().addOnCompleteListener(callback1)
        }

        // 게시글 이미지를 가져온다.
        fun getPostImage(fileName:String, callback1: (Task<Uri>) -> Unit) {
            val storage = FirebaseStorage.getInstance()
            val fileRef = storage.reference.child(fileName)

            // 데이터를 가져올 수 있는 경로를 가져온다.
            fileRef.downloadUrl.addOnCompleteListener(callback1)
        }

        // 게시글 정보 전체를 가져온다.
        fun getPostAll(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("PostData")
            postDataRef.orderByChild("postIdx").get().addOnCompleteListener(callback1)
        }

        // 특정 게시판의 글 정보만 가져온다.
        fun getPostOne(postType:Long, callback1:(Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("PostData")
            postDataRef.orderByChild("postType").equalTo(postType.toDouble())
                .ref.orderByChild("postIdx").get().addOnCompleteListener(callback1)
        }

        // 이미지 삭제
        fun removeImage(fileName:String, callback1:(Task<Void>) -> Unit) {
            val storage = FirebaseStorage.getInstance()
            val fileRef = storage.reference.child(fileName)
            // 파일을 삭제한다.
            fileRef.delete().addOnCompleteListener (callback1)
        }

        // 글 삭제
        fun removePost(postIdx:Long, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val testDataRef = database.getReference("PostData")

            testDataRef.orderByChild("postIdx").equalTo(postIdx.toDouble()).get().addOnCompleteListener {
                for(a1 in it.result.children) {
                    // 해당 데이터 삭제
                    a1.ref.removeValue().addOnCompleteListener(callback1)
                }
            }
        }

        // 글 수정
        fun modifyPost(postDataClass: PostDataClass, isNewImage:Boolean, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("PostData")

            postDataRef.orderByChild("postIdx").equalTo(postDataClass.postIdx.toDouble()).get().addOnCompleteListener {
                for(a1 in it.result.children){
                    if(isNewImage){
                        a1.ref.child("postImage").setValue(postDataClass.postImage)
                    }
                    a1.ref.child("postSubject").setValue(postDataClass.postSubject)
                    a1.ref.child("postText").setValue(postDataClass.postText).addOnCompleteListener(callback1)
                }
            }
        }
    }
}