/*
 * Aggregate with multiple roots.
 */

@de.smartsquare.ddd.annotations.AggregateRoot
class Root1 {
    private Child1 child;
    private Child2 child2;
}

@de.smartsquare.ddd.annotations.AggregateRoot
class Root2 {
    private Child1 child;
    private Child2 child2;
}

@de.smartsquare.ddd.annotations.Entity
class Child1 {} // Noncompliant

@de.smartsquare.ddd.annotations.ValueObject
class Child2 {}
