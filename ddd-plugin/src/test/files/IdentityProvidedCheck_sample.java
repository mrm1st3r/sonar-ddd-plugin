@de.smartsquare.ddd.annotations.DDDEntity
class SampleEntity {

    public int getId() {return 0;}
}

@de.smartsquare.ddd.annotations.DDDEntity
class SampleEntity2 { // Noncompliant

    public int getFoo() {return 0;}
}

class SampleEntity3 {

    public int getFoo() {return 0;}
}


class BaseEntity {
    public int getId() {return 0;}
}

@de.smartsquare.ddd.annotations.DDDEntity
class ExtendedEntity extends BaseEntity {
    public int getFoo() {return 0xf00;}
}
