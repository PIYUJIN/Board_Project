package com.test.board_project.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel() : ViewModel() {

    var userId = MutableLiveData<String>()
    var userPw = MutableLiveData<String>()
    var userPwCheck = MutableLiveData<String>()

    fun reset(){
        userId.value = ""
        userPw.value = ""
        userPwCheck.value = ""
    }
}

