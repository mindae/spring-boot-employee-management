package com.example.hello.bdd;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple BDD-style test using only Apache Commons and JUnit 5.
 * Demonstrates BDD testing without complex Spring context loading.
 */
public class SimpleBddTest {

    private Map<String, Object> testContext;
    private StopWatch stopWatch;

    @BeforeEach
    void setUp() {
        testContext = new HashMap<>();
        stopWatch = StopWatch.createStarted();
    }

    @Test
    public void shouldValidateStringOperations() {
        given("a valid string");
        String testString = "Hello World";
        storeInContext("testString", testString);

        when("string operations are performed");
        String upperCase = testString.toUpperCase();
        String lowerCase = testString.toLowerCase();
        boolean isEmpty = StringUtils.isEmpty(testString);
        boolean isBlank = StringUtils.isBlank(testString);

        then("the operations should work correctly");
        assertThat(upperCase).isEqualTo("HELLO WORLD");
        assertThat(lowerCase).isEqualTo("hello world");
        assertThat(isEmpty).isFalse();
        assertThat(isBlank).isFalse();

        and("the string should contain expected content");
        assertThat(testString).contains("Hello");
        assertThat(testString).contains("World");
    }

    @Test
    public void shouldValidatePerformanceTiming() {
        given("a performance test scenario");
        stopWatch.reset();
        stopWatch.start();

        when("a simulated operation is performed");
        simulateOperation();

        then("the operation should complete within reasonable time");
        stopWatch.stop();
        long duration = stopWatch.getTime();
        assertThat(duration).isLessThan(1000); // Should complete within 1 second

        and("performance metrics should be collected");
        assertThat(duration).isGreaterThan(0);
        System.out.println("Operation completed in: " + duration + "ms");
    }

    @Test
    public void shouldValidateDataTransformation() {
        given("a data transformation scenario");
        String input = "  test data  ";
        storeInContext("input", input);

        when("data is cleaned and transformed");
        String cleaned = StringUtils.trim(input);
        String normalized = StringUtils.strip(input);
        String reversed = StringUtils.reverse(cleaned);

        then("the transformation should be correct");
        assertThat(cleaned).isEqualTo("test data");
        assertThat(normalized).isEqualTo("test data");
        assertThat(reversed).isEqualTo("atad tset");

        and("the original data should be preserved");
        String original = getFromContext("input", String.class);
        assertThat(original).isEqualTo("  test data  ");
    }

    @Test
    public void shouldValidateBusinessLogic() {
        given("a business rule validation scenario");
        int age = 25;
        String name = "John Doe";
        double salary = 50000.0;
        
        storeInContext("age", age);
        storeInContext("name", name);
        storeInContext("salary", salary);

        when("business rules are applied");
        boolean isAdult = age >= 18;
        boolean hasValidName = StringUtils.isNotBlank(name);
        boolean hasGoodSalary = salary > 30000;

        then("the business rules should be satisfied");
        assertThat(isAdult).isTrue();
        assertThat(hasValidName).isTrue();
        assertThat(hasGoodSalary).isTrue();

        and("all conditions should be met for approval");
        boolean approved = isAdult && hasValidName && hasGoodSalary;
        assertThat(approved).isTrue();
    }

    @Test
    public void shouldValidateErrorHandling() {
        given("an error handling scenario");
        String nullString = null;
        String emptyString = "";
        String blankString = "   ";

        when("error conditions are tested");
        boolean isNull = nullString == null;
        boolean isEmpty = StringUtils.isEmpty(emptyString);
        boolean isBlank = StringUtils.isBlank(blankString);

        then("error conditions should be detected");
        assertThat(isNull).isTrue();
        assertThat(isEmpty).isTrue();
        assertThat(isBlank).isTrue();

        and("safe operations should be performed");
        String safeResult = StringUtils.defaultString(nullString, "default");
        assertThat(safeResult).isEqualTo("default");
    }

    // BDD helper methods
    private void given(String description) {
        System.out.println("Given: " + description);
    }

    private void when(String description) {
        System.out.println("When: " + description);
    }

    private void then(String description) {
        System.out.println("Then: " + description);
    }

    private void and(String description) {
        System.out.println("And: " + description);
    }

    private void storeInContext(String key, Object value) {
        testContext.put(key, value);
    }

    private <T> T getFromContext(String key, Class<T> type) {
        return type.cast(testContext.get(key));
    }

    private void simulateOperation() {
        // Simulate some work
        try {
            Thread.sleep(10); // 10ms delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
