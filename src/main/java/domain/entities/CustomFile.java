package domain.entities;

public class CustomFile {
    public final String name;
    public final byte[] content;

    public CustomFile(String name, byte[] content) {
        this.name = name;
        this.content = content;
    }
}
