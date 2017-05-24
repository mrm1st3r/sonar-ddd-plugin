@de.smartsquare.ddd.annotations.AggregateRoot
class Root1 {
    private Child1 child1;
    private Child2 child2;
}

@de.smartsquare.ddd.annotations.DDDEntity
class Child1 {}

@de.smartsquare.ddd.annotations.DDDValueObject
class Child2 {}

@de.smartsquare.ddd.annotations.AggregateRoot
class Root2 {
    private Child3 child3;
    private Root1 root1;
}

@de.smartsquare.ddd.annotations.DDDValueObject
class Child3 {}

class IrrelevantClass {}
