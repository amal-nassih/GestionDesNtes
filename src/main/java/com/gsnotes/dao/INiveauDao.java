package com.gsnotes.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsnotes.bo.Niveau;


public interface INiveauDao extends JpaRepository<Niveau, Long> {

}
