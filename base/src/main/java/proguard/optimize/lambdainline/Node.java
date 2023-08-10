package proguard.optimize.lambdainline;

import java.util.List;

/**
 * A very simple DTO that contains Node objects used to build a tree of InstructionAtOffset objects used in the
 * SourceTracer. A tree is used because an argument of a method call can have multiple source locations. The program
 * will at runtime only get the value form one location of course, but we cannot predict which branches the code will
 * take at compile time, because of this we have a tree that contains all the options.
 */
public final class Node {
    public InstructionAtOffset value;
    public List<Node> children;

    Node(InstructionAtOffset value, List<Node> children) {
        this.value = value;
        this.children = children;
    }
}
