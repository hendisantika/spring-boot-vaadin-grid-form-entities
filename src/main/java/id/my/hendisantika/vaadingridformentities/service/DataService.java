package id.my.hendisantika.vaadingridformentities.service;

import id.my.hendisantika.vaadingridformentities.entity.Address;
import id.my.hendisantika.vaadingridformentities.entity.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-vaadin-grid-form-entities
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 20/11/24
 * Time: 09.17
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DataService {

    private final List<Person> persons = List.of(
            new Person(1L, "John", "Doe", "john.doe@example.com", true,
                    LocalDate.of(1971, 3, 4)),
            new Person(2L, "Jane", "Smith", "jane.smith@example.com", false
                    , LocalDate.of(1975, 6, 12)),
            new Person(3L, "Bob", "Johnson", "bob.johnson@example.com", true,
                    LocalDate.of(1990, 1, 24)),
            new Person(4L, "Alice", "Williams", "alice.williams@example.com", true,
                    LocalDate.of(1985, 11, 23))
    );

    private final List<Address> addresses = List.of(
            new Address(1L, "123 Main St", "New York", "USA", "10001"),
            new Address(2L, "456 Elm St", "Los Angeles", "USA", "90001"),
            new Address(3L, "789 Oak St", "Chicago", "USA", "60601"),
            new Address(4L, "101 Park Ave", "Boston", "USA", "02111")
    );

    public List<Person> findAllPersons() {
        return persons;
    }
}
