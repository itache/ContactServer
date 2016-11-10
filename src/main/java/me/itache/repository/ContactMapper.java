package me.itache.repository;

import me.itache.entity.Contact;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class ContactMapper {

    /**
     * Create new contact object from ResultSet row.
     *
     * @param resultSet
     * @return newly created contact
     */
    public static Contact mapRow(SqlRowSet resultSet) {
        return new Contact(resultSet.getLong("id"), resultSet.getString("name"));
    }
}
