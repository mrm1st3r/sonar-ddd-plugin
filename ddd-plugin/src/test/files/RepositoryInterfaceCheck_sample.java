@de.smartsquare.ddd.annotations.DDDRepository
interface SampleRepository {

}

class SampleRepositorySql implements SampleRepository {

}

@de.smartsquare.ddd.annotations.DDDRepository
class Sample2Repository { // Noncompliant

}
