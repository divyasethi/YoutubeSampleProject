package com.youtubesampleproject;

/**
 * Created by DivyaSethi on 2020-02-13.
 */

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION;
import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI;

public class YoutubeFragment extends Fragment implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaybackEventListener, YouTubePlayer.PlayerStateChangeListener {
    private static final String ARG_VIDEO_ID = "argVideoId";
    private YouTubePlayer youTubePlayer;
    private static final int RECOVERY_REQUEST = 1;
    private String url = "https://www.youtube.com/watch?v=";
    private Context activityContext;
    private YouTubePlayerSupportFragment playerSupportFragment;

    private String videoId;
    private String youtubeKey = Constants.YOUTUBE_API_KEY;

    public YoutubeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static YoutubeFragment newInstance(String param1) {
        YoutubeFragment fragment = new YoutubeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VIDEO_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoId = getArguments().getString(ARG_VIDEO_ID);
        }
        playerSupportFragment = YouTubePlayerSupportFragment.newInstance();
        playerSupportFragment.initialize(youtubeKey, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_youtube, container, false);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.youtube_fragment_layout, playerSupportFragment).commitAllowingStateLoss();
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Pip) {
            activityContext = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (activityContext != null) {
            final int rotationState = Settings.System.getInt(
                    activityContext.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION, 0
            );
            if (rotationState == 1) {
                player.setFullscreenControlFlags(FULLSCREEN_FLAG_CONTROL_ORIENTATION | FULLSCREEN_FLAG_CONTROL_SYSTEM_UI | FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);

            }
        }
        youTubePlayer = player;
        player.setPlayerStateChangeListener(this);
        player.setPlaybackEventListener(this);
        youTubePlayer.setShowFullscreenButton(false);
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        if (wasRestored) {
            player.play();
        } else {
            youTubePlayer.cueVideo(videoId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(getActivity(), RECOVERY_REQUEST).show();
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void onPlaying() {
        // Called when playback starts, either due to user action or call to play().
    }

    @Override
    public void onPaused() {
        // Called when playback is paused, either due to user action or call to pause().
    }

    @Override
    public void onStopped() {
        // Called when playback stops for a reason other than being paused.
    }

    @Override
    public void onBuffering(boolean buffering) {
        // Called when buffering starts or ends.
    }

    @Override
    public void onSeekTo(int i) {
        // Called when a jump in playback position occurs, either
        // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
    }


    @Override
    public void onLoading() {
        // Called when the player is loading a video
        // At this point, it's not ready to accept commands affecting playback such as play() or pause()
    }

    @Override
    public void onLoaded(String s) {
        // Called when a video is done loading.
        // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
        youTubePlayer.play();
    }

    @Override
    public void onAdStarted() {
        // Called when playback of an advertisement starts.
    }

    @Override
    public void onVideoStarted() {
        // Called when playback of the video starts.
    }

    @Override
    public void onVideoEnded() {
        // Called when the video reaches its end.
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        // Called when an error occurs.
        System.out.println("inside onError and error reason = " + errorReason.toString());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (youTubePlayer != null && youTubePlayer.isPlaying()) {
            youTubePlayer.pause();
        }
    }

    @Override
    public void onDestroy() {
        if (youTubePlayer != null) {
            youTubePlayer.release();
            youTubePlayer = null;
        }

        super.onDestroy();
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        if (youTubePlayer == null) {
            return;
        }

        if (isInPictureInPictureMode) {
            youTubePlayer.setShowFullscreenButton(false);
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        } else {
            youTubePlayer.setShowFullscreenButton(false);
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        }
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void loadVideo() {
        if (youTubePlayer != null) {
            youTubePlayer.cueVideo(videoId);
        }
    }
}
