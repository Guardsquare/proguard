package com.example.demo.sub;

import org.springframework.stereotype.Component;

/**
 * The component name will be "TestComponent2" since it is 
 * not set explicitly like @Component("MyComponent2").
 */
@Component
public class TestComponent2
{
    public String getMessage() { return "hello"; }
}
