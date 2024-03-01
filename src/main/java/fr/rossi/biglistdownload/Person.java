package fr.rossi.biglistdownload;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

public class Person extends PanacheEntity {
    public String firstname;
    public String lastname;
    public int age;
}
