package com.test.board_project.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.test.board_project.UserClass

class UserRepository {

    companion object{

        // 사용자 인덱스 번호를 가져온다.
        fun getUserIdx(callback1:(Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userIdxRef = database.getReference("UserIdx")

            userIdxRef.get().addOnCompleteListener(callback1)
        }

        // 사용자 인덱스 번호를 설정한다.
        fun setUserIdx(userIdx:Long, callback1: (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userIdxRef = database.getReference("UserIdx")

            userIdxRef.get().addOnCompleteListener {
                it.result.ref.setValue(userIdx).addOnCompleteListener(callback1)
            }
        }

        // 사용자 정보를 저장한다.
        fun addUserInfo(userClass: UserClass, callback1:(Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")
            userDataRef.push().setValue(userClass).addOnCompleteListener(callback1)
        }

        // 사용자 아이디를 통해 사용자 정보를 가져온다.
        fun getUserInfoByUserId(loginUserId:String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")

            // userId가 사용자가 입력한 아이디와 같은 데이터를 가져온다.
            userDataRef.orderByChild("userId").equalTo(loginUserId).get().addOnCompleteListener(callback1)
        }

        // 사용자 인덱스를 통해 사용자 정보를 가져온다.
        fun getUserInfoByUserIdx(userIdx:Long, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")

            userDataRef.orderByChild("userIdx").equalTo(userIdx.toDouble()).get().addOnCompleteListener(callback1)
        }
    }
}