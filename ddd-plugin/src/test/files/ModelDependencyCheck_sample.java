package dependencyTest.model; // Noncompliant

import dependencyTest.ui.SampleController;

@de.smartsquare.ddd.annotations.DDDEntity
class TestEntity {

    public void doStuff(SampleController controller) {}
}
