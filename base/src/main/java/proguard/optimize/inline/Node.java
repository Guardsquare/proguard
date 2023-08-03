package proguard.optimize.inline;

import java.util.List;
import java.util.Objects;

public final class Node {
    public InstructionAtOffset value;
    public List<Node> children;

    Node(InstructionAtOffset value, List<Node> children) {
        this.value = value;
        this.children = children;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Node that = (Node) obj;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Node[" +
                "value=" + value + ']';
    }
}
