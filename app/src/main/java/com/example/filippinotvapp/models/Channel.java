package com.example.filippinotvapp.models;

public class Channel {
    private String id;
    private String name;
    private String streamUrl;
    private String logoUrl;
    private String group;

    public Channel(String id, String name, String streamUrl, String logoUrl, String group) {
        this.id = id;
        this.name = name;
        this.streamUrl = streamUrl;
        this.logoUrl = logoUrl;
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", streamUrl='" + streamUrl + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
