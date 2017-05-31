package dependencyTest.model;

import com.google.common.collect.ImmutableList;
import dependencyTest.model.OtherEntity;

@de.smartsquare.ddd.annotations.DDDEntity
class TestEntity {

    public void doStuff(ImmutableList list) {}

    public void doOtherStuff(OtherEntity e) {}
}
