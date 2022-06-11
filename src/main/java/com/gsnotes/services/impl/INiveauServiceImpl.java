package com.gsnotes.services.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Role;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.dao.ICompteDao;
import com.gsnotes.dao.IRoleDao;
import com.gsnotes.dao.IUtilisateurDao;
import com.gsnotes.services.ICompteService;
import com.gsnotes.services.INiveauService;
import com.gsnotes.utils.export.ExcelExporter;

@Service
@Transactional
public class INiveauServiceImpl implements INiveauService {

	

}
