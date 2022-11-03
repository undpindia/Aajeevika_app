package com.aajeevika.individual.view.suvidha

import android.content.pm.ActivityInfo
import android.os.Bundle
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseActivity
import com.aajeevika.individual.databinding.ActivityVideoPlayerBinding
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.utility.extension.gone
import com.aajeevika.individual.utility.extension.visible
import com.aajeevika.individual.utility.player.FullScreenHelper
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener

class ActivityVideoPlayer : BaseActivity<ActivityVideoPlayerBinding>(R.layout.activity_video_player),
    YouTubePlayerFullScreenListener {
    private val title by lazy { intent.getStringExtra(Constant.TITLE) ?: "" }
    private val videoId by lazy { intent.getStringExtra(Constant.VIDEOID) ?: "" }
    private var fullScreenHelper: FullScreenHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.title = title
        fullScreenHelper =
            FullScreenHelper(this)
        lifecycle.addObserver(viewDataBinding.youtubePlayer)
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            youtubePlayer.addYouTubePlayerListener(object : YouTubePlayerListener {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0F)
                }

                override fun onApiChange(youTubePlayer: YouTubePlayer) {}
                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {}
                override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {}
                override fun onPlaybackQualityChange(youTubePlayer: YouTubePlayer, playbackQuality: PlayerConstants.PlaybackQuality) {}
                override fun onPlaybackRateChange(youTubePlayer: YouTubePlayer, playbackRate: PlayerConstants.PlaybackRate) {}
                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {}
                override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {}
                override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {}
                override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {}
            })

            youtubePlayer.addFullScreenListener(this@ActivityVideoPlayer)
        }
    }

    override fun onBackPressed() {

        if (fullScreenHelper?.isFullScreen == true){
            exitFullScreenMode()
        }else{
            super.onBackPressed()
        }
    }
    override fun onYouTubePlayerEnterFullScreen() {
        viewDataBinding.toolbar.root.gone()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        fullScreenHelper?.enterFullScreen()
    }

    override fun onYouTubePlayerExitFullScreen() {
        viewDataBinding.toolbar.root.visible()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        fullScreenHelper?.exitFullScreen()
    }

    private fun exitFullScreenMode() {
        viewDataBinding.youtubePlayer.exitFullScreen()
        onYouTubePlayerExitFullScreen()
    }

}