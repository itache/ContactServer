package me.itache.repository;

import me.itache.filter.Cursor;
import me.itache.filter.ColumnFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ContactRepositoryTest {
    @Autowired
    private ContactRepository contactRepository;

    @Test
    public void shouldFindContacts() {
        System.out.println(
                contactRepository.findAll(new ColumnFilter() {
                    @Override
                    public boolean isPassed(Object value) {
                        return true;
                    }

                    @Override
                    public String getColumn() {
                        return "name";
                    }
                },new Cursor(0, Cursor.Direction.FORWARD, Optional.of(10))));
    }
}
