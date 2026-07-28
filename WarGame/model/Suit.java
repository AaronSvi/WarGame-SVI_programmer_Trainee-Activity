package model;

/**
 * The four card suits.
 *
 * The requirements doc ranks suits (descending): diamond, heart, spade, club.
 * That ranking only matters as a tiebreaker when two players lay the same
 * rank in the same round (see Card#compareStrength). The numeric "value"
 * below encodes that ranking: a higher value beats a lower one.
 */
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

    /** Parses the single-letter code used in the deck text file (e.g. "D", "H"). */
    public static Suit fromCode(String code) {
        for (Suit s : values()) {
            if (s.code.equalsIgnoreCase(code)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown suit code: " + code);
    }
}
