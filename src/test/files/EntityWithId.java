@de.smartsquare.ddd.annotations.DDDEntity
class SampleEntity {

    public int getId() {return 0;}
}

@de.smartsquare.ddd.annotations.DDDEntity
class SampleEntity2 { // Noncompliant

    public int getFoo() {return 0;}
}
