package fr.salim.equisign.domain.exceptions;

public class DuplicateFileNameException extends RuntimeException {
    public DuplicateFileNameException(String message) {
        super(message);
    }
}
