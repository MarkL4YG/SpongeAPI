/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.api.command.parameters.flags;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.parameters.CommandExecutionContext;
import org.spongepowered.api.command.parameters.Parameter;
import org.spongepowered.api.command.parameters.tokens.TokenizedArgs;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.ResettableBuilder;

/**
 * Defines how flags are parsed.
 *
 * <p>Flags in commands are elements that start with a single dash followed by
 * a letter (-f), or two dashes followed by a string (--flag). Flags can
 * optionally be followed by a value (--flag [value], -f [value] or -f[value])
 * </p>
 *
 * <p>Flags can be specified anywhere in a command, unless
 * {@link Builder#setAnchorFlags(boolean)} is set to true, in which case, flags
 * can only be set at the beginning of an argument set.</p>
 */
public interface Flags {

    /**
     * Gets a {@link Builder} to build up a flagset.
     *
     * @return The {@link Builder}
     */
    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    /**
     * Attempts to parse the next element using these flags.
     *
     * <p>If the next element is not a flag, the states of {@link TokenizedArgs}
     * and {@link CommandExecutionContext} will not change. If the next element
     * is not a <em>recognized</em> flag, but could be a flag, it will be parsed
     * in accordance with the {@link UnknownFlagBehavior} supplied (or
     * {@link UnknownFlagBehaviors#ERROR} if not specified) for:</p>
     *
     * <ul>
     *     <li>{@link Builder#setUnknownShortFlagBehavior(UnknownFlagBehavior)}
     *     for any short flag (like {@code -f})</li>
     *     <li>{@link Builder#setUnknownLongFlagBehavior(UnknownFlagBehavior)}
     *     for any long flag (like {@code --flag})</li>
     * </ul>
     *
     * @param source The {@link CommandSource} that is running this command
     * @param args The {@link TokenizedArgs}
     * @param context The {@link CommandExecutionContext}
     */
    void parse(CommandSource source, TokenizedArgs args, CommandExecutionContext context);

    /**
     * Gets the usage for the flag potion of the command.
     *
     * @param src The {@link CommandSource}
     * @return The usage
     */
    Text getUsage(CommandSource src);

    /**
     * A builder for creating a flag element.
     */
    interface Builder extends ResettableBuilder<Flags, Builder> {

        /**
         * Allow a flag with any of the provided specifications and require
         * no value. This flag will be exposed in a
         * {@link CommandExecutionContext} under the key equivalent to the
         * first flag in the specification array with the value {@code true}.
         *
         * <p>Each entry in the specification will be treated as its own flag.
         * There are two types of flags: short and long.</p>
         *
         * <ul>
         *     <li>A single character flag is known as a short flag, and is
         *     specified on the commandline by prefixing with one dash, such as
         *     "-a", "-b" etc.</li>
         *     <li>A multi character flag is known as a long flag, and is
         *     specified on the commandline by prefixing with two dashes, such
         *     as "--flag"</li>
         * </ul>
         *
         * @param specs The flag specifications
         * @return This builder, for chaining
         */
        Builder flag(String... specs);

        /**
         * Allow a flag with any of the provided specifications that has no
         * value but requires the source to have a specific permission to
         * specify the command.
         *
         * @see #flag(String...) for details on the format
         * @param flagPermission The required permission
         * @param specs The flag specifications
         * @return This builder, for chaining
         */
        Builder permissionFlag(final String flagPermission, String... specs);

        /**
         * Allow a flag with any of the provided specifications, with the given
         * command element. The flag may be present multiple times, and may
         * therefore have multiple values.
         *
         * @see #flag(String...) for information on how the flag specifications
         *     are parsed
         * @param value The command element used to parse any occurrences
         * @param specs The flag specifications
         * @return This builder, for chaining
         */
        Builder valueFlag(Parameter value, String... specs);

        /**
         * Sets how long flags that are not registered should be handled when
         * encountered.
         *
         * @param behavior The behavior to use
         * @return This builder, for chaining
         */
        Builder setUnknownLongFlagBehavior(UnknownFlagBehavior behavior);

        /**
         * Sets how long flags that are not registered should be handled when
         * encountered.
         *
         * @param behavior The behavior to use
         * @return This builder, for chaining
         */
        Builder setUnknownShortFlagBehavior(UnknownFlagBehavior behavior);

        /**
         * Whether flags should be anchored to the beginning of the text (so
         * flags will only be picked up if they are at the beginning of the
         * input).
         *
         * @param anchorFlags Whether flags are anchored
         * @return This builder, for chaining
         */
        Builder setAnchorFlags(boolean anchorFlags);

        /**
         * Builds a {@link Flags} object.
         *
         * @return The {@link Flags} object.
         */
        Flags build();

    }

}
