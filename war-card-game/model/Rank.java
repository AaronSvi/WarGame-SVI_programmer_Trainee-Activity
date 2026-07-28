package model;

/**
 * The thirteen card ranks, in the descending order given by the requirements
 * doc: ace, king, queen, jack, ten, nine, eight, seven, six, five, four,
 * three, two. The "value" field is what Card#compareStrength actually
 * compares.
 */
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

    /** Parses the code used in the deck text file (e.g. "A", "K", "10", "7"). */
    public static Rank fromCode(String code) {
        for (Rank r : values()) {
            if (r.code.equalsIgnoreCase(code)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Unknown rank code: " + code);
    }
}
