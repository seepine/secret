package com.seepine.secret.quarkus.test;

import com.seepine.secret.quarkus.runtime.properties.AuthPropertiesImpl;
import io.quarkus.test.QuarkusUnitTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.inject.Inject;

public class SecretQuarkusTest {

  // Start unit test with your extension loaded
  @RegisterExtension
  static final QuarkusUnitTest unitTest =
      new QuarkusUnitTest().setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));

  @Inject AuthPropertiesImpl authProperties;

  //  @Test
  //  public void writeYourOwnUnitTest() {
  //     Write your unit tests here - see the testing extension guide
  // https://quarkus.io/guides/writing-extensions#testing-extensions for more information
  //    Assertions.assertTrue(true, "Add some assertions to " + getClass().getName());
  //  }
  @Test
  public void testProperties() {
    System.out.println(authProperties);
  }
}
