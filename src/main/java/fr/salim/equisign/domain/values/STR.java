package fr.salim.equisign.domain.values;

public class STR {
    public static final String EMPTY = "";
    public static boolean isEmpty(String password) {
        return password == null || password.length() == 0;
    }
}
