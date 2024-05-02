package com.paf.fitness_app.service;


import com.paf.fitness_app.dto.PostDTO;
import com.paf.fitness_app.entity.PostNewEntity;

import java.io.IOException;
import java.util.List;

public interface PostService {


    void delete(Long id);
    PostNewEntity saveData(PostNewEntity postEntity);

    List<PostNewEntity> getPostsByUserEmail(String userEmail);

    PostNewEntity findById(Long id);

    public PostNewEntity getPostById(Long id);
}
