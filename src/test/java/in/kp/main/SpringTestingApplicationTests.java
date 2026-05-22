package in.kp.main;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

//@SpringBootTest
@Slf4j
@DisplayName("TESTS")
class SpringTestingApplicationTests {

    //    @BeforeAll
//    static void beforeAllTests() {
//        log.info("INITIATING ALL TESTS ");
//        System.out.println();
//    }
//
//    @AfterAll
//    static void afterAllTests() {
//        log.info("SHUTTING DOWN ALL TESTS");
//    }
//
//    @BeforeEach
//    void beforeEachTest() {
//        log.info("STARING TEST......");
//    }
//
//    @AfterEach
//    void afterEachTest() {
//        log.info("ENDING TEST...");
//        System.out.println();
//    }
//
    @Test
//	@Disabled
    @DisplayName("my test 1")
    void test1() {
        log.info("this is test 1");
    }

    @Test
//    @Disabled
    @DisplayName("my test 2")
    void test2() {
        log.info("this is test 2");
    }

    int addTwoNumber(int a, int b) {
        return a + b;
    }

    @Test
    @DisplayName("assert int")
    void test3() {
        int a = 5;
        int b = 6;
        int add = addTwoNumber(a, b);

        //this Assertion is part of Junit.
//        Assertions.assertEquals(a + b, add);

        // this is a part of assertj library.
        Assertions.assertThat(add).isEqualTo(a + b);

    }

    @Test
    @DisplayName("assert string")
    void test4() {
        assertThat("karan soni").isEqualTo("karan soni").startsWith("kar").endsWith("oni").hasSizeGreaterThanOrEqualTo(7);
    }

    @Test
    @DisplayName("assert boolean")
    void test5() {
        assertThat(true).isEqualTo(true);
        assertThat(false).isFalse();
        System.out.println("test successful");
    }

    double division(int a, int b) {
        try {
            return a / b;
        } catch (ArithmeticException e) {
            log.error(e.getMessage());
            throw new ArithmeticException("CAN NOT DIVIDE BY ZERO");
        }
    }

    @Test
    void test6() {
        int a = 5;
        int b = 0;
        assertThatThrownBy(() -> division(a, b)).isInstanceOf(ArithmeticException.class).hasMessage("CAN NOT DIVIDE BY ZERO");
    }
}


/*
=> @Test : Marks a method as a test method. JUnit will execute this method when running tests.
=> @DisplayName: Sets a custom display name for the test class or test method. This name is used in test reports and IDEs.
=> @Disabled: Disables a test class or test method. Disabled tests are not executed.
=> @BeforeEach: Indicates that the annotated method should be executed before each test method. These can be used to reset each test case conditions.
=> @AfterEach: Indicates that the annotated method should be executed after each test method.
=> @BeforeAll: Indicates that the annotated method should be executed once before all test methods in the class. The method must be static. (Executed once)
=> @AfterAll: Indicates that the annotated method should be executed once after all test methods in the class. The method must be static. (Executed once)
*/