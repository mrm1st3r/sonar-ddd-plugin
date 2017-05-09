// Annotation based

@de.smartsquare.ddd.annotations.DDDValueObject
class AnnotatedValueObject {
}

// Interface based

interface ValueObjectInterface {}
class ValueObjectWithInterface implements ValueObjectInterface {
}

// Parent class based

abstract class AbstractValueObject {}
class ValueObjectWithAbstractParent extends AbstractValueObject {
}

// Name pattern based

class VONamedValueObject {
}

// Unmarked class, should not be collected

class UnmarkedValueObject {
}
