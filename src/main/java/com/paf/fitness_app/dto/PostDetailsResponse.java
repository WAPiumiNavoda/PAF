package com.paf.fitness_app.dto;

import org.springframework.core.io.Resource;

public class PostDetailsResponse {
    private Long postId;
    private String title;
    private String description;
    private Resource postImage;

    // Getters and setters

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Resource getPostImage() {
        return postImage;
    }

    public void setPostImage(Resource postImage) {
        this.postImage = postImage;
    }
}
