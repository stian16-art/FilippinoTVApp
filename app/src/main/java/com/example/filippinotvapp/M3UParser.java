package com.example.filippinotvapp;

import com.example.filippinotvapp.models.Channel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class M3UParser {

    /**
     * Parse M3U playlist content and extract channels
     * @param content M3U file content
     * @return List of Channel objects
     */
    public static List<Channel> parseM3U(String content) {
        List<Channel> channels = new ArrayList<>();
        String[] lines = content.split("\n");
        
        Channel currentChannel = null;
        
        for (String line : lines) {
            line = line.trim();
            
            // Skip empty lines and header
            if (line.isEmpty() || line.startsWith("#EXTM3U")) {
                continue;
            }
            
            // Parse EXTINF line
            if (line.startsWith("#EXTINF:")) {
                currentChannel = parseExtinf(line);
            } 
            // Next line should be the URL
            else if (currentChannel != null && !line.startsWith("#") && !line.isEmpty()) {
                currentChannel.setStreamUrl(line);
                channels.add(currentChannel);
                currentChannel = null;
            }
        }
        
        return channels;
    }
    
    /**
     * Parse EXTINF line to extract channel metadata
     * @param extinf EXTINF line
     * @return Channel object with metadata
     */
    private static Channel parseExtinf(String extinf) {
        String name = "Unknown";
        String logoUrl = "";
        String group = "Local Channels";
        String id = String.valueOf(System.currentTimeMillis());
        
        // Extract tvg-name
        Pattern namePattern = Pattern.compile("tvg-name=\"([^\"]+)\"");
        Matcher nameMatcher = namePattern.matcher(extinf);
        if (nameMatcher.find()) {
            name = nameMatcher.group(1);
        }
        
        // Extract tvg-logo
        Pattern logoPattern = Pattern.compile("tvg-logo=\"([^\"]+)\"");
        Matcher logoMatcher = logoPattern.matcher(extinf);
        if (logoMatcher.find()) {
            logoUrl = logoMatcher.group(1);
        }
        
        // Extract group-title
        Pattern groupPattern = Pattern.compile("group-title=\"([^\"]+)\"");
        Matcher groupMatcher = groupPattern.matcher(extinf);
        if (groupMatcher.find()) {
            group = groupMatcher.group(1);
        }
        
        // Extract channel name from end of line (after last comma)
        int lastCommaIndex = extinf.lastIndexOf(",");
        if (lastCommaIndex != -1 && lastCommaIndex < extinf.length() - 1) {
            name = extinf.substring(lastCommaIndex + 1).trim();
        }
        
        return new Channel(id, name, "", logoUrl, group);
    }
}
