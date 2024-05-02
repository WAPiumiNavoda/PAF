package com.paf.fitness_app.service.impl;


import com.paf.fitness_app.config.AuthenticationFacade;
import com.paf.fitness_app.entity.PostNewEntity;
import com.paf.fitness_app.repository.PostEntityNewRepository;
import com.paf.fitness_app.service.PostService;
import lombok.RequiredArgsConstructor;
import  org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private  final PostEntityNewRepository postNewEntityRepository;
    private final AuthenticationFacade authenticationFacade;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(PostServiceImpl.class);


    @Override
    public void delete(Long id) {
        postNewEntityRepository.deleteById(id);
    }

    @Override
    public PostNewEntity saveData(PostNewEntity postEntity) {
        return postNewEntityRepository.save(postEntity);
    }

    @Transactional
    @Override
    public List<PostNewEntity> getPostsByUserEmail(String userEmail) {
        // Implement a method in your repository to retrieve posts by userEmail
        // Assuming you have a method in your repository named findByUserEmail
        return postNewEntityRepository.findByUserEmail(userEmail);
    }

    @Override
    public PostNewEntity findById(Long id) {
        return postNewEntityRepository.findById(id).orElse(null);
    }

    public PostNewEntity getPostById(Long id) {
        Optional<PostNewEntity> findById = postNewEntityRepository.findById(id);
        PostNewEntity postEntity = findById.get();
        return postEntity;
    }


}
