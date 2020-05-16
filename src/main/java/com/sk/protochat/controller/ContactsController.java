package com.sk.protochat.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sk.protochat.model.Contact;
import com.sk.protochat.service.ContactsService;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

	@Autowired
	ContactsService contactsService;
	
	@GetMapping
	public List<Contact> getContacts(Principal principal) {
		
		String currentUser = principal.getName();
		
		
		return contactsService.selectAll(currentUser);
	}

	
}
