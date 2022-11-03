package com.aajeevika.clf.view.application

import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivity
import com.aajeevika.clf.databinding.ActivityVideoPlayerBinding
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.player.FullScreenCallBack
import com.aajeevika.clf.utility.player.StreamHelper

class ActivityVideoPlayer : BaseActivity<ActivityVideoPlayerBinding>(R.layout.activity_video_player), FullScreenCallBack {
    private val mVideoUrl: String by lazy { intent.getStringExtra(Constant.WEB_URL) ?: "https://www.youtube.com/watch?v=PFnJvUNK6t0" }
    private val mStreamHelper: StreamHelper by lazy { StreamHelper(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initYoutube()
    }

    override fun onResume() {
        super.onResume()
        mStreamHelper.onResume()
    }

    override fun initListener() {
        viewDataBinding.run {
            ivCrossBtn.setOnClickListener {
                finish()
            }
        }
    }

    override fun onBackPressed() {
        if (mStreamHelper.canGoBack()) super.onBackPressed()
    }

    private fun initYoutube() {
        mStreamHelper.initYoutube(mVideoUrl)
    }

    override fun onEnterFullScreen() {
    }

    override fun onExitFullScreen() {
    }

    override fun onPlayNext() {
    }

}