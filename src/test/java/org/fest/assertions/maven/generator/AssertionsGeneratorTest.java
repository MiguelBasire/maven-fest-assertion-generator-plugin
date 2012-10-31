package org.fest.assertions.maven.generator;

import org.fest.assertions.generator.BaseAssertionGenerator;
import org.fest.assertions.generator.Template;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AssertionsGeneratorTest {

  private BaseAssertionGenerator mockGenerator;
  private AssertionsGenerator sut;

  @Before
  public void stubGenerator() throws IOException {
    mockGenerator = mock(BaseAssertionGenerator.class);

    doThrow(new AssertionError("setAssertionClassTemplate should not be called")).when(mockGenerator).setAssertionClassTemplate(any(Template.class));
    doThrow(new AssertionError("setHasAssertionTemplate should not be called")).when(mockGenerator).setHasAssertionTemplate(any(Template.class));
    doThrow(new AssertionError("setHasElementsAssertionForArrayTemplate should not be called")).when(mockGenerator).setHasElementsAssertionForArrayTemplate(any(Template.class));
    doThrow(new AssertionError("setHasElementsAssertionForIterableTemplate should not be called")).when(mockGenerator).setHasElementsAssertionForIterableTemplate(any(Template.class));
    doThrow(new AssertionError("setIsAssertionTemplate should not be called")).when(mockGenerator).setIsAssertionTemplate(any(Template.class));

    sut = new AssertionsGenerator(null);
    sut.setBaseAssertionGenerator(mockGenerator);
  }

  @Test
  public void should_register_only_custom_assertions_template() throws Exception {

    doAnswer(RETURNS_DEFAULTS).when(mockGenerator).setAssertionClassTemplate(any(Template.class));

    sut.registerAssertionTemplate("CUSTOM", testCustomAssertionTemplate());

    verify(mockGenerator).setAssertionClassTemplate(any(Template.class));
  }

  @Test
  public void should_register_only_is_assertions_template() throws Exception {

    doAnswer(RETURNS_DEFAULTS).when(mockGenerator).setIsAssertionTemplate(any(Template.class));
    sut.registerAssertionTemplate("IS", testCustomAssertionTemplate());

    verify(mockGenerator).setIsAssertionTemplate(any(Template.class));
  }

  @Test
  public void should_register_only_has_assertions_template() throws Exception {

    doAnswer(RETURNS_DEFAULTS).when(mockGenerator).setHasAssertionTemplate(any(Template.class));
    sut.registerAssertionTemplate("HAS", testCustomAssertionTemplate());

    verify(mockGenerator).setHasAssertionTemplate(any(Template.class));
  }

  @Test
  public void should_register_only_has_on_iterable_assertions_template() throws Exception {

    doAnswer(RETURNS_DEFAULTS).when(mockGenerator).setHasElementsAssertionForIterableTemplate(any(Template.class));
    sut.registerAssertionTemplate("HAS_FOR_ITERABLE", testCustomAssertionTemplate());

    verify(mockGenerator).setHasElementsAssertionForIterableTemplate(any(Template.class));
  }

  @Test
  public void should_register_only_has_on_array_assertions_template() throws Exception {

    doAnswer(RETURNS_DEFAULTS).when(mockGenerator).setHasElementsAssertionForArrayTemplate(any(Template.class));
    sut.registerAssertionTemplate("HAS_FOR_ARRAY", testCustomAssertionTemplate());

    verify(mockGenerator).setHasElementsAssertionForArrayTemplate(any(Template.class));
  }

  @Test
  public void should_throw_runtime_exception_when_type_is_unknown(){

    try{
      sut.registerAssertionTemplate("unknown_type", testCustomAssertionTemplate());
     fail("Unkown type should trigger a runtime exception");
    }catch(RuntimeException e){
      assertThat(e).hasMessageStartingWith("No enum constant org.fest.assertions.generator.Template.Type");
    }

  }



  private URL testCustomAssertionTemplate() {
    return this.getClass().getResource("/somewhere/customTemplate.txt");
  }
}

