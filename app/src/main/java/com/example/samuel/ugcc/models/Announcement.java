package com.example.samuel.ugcc.models;

/**
 * Created by Samuel on 06-Apr-18.
 */

public class Announcement {
    Long id;
    private String title;
    private String content;
    private String image;
    private String date_created;

    public Announcement() {
    }

    public Announcement(Long id, String title, String content, String image, String date_created) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.date_created = date_created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
