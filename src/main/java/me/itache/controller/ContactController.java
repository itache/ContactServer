package me.itache.controller;

import me.itache.entity.Contact;
import me.itache.entity.ContactPage;
import me.itache.filter.Cursor;
import me.itache.filter.RegexpExcludingFilter;
import me.itache.repository.ContactRepository;
import me.itache.util.QueryParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/hello/contacts")
public class ContactController {

    @Autowired
    private ContactRepository repository;

    @RequestMapping(method = RequestMethod.GET, params = "after")
    @ResponseBody
    public ContactPage getContactsAfter(@RequestParam String nameFilter,
                                        @RequestParam Long after,
                                        @RequestParam(required = false) Integer size) {
        Cursor cursor = Cursor.createForward(after, Optional.ofNullable(size));
        return getContactPage(nameFilter, cursor);
    }

    @RequestMapping(method = RequestMethod.GET, params = "before")
    @ResponseBody
    public ContactPage getContactsBefore(@RequestParam String nameFilter,
                                         @RequestParam Long before,
                                         @RequestParam(required = false) Integer size) {
        Cursor cursor = Cursor.createBackward(before, Optional.ofNullable(size));
        return getContactPage(nameFilter, cursor);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ContactPage getContacts(@RequestParam String nameFilter,
                                   @RequestParam(required = false) Integer size) {
        Cursor cursor = Cursor.createForward(0, Optional.ofNullable(size));
        return getContactPage(nameFilter, cursor);
    }

    private ContactPage getContactPage(String nameFilter, Cursor cursor) {
        RegexpExcludingFilter filter = new RegexpExcludingFilter("name", nameFilter);
        Set<Contact> contacts = repository.findAll(filter, cursor);
        return new ContactPage(contacts, cursor, new QueryParameter("nameFilter", nameFilter));
    }
}
