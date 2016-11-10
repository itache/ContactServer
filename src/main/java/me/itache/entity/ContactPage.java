package me.itache.entity;

import me.itache.filter.Cursor;
import me.itache.util.LinkBuilder;
import me.itache.util.QueryParameter;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a sub-list of list of contacts.
 * Allows gain information about adjacent sub-lists.
 */
public class ContactPage {
    private Set<Contact> contacts;
    private Map<String, String> links = new HashMap<>();

    public ContactPage(Set<Contact> contacts, Cursor cursor, QueryParameter... parameters) {
        this.contacts = contacts;
        LinkBuilder linkBuilder = new LinkBuilder(createBuilder());
        links = linkBuilder.build(cursor, parameters);
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    private UriComponentsBuilder createBuilder() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        return ServletUriComponentsBuilder.fromHttpUrl(servletRequest.getRequestURL().toString());
    }
}
