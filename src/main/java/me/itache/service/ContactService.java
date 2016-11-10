package me.itache.service;

import me.itache.entity.Contact;
import me.itache.filter.Cursor;
import me.itache.filter.ColumnFilter;
import me.itache.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ContactService {
    @Autowired
    private ContactRepository repository;

    public Set<Contact> get(ColumnFilter filter, Cursor cursor) {
        return repository.findAll(filter, cursor);
    }
}
