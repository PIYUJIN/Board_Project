package com.test.board_project

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        splashScreenCustomizing(splashScreen)

        setContentView(R.layout.activity_main)
    }

    // SplashScreen 커스터마이징
    fun splashScreenCustomizing(splashScreen: androidx.core.splashscreen.SplashScreen){
        // SplashScreen이 사라질 때 동작하는 리스너를 설정한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener{
                // 가로 비율 애니메이션
                val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 2f, 1f, 0f)
                // 세로 비율 애니메이션
                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 2f, 1f, 0f)
                // 투명도
                val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 1f, 0.5f, 0f)

                // 애니메이션 관리 객체를 생성한다.
                // 첫 번째 : 애니메이션을 적용할 뷰
                // 나머지는 적용한 애니메이션 종류
                val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(it.iconView, scaleX, scaleY, alpha)
                // 애니메이션 적용을 위한 에이징
                objectAnimator.interpolator = AnticipateInterpolator()
                // 애니메이션 동작 시간
                objectAnimator.duration = 1000
                // 애니메이션이 끝났을 때 동작할 리스너
                objectAnimator.addListener(object : AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)

                        // SplashScreen을 제거한다.
                        it.remove()
                    }
                })
                // 애니메이션 가동
                objectAnimator.start()
            }
        }
    }
}