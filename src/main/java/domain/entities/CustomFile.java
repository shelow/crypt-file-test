package domain.entities;

public class CustomFile {
    public final String name;
    public final byte[] fileContent;

    public CustomFile(String name, byte[] fileContent) {
        this.name = name;
        this.fileContent = fileContent;
    }
}
