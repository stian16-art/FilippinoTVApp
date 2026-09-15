package com.example.filippinotvapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.ui.PlayerView;

public class PlayerActivity extends AppCompatActivity {
    
    private static final String TAG = "PlayerActivity";
    
    private PlayerView playerView;
    private ExoPlayer player;
    private String channelName;
    private String streamUrl;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        
        // Hide system UI for immersive experience
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        
        initializePlayer();
        getChannelData();
    }
    
    private void initializePlayer() {
        playerView = findViewById(R.id.player_view);
        
        // Create ExoPlayer instance
        player = new ExoPlayer.Builder(this)
                .setMediaSourceFactory(new DefaultMediaSourceFactory(this)
                    .setDataSourceFactory(new DefaultHttpDataSource.Factory()
                        .setUserAgent("FilippinoTVApp/1.0")))
                .build();
        
        playerView.setPlayer(player);
    }
    
    private void getChannelData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            channelName = extras.getString("channel_name", "Unknown Channel");
            streamUrl = extras.getString("stream_url", "");
            
            Log.d(TAG, "Channel: " + channelName);
            Log.d(TAG, "Stream URL: " + streamUrl);
            
            if (!streamUrl.isEmpty()) {
                playStream();
            } else {
                Log.e(TAG, "Stream URL is empty");
                showError("Invalid stream URL");
            }
        }
    }
    
    private void playStream() {
        try {
            Uri streamUri = Uri.parse(streamUrl);
            
            // Create media source based on URL type
            MediaSource mediaSource;
            
            if (streamUrl.contains(".m3u8")) {
                // HLS Stream
                mediaSource = new HlsMediaSource.Factory(
                    new DefaultHttpDataSource.Factory()
                        .setUserAgent("FilippinoTVApp/1.0"))
                    .createMediaSource(MediaItem.fromUri(streamUri));
                
                Log.d(TAG, "Loading HLS stream: " + streamUrl);
            } else if (streamUrl.contains(".mpd")) {
                // DASH Stream
                mediaSource = new DefaultMediaSourceFactory(this)
                    .createMediaSource(MediaItem.fromUri(streamUri));
                
                Log.d(TAG, "Loading DASH stream: " + streamUrl);
            } else {
                // Progressive stream
                mediaSource = new DefaultMediaSourceFactory(this)
                    .createMediaSource(MediaItem.fromUri(streamUri));
                
                Log.d(TAG, "Loading progressive stream: " + streamUrl);
            }
            
            // Prepare and play
            player.setMediaSource(mediaSource);
            player.prepare();
            player.setPlayWhenReady(true);
            
        } catch (Exception e) {
            Log.e(TAG, "Error playing stream", e);
            showError("Error: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        Log.e(TAG, message);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
