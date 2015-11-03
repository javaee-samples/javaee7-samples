package org.javaee7.jpa.converter;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class EmployeeRepositoryTest {

    @Deployment
    public static Archive<?> createDeployment() {
        final File[] assertJ = Maven.resolver().loadPomFromFile("pom.xml")
            .resolve("org.assertj:assertj-core")
            .withTransitivity()
            .asFile();

        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "employee-card-converter-sample.jar")
            .addPackage(Employee.class.getPackage())
            .addAsManifestResource("test-persistence.xml", "persistence.xml")
            .merge(metaInfFolder(), "/META-INF", Filters.include(".*\\.sql"));
        mergeDependencies(archive, assertJ);

        return archive;

    }

    @Inject
    private EmployeeRepository repository;

    @Test
    public void should_return_all_employee_records() throws Exception {
        // when
        final List<Employee> employees = repository.all();

        // then
        assertThat(employees).hasSize(6)
            .contains(employee("Leonard", "11-22-33-44"), employee("Sheldon", "22-33-44-55"),
                employee("Penny", "33-44-55-66"), employee("Raj", "44-55-66-77"),
                employee("Howard", "55-66-77-88"), employee("Bernadette", "66-77-88-99"));
    }

    // -- Test utility methods

    private static Employee employee(String name, String creditCardNumber) {
        final CreditCard creditCard = new CreditCard(creditCardNumber);
        return new Employee(name, creditCard);
    }

    private static void mergeDependencies(JavaArchive archive, File... dependencies) {
        for (File file : dependencies) {
            archive.merge(ShrinkWrap.createFromZipFile(JavaArchive.class, file));
        }
    }

    private static GenericArchive metaInfFolder() {
        return ShrinkWrap.create(GenericArchive.class)
            .as(ExplodedImporter.class)
            .importDirectory("src/main/resources/META-INF")
            .as(GenericArchive.class);
    }
}
