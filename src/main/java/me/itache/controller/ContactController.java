package me.itache.controller;

import me.itache.entity.Contact;
import me.itache.entity.ContactPage;
import me.itache.filter.Cursor;
import me.itache.filter.RegexpExcludingFilter;
import me.itache.service.ContactService;
import me.itache.util.QueryParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactService service;

    @Autowired
    private JdbcTemplate template;

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

    @RequestMapping(method = RequestMethod.GET, path = "/fill")
    public void fillDB() {

        for (int i = 1000; i < 1_000_000; i++) {
            template.update("INSERT INTO Contact VALUES(?,?)", i, getRandomString());
        }
    }

    private String getRandomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        StringBuilder buffer = new StringBuilder(targetStringLength);
        int randomLimitedIntUpper = 65 + (int)
                (new Random().nextFloat() * (90 - 65));
        buffer.append((char) randomLimitedIntUpper);
        for (int i = 1; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (new Random().nextFloat() * (rightLimit - leftLimit));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    private ContactPage getContactPage(String nameFilter, Cursor cursor) {
        RegexpExcludingFilter filter = new RegexpExcludingFilter("name", nameFilter);
        Set<Contact> contacts = service.get(filter, cursor);
        return new ContactPage(contacts, cursor, new QueryParameter("nameFilter", nameFilter));
    }
}
