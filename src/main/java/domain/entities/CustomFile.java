package domain.entities;

import java.util.Arrays;
import java.util.Objects;

public class CustomFile {
    public final String name;
    public final byte[] content;

    public CustomFile(String name, byte[] content) {
        this.name = name;
        this.content = content;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomFile that = (CustomFile) o;
        return Objects.equals(name, that.name) && Arrays.equals(content, that.content);
    }

    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
