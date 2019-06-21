package com.register.farmerregistration.local.managers;

import com.register.farmerregistration.local.annotation.EntityAnnotation;
import com.register.farmerregistration.local.entities.Contact;
import com.register.farmerregistration.local.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
* Generated by Spring Data Generator on 22/06/2019
*/
@Component
@EntityAnnotation(entityClass = Contact.class)

public class ContactManager extends BaseManager<Contact, Long, ContactRepository> {

	private ContactRepository contactRepository;

	@Autowired
	public ContactManager(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}

}