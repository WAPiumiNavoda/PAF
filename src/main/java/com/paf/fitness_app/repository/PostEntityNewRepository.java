package com.paf.fitness_app.repository;


import com.paf.fitness_app.entity.PostNewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostEntityNewRepository extends JpaRepository<PostNewEntity, Long> {

    List<PostNewEntity> findByUserEmail(String userEmail);
}
