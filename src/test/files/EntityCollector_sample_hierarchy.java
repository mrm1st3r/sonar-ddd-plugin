interface Entity {}

abstract class AbstractEntity {}

class SampleEntity implements Entity {

    public int getId() {return 0;}
}

class SampleEntity2 extends AbstractEntity {

    public int getFoo() {return 0;}
}
class SampleEntity3 {

    public int getFoo() {return 0;}
}
