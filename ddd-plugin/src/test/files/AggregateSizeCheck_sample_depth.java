@de.smartsquare.ddd.annotations.AggregateRoot
class Root {
    private Entity1 secondLevel;
}

@de.smartsquare.ddd.annotations.Entity
class Entity1 {
    private Entity2 thirdLevel;
}

@de.smartsquare.ddd.annotations.Entity
class Entity2 {} // Noncompliant
