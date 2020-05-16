package com.sk.protochat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sk.protochat.dao.ContactsDao;
import com.sk.protochat.model.Contact;

@Service
public class ContactsService {

	@Autowired
	private ContactsDao contactsDao;
	
	public List<Contact> selectAll(String currentUser){
		return contactsDao.selectAll(currentUser);
	}
	
}
