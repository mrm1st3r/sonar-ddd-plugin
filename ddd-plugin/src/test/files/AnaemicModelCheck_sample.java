@de.smartsquare.ddd.annotations.DDDEntity
class BeanEntity { // Noncompliant
    private int key;
    int getKey() {
        if (key < 0) {
            return 0;
        } else {
            return key;
        }
    }
    void setKey(int key) {
        if (key > 1) {
            this.key = 1;
        } else {
            this.key = key;
        }
    }
}

@de.smartsquare.ddd.annotations.DDDEntity
class NoncomplexEntity { // Noncompliant

    int foo() {
        return 0xf00;
    }

    void bar(int bar) {
        bar = 0xba9;
    }
}
