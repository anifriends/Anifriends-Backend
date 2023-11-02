package com.clova.anifriends.domain.animal.repository;

import com.clova.anifriends.domain.animal.AnimalImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalImageRepository extends JpaRepository<AnimalImage, Long> {

    List<AnimalImage> findByImageUrlIn(List<String> imageUrls);
}
