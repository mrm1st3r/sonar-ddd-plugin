@de.smartsquare.ddd.annotations.AggregateRoot
class Root { // Noncompliant
    private Entity1 second;
    private Entity2 third;
    private Entity3 fourth;
}

@de.smartsquare.ddd.annotations.DDDEntity
class Entity1 {
    private Entity2 thirdLevel;
}

@de.smartsquare.ddd.annotations.DDDEntity
class Entity2 {}

@de.smartsquare.ddd.annotations.DDDEntity
class Entity3 {}
