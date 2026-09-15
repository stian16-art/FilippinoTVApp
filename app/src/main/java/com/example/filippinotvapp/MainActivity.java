package com.example.filippinotvapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filippinotvapp.models.Channel;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ChannelAdapter.ChannelClickListener {
    
    private static final String TAG = "MainActivity";
    
    private RecyclerView channelsRecyclerView;
    private ChannelAdapter channelAdapter;
    private ProgressBar loadingProgress;
    private ChannelRepository channelRepository;
    private List<Channel> channels;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        loadChannels();
    }
    
    private void initializeViews() {
        channelsRecyclerView = findViewById(R.id.channels_recycler_view);
        loadingProgress = findViewById(R.id.loading_progress);
        
        // Setup RecyclerView
        channelsRecyclerView.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
    }
    
    private void loadChannels() {
        loadingProgress.setVisibility(View.VISIBLE);
        
        // Run on background thread
        new Thread(() -> {
            try {
                channelRepository = new ChannelRepository();
                channels = channelRepository.getChannels();
                
                runOnUiThread(() -> {
                    if (channels != null && !channels.isEmpty()) {
                        setupChannelAdapter();
                        loadingProgress.setVisibility(View.GONE);
                        Log.d(TAG, "Channels loaded: " + channels.size());
                    } else {
                        showError("Walang channels na nahanap");
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error loading channels", e);
                runOnUiThread(() -> showError("Error: " + e.getMessage()));
            }
        }).start();
    }
    
    private void setupChannelAdapter() {
        channelAdapter = new ChannelAdapter(this, channels, this);
        channelsRecyclerView.setAdapter(channelAdapter);
        
        // Select first channel
        if (!channels.isEmpty()) {
            channelAdapter.setSelectedPosition(0);
        }
    }
    
    @Override
    public void onChannelClick(Channel channel) {
        if (channel.getStreamUrl() != null && !channel.getStreamUrl().isEmpty()) {
            Log.d(TAG, "Playing channel: " + channel.getName());
            openPlayer(channel);
        } else {
            Toast.makeText(this, "Stream URL not available", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void openPlayer(Channel channel) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("channel_name", channel.getName());
        intent.putExtra("stream_url", channel.getStreamUrl());
        intent.putExtra("channel_logo", channel.getLogoUrl());
        startActivity(intent);
    }
    
    private void showError(String message) {
        loadingProgress.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.e(TAG, message);
    }
}
