package fr.rossi.biglistdownload.dataload;

import java.util.ArrayList;
import java.util.Random;

import fr.rossi.biglistdownload.Person;
import org.apache.commons.lang3.RandomStringUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class Dataload {

    private static final int NB_PERSONS = 100_000;
    private static final Random RANDOM = new Random();

    @Transactional
    public void init() {
        var count = Person.count();
        if (count >= NB_PERSONS) return;

        var newPersons = new ArrayList<>();

        for (var nb = count; nb < NB_PERSONS; nb++) {
            var person = new Person();
            person.firstname = RandomStringUtils.randomAlphabetic(10);
            person.lastname = RandomStringUtils.randomAlphabetic(10);
            person.age = RANDOM.nextInt(100);
            newPersons.add(person);
        }

        Person.persist(newPersons);
    }
}
