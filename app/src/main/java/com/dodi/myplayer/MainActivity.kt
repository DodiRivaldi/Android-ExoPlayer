package com.dodi.myplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.dodi.myplayer.databinding.ActivityMainBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.util.Util

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var player: ExoPlayer? = null

    private fun initPlayer(){
        val mediaItem = MediaItem.fromUri(URL_VIDEO_DICODING)
        val anotherMediaItem = MediaItem.fromUri(URL_AUDIO)

        player = ExoPlayer.Builder(this).build().also {
            binding.videoView.player = it
            it.setMediaItem(mediaItem)
            it.addMediaItem(anotherMediaItem)
            it.prepare()
        }
    }

    private fun releasePlayer(){
        player?.release()
        player = null
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23){
            initPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 && player == null){
            initPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT<= 23){
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT>23){
            releasePlayer()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun hideSystemUi(){
        WindowCompat.setDecorFitsSystemWindows(window,false)
        WindowInsetsControllerCompat(window,binding.videoView).let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    companion object {
        const val URL_VIDEO_DICODING = "https://github.com/dicodingacademy/assets/releases/download/release-video/VideoDicoding.mp4"
        const val URL_AUDIO = "https://github.com/dicodingacademy/assets/raw/main/android_intermediate_academy/bensound_ukulele.mp3"
    }
}