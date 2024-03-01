package fr.rossi.biglistdownload;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class Dataload {

    private static final int NB_PERSONS = 100_000;
    
    @Transactional
    public void init() {
        var count = Person.count();
        if (count >= NB_PERSONS) return;

        var newPersons = new ArrayList<>();
        var random = new Random();

        
        for (var nb = count; nb < NB_PERSONS; nb++) {
            var person = new Person();
            person.firstname = RandomStringUtils.random(10);
            person.lastname = RandomStringUtils.random(10);
            person.age =random.nextInt(100);
            newPersons.add(person);
        }

        Person.persist(newPersons);
    }

}
