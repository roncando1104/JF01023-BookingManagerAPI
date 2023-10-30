package com.jfcm.manda.bookingmanagerapi;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootTest
class BookingManagerAPIApplicationTests {

  @Test
  void testMainApplication() {
    try(MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)){
      mocked.when(() -> { SpringApplication.run(BookingManagerAPIApplication.class,
              "foo", "bar"); })
          .thenReturn(Mockito.mock(ConfigurableApplicationContext.class));

      BookingManagerAPIApplication.main(new String[] { "foo", "bar"});
      mocked.verify(() -> { SpringApplication.run(BookingManagerAPIApplication.class,
          "foo", "bar"); });
    }
  }

}
