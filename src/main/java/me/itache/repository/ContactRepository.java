package me.itache.repository;

import me.itache.entity.Contact;
import me.itache.filter.Cursor;
import me.itache.filter.ColumnFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public class ContactRepository {
    private static final int DEFAULT_FETCH_SIZE = 1000;
    private Logger logger = LoggerFactory.getLogger(ContactRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Finds contacts that satisfy given filter condition in given cursor bounds.
     *
     * @param filter
     * @param cursor
     * @return sub-list of contacts
     */
    @Transactional(readOnly = true)
    public Set<Contact> findAll(ColumnFilter filter, Cursor cursor) {
        logger.info("Try find contacts with filter " + filter.toString());
        logger.info("Cursor starts from " + cursor.getLowerBound() + ", direction " + cursor.getDirection());
        jdbcTemplate.setFetchSize(DEFAULT_FETCH_SIZE);
        Set<Contact> contacts = getContacts(filter, cursor);
        logger.info("Obtain " + contacts.size() + " contacts, need - " + cursor.getSize());
        logger.info("Last viewed id: " + cursor.getLastViewedId());
        return contacts;
    }

    private Set<Contact> getContacts(ColumnFilter filter, Cursor cursor) {
        Set<Contact> contacts = new TreeSet<>(getContactByIdComparator());
        cursor.setMaxId(getMaxId());
        outer:
        while (hasMoreRecords(cursor)) {
            SqlRowSet rowSet = jdbcTemplate
                    .queryForRowSet(getQuery(cursor), cursor.getLastViewedId());
            while (rowSet.next()) {
                cursor.setLastViewedId(rowSet.getLong("id"));
                if (filter.isPassed(rowSet.getObject(filter.getColumn()))) {
                    contacts.add(ContactMapper.mapRow(rowSet));
                }
                if (contacts.size() >= cursor.getSize()) {
                    cursor.setUpperBound(rowSet.getLong("id"));
                    break outer;
                }
            }
        }
        return contacts;
    }

    private Comparator<Contact> getContactByIdComparator() {
        return (o1, o2) -> (o1.getId() == o2.getId()) ? 0 : (o1.getId() > o2.getId()) ? 1 : -1;
    }

    private boolean hasMoreRecords(Cursor cursor) {
        if (cursor.getDirection() == Cursor.Direction.FORWARD) {
            return cursor.getLastViewedId() < cursor.getMaxId();
        }
        return cursor.getLastViewedId() > 1;
    }

    private String getQuery(Cursor cursor) {
        if (cursor.getDirection() == Cursor.Direction.FORWARD) {
            return "SELECT * FROM Contact WHERE id > ? ORDER BY id ASC LIMIT 10000";
        }
        return "SELECT * FROM Contact WHERE id < ? ORDER BY id DESC LIMIT 10000";
    }

    private long getMaxId() {
        return jdbcTemplate.queryForObject("SELECT MAX(id) FROM Contact", Long.class);
    }
}
