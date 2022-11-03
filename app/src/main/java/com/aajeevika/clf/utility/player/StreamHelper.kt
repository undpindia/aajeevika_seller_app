package com.aajeevika.clf.utility.player

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import com.aajeevika.clf.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.regex.Matcher
import java.util.regex.Pattern

class StreamHelper(
    private val mActivity: AppCompatActivity,
    private val mCallback: FullScreenCallBack? = null
) : YouTubePlayerFullScreenListener {
    private var youTubePlayerView: YouTubePlayerView? = null
    private var mYouTubePlayer: YouTubePlayer? = null
    private var fullScreenHelper: FullScreenHelper? = null
    private var mVideoId: String = ""

    init {
        fullScreenHelper = FullScreenHelper(mActivity)
    }

    fun releaseYtPlayer() {
        youTubePlayerView?.release()
        mYouTubePlayer = null
    }

    fun playYoutube() {
        mYouTubePlayer?.play()
    }

    private fun isUrlM3u8(url: String?): Boolean {
        var isM3u8 = false
        url?.let { ads ->
            val ln = ads.lastIndexOf(".")
            isM3u8 = (ln >= 0 && ads.substring(ln).contains("m3u8"))
        }
        return isM3u8
    }

    fun onResume() {
        youTubePlayerView?.post {
            if (fullScreenHelper?.isFullScreen == true) {
                youTubePlayerView?.enterFullScreen()
                fullScreenHelper?.enterFullScreen()
            }
        }
    }

    fun canGoBack(): Boolean {
        return if (fullScreenHelper?.isFullScreen == true) {
            exitFullScreenMode()
            false
        } else {
            true
        }
    }

    fun initYoutube(mVideoUrl: String) {
        this.mVideoId = getYoutubeId(mVideoUrl)

        if (mYouTubePlayer != null) {
            mYouTubePlayer?.loadVideo(mVideoId, 0F)
            return
        }

        youTubePlayerView = mActivity.window?.decorView?.rootView?.findViewById<YouTubePlayerView?>(R.id.youtubeView)
        youTubePlayerView?.let { ytView ->
            mActivity.lifecycle.addObserver(ytView)

            ytView.initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    mYouTubePlayer = youTubePlayer
                    youTubePlayer.cueVideo(mVideoId, 0f)
                    youTubePlayer.play()
                }

                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                    super.onStateChange(youTubePlayer, state)
                    if (state.name == PlayerConstants.PlayerState.ENDED.name) mCallback?.onPlayNext()
                }
            })

            ytView.addFullScreenListener(this)
        }
    }

    override fun onYouTubePlayerEnterFullScreen() {
        mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        fullScreenHelper?.enterFullScreen()
        mCallback?.onEnterFullScreen()
    }

    override fun onYouTubePlayerExitFullScreen() {
        mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        fullScreenHelper?.exitFullScreen()
        mCallback?.onExitFullScreen()
    }

    private fun exitFullScreenMode() {
        youTubePlayerView?.exitFullScreen()
        onYouTubePlayerExitFullScreen()
    }


    fun getYoutubeId(url:String): String {
        val pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"

        val compiledPattern: Pattern = Pattern.compile(pattern)
        val matcher: Matcher = compiledPattern.matcher(url)

        return if (matcher.find()) matcher.group() else ""
    }
}

