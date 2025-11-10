package com.github.icecheesecat.kantaicraft.exception;

import com.github.icecheesecat.kantaicraft.KantaiCraft;

public class KantaiCraftException extends RuntimeException {

    public KantaiCraftException(Class<?> n_class, String message) {
        super("Error from " + n_class.descriptorString() + " -> " + message);
        KantaiCraft.LOGGER.info("Error from " + n_class.descriptorString() + " -> " + message);
    }

}
