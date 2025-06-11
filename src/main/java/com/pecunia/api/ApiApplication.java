package com.pecunia.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Annonation SpringBoot.
 *
 * @author torigon
 * @version $Id: $Id
 */
@SpringBootApplication
public class ApiApplication {

  /**
   * <p>main.</p>
   *
   * @param args an array of {@link java.lang.String} objects
   */
  public static void main(final String[] args) {
    SpringApplication.run(ApiApplication.class, args);
  }

  /**
   * Test endpoint.
   *
   * @return String
   */
  @GetMapping("/hello")
  public final String sayHello() {
    return "Hello Wolrd!";
  }
}
