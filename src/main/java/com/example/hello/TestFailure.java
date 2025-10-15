package com.example.hello;

// This class has intentional syntax errors to test failure scenarios
public class TestFailure {
    
    // Missing semicolon - will cause compilation error
    public void testMethod() {
        System.out.println("This will fail")
    }
    
    // Missing closing brace - will cause syntax error
    public void anotherMethod() {
        System.out.println("This will also fail");
    
}
