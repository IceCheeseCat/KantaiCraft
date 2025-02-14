package com.github.icecheesecat.kantaicraft.util;

public interface IProcessValue {
    ProcessValue getSecond();
    ProcessValue getMinute();
    ProcessValue getHour();
    default int getTick() {
        return getHour().getTick() + getMinute().getTick() + getSecond().getTick();
    }
}
