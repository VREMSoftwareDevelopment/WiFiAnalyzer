package com.vrem.wifianalyzer;

enum Frequency {
    UNKNOWN(0, 0, 0),
    TWO_POINT_FOUR(2412, 2472, 1),
    TWO_POINT_FOUR_CH_14(2484, 2484, 14),
    FIVE(5170, 5825, 34);

    final int CHANNEL_FREQUENCY_SPREAD = 5;

    private int start;
    private int end;
    private int offset;

    private Frequency(int start, int end, int offset) {
        this.start = start;
        this.end = end;
        this.offset = offset;
    }

    boolean inRange(int value) {
        return value >= start && value <= end;
    }
    int channel(int value) {
        if (inRange(value)) {
            return (value - start) / CHANNEL_FREQUENCY_SPREAD + offset;
        }
        return 0;
    }

    static Frequency find(int value) {
        for (Frequency frequency: Frequency.values()) {
            if (frequency.inRange(value)) {
                return frequency;
            }
        }
        return Frequency.UNKNOWN;
    }
}
