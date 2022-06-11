package com.gsnotes.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Element;
import com.gsnotes.bo.Module;
import com.gsnotes.bo.Niveau;

public interface IElementDao extends JpaRepository<Element, Long> {


}
