package entities;

import java.util.Objects;

public class Key {
    Node uNode;
    int vIndex;

    @Override
    public int hashCode() {
        return Objects.hash(uNode.getNumber(), vIndex);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Key) {
            return uNode.getNumber() == ((Key) obj).uNode.getNumber() && vIndex == ((Key) obj).vIndex;
        } else {
            return false;
        }
    }
}
