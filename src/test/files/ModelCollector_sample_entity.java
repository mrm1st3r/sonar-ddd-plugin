// Annotation based

@de.smartsquare.ddd.annotations.DDDEntity
class AnnotatedEntity {
}

// Interface based

interface EntityInterface {}
class EntityWithInterface implements EntityInterface {
}

// Parent class based

abstract class AbstractEntity {}
class EntityWithAbstractParent extends AbstractEntity {
}

// Name pattern based

class ENTNamedEntity {
}

// Unmarked class, should not be collected

class UnmarkedEntity {
}
