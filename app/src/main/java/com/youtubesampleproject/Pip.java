package com.youtubesampleproject;

import android.annotation.TargetApi;
import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


@TargetApi(Build.VERSION_CODES.O)
public class Pip extends AppCompatActivity {

    private String videoId;
    private boolean shouldFinishActivity = false;
    private PictureInPictureParams.Builder pictureInPictureParams;
    private YoutubeFragment youtubeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        startPipMode();
        videoId = getIntent().getStringExtra(Constants.YOUTUBE_ID);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x000000));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictureinpicture);

        youtubeFragment = YoutubeFragment.newInstance(videoId);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_layout, youtubeFragment).commit();
    }

    private void startPipMode() {
        Rational aspectRatio = new Rational(200, 110);
        pictureInPictureParams = new PictureInPictureParams.Builder();
        pictureInPictureParams.setAspectRatio(aspectRatio).build();
        enterPictureInPictureMode(pictureInPictureParams.build());
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {

        if (isInPictureInPictureMode) {
            shouldFinishActivity = false;
        } else {
            if (shouldFinishActivity == true) {
                finish();
            }
        }
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isInPictureInPictureMode()) {
            shouldFinishActivity = true;
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        videoId = intent.getStringExtra(Constants.YOUTUBE_ID);
        youtubeFragment.setVideoId(videoId);
        youtubeFragment.loadVideo();
        super.onNewIntent(intent);
    }
}
