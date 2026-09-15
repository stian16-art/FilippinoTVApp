package com.example.filippinotvapp;

import android.util.Log;
import com.example.filippinotvapp.models.Channel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChannelRepository {
    
    private static final String TAG = "ChannelRepository";
    private static final String IPHTV_M3U_URL = 
        "https://raw.githubusercontent.com/Harleythetech/IPHTV/refs/heads/main/ph.m3u";
    
    private final OkHttpClient httpClient;
    
    public ChannelRepository() {
        this.httpClient = new OkHttpClient();
    }
    
    /**
     * Fetch and parse M3U playlist from URL
     * @return List of Channel objects
     */
    public List<Channel> getChannels() {
        List<Channel> channels = new ArrayList<>();
        
        try {
            String m3uContent = fetchM3U(IPHTV_M3U_URL);
            if (m3uContent != null && !m3uContent.isEmpty()) {
                channels = M3UParser.parseM3U(m3uContent);
                Log.d(TAG, "Loaded " + channels.size() + " channels");
            } else {
                Log.e(TAG, "Failed to fetch M3U content");
                channels = getHardcodedChannels();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading channels", e);
            channels = getHardcodedChannels();
        }
        
        return channels;
    }
    
    /**
     * Fetch M3U content from URL
     * @param url M3U URL
     * @return M3U content as string
     */
    private String fetchM3U(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                Log.e(TAG, "HTTP Error: " + response.code());
                return null;
            }
        }
    }
    
    /**
     * Fallback hardcoded channels if online fetch fails
     * @return List of hardcoded Channel objects
     */
    private List<Channel> getHardcodedChannels() {
        List<Channel> channels = new ArrayList<>();
        
        // Filipino Channels - Fallback
        channels.add(new Channel("1", "ABS-CBN", 
            "https://myxnola-abscbn-ono.amagi.tv/index.m3u8", 
            "https://i.imgur.com/placeholder.png", "Local Channels"));
            
        channels.add(new Channel("2", "GMA", 
            "https://gma-live.example.com/stream.m3u8", 
            "https://i.imgur.com/placeholder.png", "Local Channels"));
            
        channels.add(new Channel("3", "TV5", 
            "https://ott.tv5monde.com/Content/HLS/Live/channel(seasie)/variant.m3u8", 
            "https://i.imgur.com/placeholder.png", "Local Channels"));
            
        channels.add(new Channel("4", "PTV", 
            "https://qp-pldt-live-grp-14-prod.akamaized.net/out/u/cg_ptv4_sd.mpd", 
            "https://i.imgur.com/placeholder.png", "Local Channels"));
            
        channels.add(new Channel("5", "RPTV", 
            "https://qp-pldt-live-grp-10-prod.akamaized.net/out/u/cnn_rptv_prod_hd.mpd", 
            "https://i.imgur.com/placeholder.png", "Local Channels"));
        
        Log.d(TAG, "Loaded hardcoded channels - " + channels.size() + " channels");
        return channels;
    }
}
