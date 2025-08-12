package com.pecunia.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Annonation SpringBoot.
 *
 * @author torigon
 * @version $Id: $Id
 */
@SpringBootApplication
public class ApiApplication {

  /**
   * main.
   *
   * @param args an array of {@link java.lang.String} objects
   */
  public static void main(final String[] args) {
    SpringApplication.run(ApiApplication.class, args);
  }
}
