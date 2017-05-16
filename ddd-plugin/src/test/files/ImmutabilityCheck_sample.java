@de.smartsquare.ddd.annotations.DDDValueObject
class obj1 {
    int getKey() {return 0;}
    void setKey(int key) {} // Noncompliant
}

@de.smartsquare.ddd.annotations.DDDValueObject
class NonFinalValueObject {
    private int bla; // Noncompliant

    NonFinalValueObject(int bla) {
        this.bla = bla;
    }

    public getBla() {
        return bla;
    }
}
