package pl.kopsoft.pczplan.activities

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import kotlinx.android.synthetic.main.activity_splash.*
import pl.kopsoft.pczplan.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val bounce = AnimatorInflater.loadAnimator(this, R.animator.anim_bounce)
        val makeVisible = AnimatorInflater.loadAnimator(this, R.animator.anim_makevisible)
        val grow = AnimatorInflater.loadAnimator(this, R.animator.anim_grow)

        val shrinkAccent = AnimatorSet()
        shrinkAccent.play(bounce)
        shrinkAccent.setTarget(splash_accent)

        val makeLogoVisible = AnimatorSet()
        makeLogoVisible.play(makeVisible)
        makeLogoVisible.startDelay = 500
        makeLogoVisible.setTarget(splash_logo_imageview)

        val growDark = AnimatorSet()
        growDark.play(grow)
        growDark.startDelay = 3000
        growDark.setTarget(splash_background)

        val anim = AnimatorSet()
        anim.playTogether(shrinkAccent, makeLogoVisible, growDark)
        anim.start()
        anim.doOnEnd {
            val intent = Intent(this, SemestersActivity::class.java)
            startActivity(intent)
        }
    }
}
