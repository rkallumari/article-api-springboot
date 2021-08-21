package com.medibank.codingchallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medibank.codingchallenge.model.Cache;

public interface CacheRepository extends JpaRepository<Cache, Long> {

}
