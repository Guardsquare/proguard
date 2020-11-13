package com.example.demo;

import org.springframework.stereotype.Component;

/**
 * The component name will be "TestComponent" since it is 
 * not set explicitly like @Component("MyComponent").
 */
@Component
public class TestComponent
{
    public int getAnswer() { return 42; }
}
