package org.spongepowered.api.command.parameters.specification;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

/**
 * Defines how a parameter should be parsed.
 */
@FunctionalInterface
public interface ValueUsage {

    /**
     * Gets the usage string for the argument.
     *
     * @param key The {@link Text} that defines the parameter key
     * @param source The source requesting the usage
     * @return The usage
     */
    Text getUsage(Text key, CommandSource source);

}
