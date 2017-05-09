// Annotation based

@de.smartsquare.ddd.annotations.DDDService
class AnnotatedService {
}

// Interface based

interface ServiceInterface {}
class ServiceWithInterface implements ServiceInterface {
}

// Parent class based

abstract class AbstractService {}
class ServiceWithAbstractParent extends AbstractService {
}

// Name pattern based

class SERVNamedService {
}

// Unmarked class, should not be collected

class UnmarkedService {
}
