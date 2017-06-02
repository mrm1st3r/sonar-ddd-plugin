@de.smartsquare.ddd.annotations.DDDEntity
class BeanEntity { // Noncompliant
    private int key;

    BeanEntity(int key) {this.key = key;}

    int getKey() {
        return key;
    }
    void setKey(int key) {
        this.key = key;
    }
}

@de.smartsquare.dd.annotations.DDDEntity
class ExtendedBeanEntity {
    private int foo;

    int getFoo() {return foo;}
    void setFoo(int foo) {this.foo = foo;}

    int blub() {return 0xb10b;}
}
