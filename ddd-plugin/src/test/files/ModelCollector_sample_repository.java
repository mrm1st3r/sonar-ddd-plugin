// Annotation based

@de.smartsquare.ddd.annotations.DDDRepository
class AnnotatedRepository {
}

// Interface based

interface RepositoryInterface {}
class RepositoryWithInterface implements RepositoryInterface {
}

// Parent class based

abstract class AbstractRepository {}
class RepositoryWithAbstractParent extends AbstractRepository {
}

// Name pattern based

class REPONamedRepository {
}

// Unmarked class, should not be collected

class UnmarkedRepository {
}
