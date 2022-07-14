package domain.values;

import domain.entities.CustomFile;

public class STR {
    public static final String EMPTY = "";

    public static boolean isEmpty(String password) {
        return password == null || password.length() == 0;
    }
}
