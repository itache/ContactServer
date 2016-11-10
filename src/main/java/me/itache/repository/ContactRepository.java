package me.itache.repository;

import me.itache.entity.Contact;
import me.itache.filter.Cursor;
import me.itache.filter.ColumnFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public class ContactRepository {
    private static final int DEFAULT_FETCH_SIZE = 1000;

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
        jdbcTemplate.setFetchSize(DEFAULT_FETCH_SIZE);
        Set<Contact> contacts = new TreeSet<>(
                (o1, o2) -> (o1.getId() == o2.getId()) ? 0 : (o1.getId() > o2.getId()) ? 1 : -1);
        long lastViewedId = cursor.getLowerBound();
        setMaxId(cursor);
        while(hasMoreRecords(lastViewedId, cursor)) {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(getQuery(cursor), lastViewedId);
            while (rowSet.next()) {
                if (filter.isPassed(rowSet.getString(filter.getColumn()))) {
                    contacts.add(ContactMapper.mapRow(rowSet));
                }
                if (contacts.size() >= cursor.getSize()) {
                    cursor.setUpperBound(rowSet.getLong("id"));
                    return contacts;
                }
                lastViewedId = rowSet.getLong("id");
            }
        }
        return contacts;
    }

    private boolean hasMoreRecords(long lastViewedId, Cursor cursor) {
        if(cursor.getDirection() == Cursor.Direction.FORWARD) {
            return lastViewedId < cursor.getMaxId();
        }
        return lastViewedId > 1;
    }

    private String getQuery(Cursor cursor) {
        if (cursor.getDirection() == Cursor.Direction.FORWARD) {
            return "SELECT * FROM Contact WHERE id > ? ORDER BY id ASC LIMIT 10000";
        }
        return "SELECT * FROM Contact WHERE id < ? ORDER BY id DESC LIMIT 10000";
    }

    private void setMaxId(Cursor cursor) {
        cursor.setMaxId(jdbcTemplate.queryForObject("SELECT MAX(id) FROM Contact", Long.class));
    }
}
