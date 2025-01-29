package com.url.shortner.repository;

import com.url.shortner.dtos.UrlMappingDTO;
import com.url.shortner.models.UrlMapping;
import com.url.shortner.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    List<UrlMapping> findByUser(User user);
}
