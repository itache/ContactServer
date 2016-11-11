package me.itache.repository;

import me.itache.entity.Contact;
import me.itache.filter.Cursor;
import me.itache.filter.ColumnFilter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ContactRepositoryTest {
    @Autowired
    private ContactRepository contactRepository;

    @Test
    public void shouldFindContactsByFilterInCursorBounds() {
        Cursor cursor = Cursor.createForward(0, Optional.of(10));
        Set<Contact>  contacts = contactRepository.findAll(new ColumnFilter<String>() {
                    @Override
                    public boolean isPassed(String value) {
                        return value.endsWith("b");
                    }

                    @Override
                    public String getColumn() {
                        return "name";
                    }
                },cursor);

        Assert.assertTrue(contacts.size() < 10);
        Assert.assertTrue(cursor.getMaxId() == 50);
        contacts.stream().forEach((c) -> Assert.assertTrue(c.getName().endsWith("b")));
    }
}
