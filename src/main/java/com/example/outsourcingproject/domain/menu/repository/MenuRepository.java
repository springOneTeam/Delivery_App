package com.example.outsourcingproject.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.outsourcingproject.domain.menu.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

}
