package model;


public enum Suit {
    DIAMOND("D", 4),
    HEART("H", 3),
    SPADE("S", 2),
    CLUB("C", 1);

    private final String code;
    private final int value;

    Suit(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }

    public static Suit fromCode(String code) {
        for (Suit s : values()) {
            if (s.code.equalsIgnoreCase(code)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown suit code: " + code);
    }
}
