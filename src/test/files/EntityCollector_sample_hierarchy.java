interface EntityInterface {}

abstract class AbstractEntity {}

class EntityWithInterface implements EntityInterface {

    public int getId() {return 0;}
}

class EntityWithAbstractParent extends AbstractEntity {

    public int getFoo() {return 0;}
}
class UnmarkedEntity {

    public int getFoo() {return 0;}
}
