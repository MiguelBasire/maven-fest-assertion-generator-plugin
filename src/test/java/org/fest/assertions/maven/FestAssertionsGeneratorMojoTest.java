package org.fest.assertions.maven;

import org.apache.maven.project.MavenProject;
import org.fest.assertions.maven.testdata1.Address;
import org.fest.assertions.maven.testdata2.Employee;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.util.Arrays.array;
import static org.fest.util.Lists.newArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FestAssertionsGeneratorMojoTest {

  private static final String TARGET_DIR = "./target/";

  private FestAssertionsGeneratorMojo festAssertionsGeneratorMojo;
  private MavenProject mavenProject;

  @Before
  public void setUp() throws Exception {
    mavenProject = mock(MavenProject.class);
    festAssertionsGeneratorMojo = new FestAssertionsGeneratorMojo();
    festAssertionsGeneratorMojo.project = mavenProject;
    festAssertionsGeneratorMojo.packages = array("org.fest.assertions.maven.testdata1",
        "org.fest.assertions.maven.testdata2");
    festAssertionsGeneratorMojo.targetDir = TARGET_DIR;
  }

  @Test
  public void testExecute() throws Exception {
    List<String> classes = newArrayList(Employee.class.getName(), Address.class.getName());
    when(mavenProject.getRuntimeClasspathElements()).thenReturn(classes);

    festAssertionsGeneratorMojo.execute();

    // check that expected assertions file exist (we don't check the content we suppose the generator works).
    assertThat(assertionsFileFor(Employee.class)).exists();
    assertThat(assertionsFileFor(Address.class)).exists();

  }

  @Test
  public void testExecute_with_a_custom_template_configured() throws Exception {
    List<String> classes = newArrayList(Employee.class.getName(), Address.class.getName());
    when(mavenProject.getRuntimeClasspathElements()).thenReturn(classes);

    festAssertionsGeneratorMojo.templates = new HashMap<String,URL>(){{
      put("CUSTOM",this.getClass().getResource("/somewhere/customTemplate.txt"));
    }};

    festAssertionsGeneratorMojo.execute();

    assertThat(assertionsFileFor(Employee.class)).exists();

    File expectedAssertionFileContent = new File(this.getClass().getResource("/expectedAssertions/EmployeeAssert.java.expected").getFile());
    assertThat(assertionsFileFor(Employee.class)).hasContentEqualTo(expectedAssertionFileContent);

  }

  private static File assertionsFileFor(Class<?> clazz) {
    return new File(TARGET_DIR + clazz.getPackage().getName().replace('.', File.separatorChar) + File.separator
        + clazz.getSimpleName() + "Assert.java");
  }

}
