package model;

public enum Rank {
    ACE("A", 14),
    KING("K", 13),
    QUEEN("Q", 12),
    JACK("J", 11),
    TEN("10", 10),
    NINE("9", 9),
    EIGHT("8", 8),
    SEVEN("7", 7),
    SIX("6", 6),
    FIVE("5", 5),
    FOUR("4", 4),
    THREE("3", 3),
    TWO("2", 2);

    private final String code;
    private final int value;

    Rank(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }

    public static Rank fromCode(String code) {
        for (Rank r : values()) {
            if (r.code.equalsIgnoreCase(code)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Unknown rank code: " + code);
    }
}
