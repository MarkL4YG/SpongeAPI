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
package org.spongepowered.api.command.specification;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.parameters.Parameter;
import org.spongepowered.api.command.parameters.flags.Flags;
import org.spongepowered.api.command.parameters.tokens.InputTokenizer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.ResettableBuilder;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * A high level interface for {@link CommandCallable}s that handles argument
 * parsing and subcommand handling.
 */
public interface CommandSpecification extends CommandCallable {

    /**
     * Gets a builder for building a {@link CommandSpecification}
     *
     * @return The {@link Builder}
     */
    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    /**
     * A high level {@link Builder} for creating a command.
     */
    interface Builder extends ResettableBuilder<CommandSpecification, Builder> {

        /**
         * Adds a {@link CommandCallable} as a child to this command, under the
         * supplied keys. The keys are case insensitive.
         *
         * @param child The {@link CommandCallable} that is a child.
         * @param keys The keys to register as a sub command.
         * @return This builder, for chaining
         */
        Builder addChild(CommandCallable child, String... keys);

        /**
         * Adds a {@link CommandCallable} as a child to this command, under the
         * supplied keys. The keys are case insensitive.
         *
         * @param child The {@link CommandCallable} that is a child.
         * @param keys The keys to register as a sub command.
         * @return This builder, for chaining
         */
        Builder addChild(CommandCallable child, Iterable<String> keys);

        /**
         * Adds multiple {@link CommandCallable} as children to this command,
         * under the supplied keys. The keys are case insensitive.
         *
         * @param children The {@link Map} that contains a mapping of keys to
         *                 their respective {@link CommandCallable} children.
         * @return This builder, for chaining
         */
        Builder addChildren(Map<? extends Iterable<String>, CommandCallable> children);

        /**
         * If this is set to true, then if the parent command (this) requires
         * a {@link CommandSource} to have a permission
         * (see {@link #permission(String)}), this permission is
         * required for all children too. If this is set to false, then
         * child commands <em>do not</em> require this permission.
         *
         * <p>This defaults to {@code true}.</p>
         *
         * @param required Whether this command's permission is required for
         *                 child commands.
         * @return This builder, for chaining
         */
        Builder requirePermissionForChildren(boolean required);

        /**
         * Determines what to do if a child command throws an exception.
         *
         * <p>Defaults to {@link ChildExceptionBehaviors#RETHROW}, which means
         * that if a child command fails to execute, this command will not
         * attempt to run in its place.</p>
         *
         * @param exceptionBehavior The {@link ChildExceptionBehavior} to adhere
         *                          to.
         * @return This builder, for chaining
         */
        Builder childExceptionBehavior(ChildExceptionBehavior exceptionBehavior);

        /**
         * Performs the logic of the command.
         *
         * <p>This is only optional if child commands are specified.</p>
         *
         * @param executor The {@link CommandExecutor} that will run the command
         * @return This builder, for chaining
         */
        Builder executor(CommandExecutor executor);

        /**
         * Provides the description for this command.
         *
         * <p>A one line summary should be provided to
         * {@link #simpleDescription(Text)}</p>
         *
         * @param extendedDescription The description to use, or {@code null}
         *                            for no description.
         * @return This builder, for chaining
         */
        Builder extendedDescription(@Nullable Text extendedDescription);

        /**
         * The flags that this command should accept. See {@link Flags}.
         *
         * @param flags The {@link Flags} to accept
         * @return This builder, for chaining
         */
        Builder flags(Flags flags);

        /**
         * Determines how an argument string should be split.
         *
         * <p>Defaults to splitting on spaces, ignoring spaces in quoted
         * regions.</p>
         *
         * @param tokenizer The {@link InputTokenizer} to use
         * @return This builder, for chaining
         */
        Builder inputTokenizer(InputTokenizer tokenizer);

        /**
         * The parameter set to use when parsing arguments. Parameters will be
         * used in the order provided here.
         *
         * @param parameters The {@link Parameter}s to use
         * @return This builder, for chaining
         */
        Builder parameters(Parameter... parameters);

        /**
         * The parameter set to use when parsing arguments. Parameters will be
         * used in the order provided here.
         *
         * @param parameters The {@link Parameter}s to use
         * @return This builder, for chaining
         */
        Builder parameters(Iterable<Parameter> parameters);

        /**
         * The permission that a {@link CommandSource} requires to run this
         * command, or {@code null} if no permission is required.
         *
         * @param permission The permission that is required, or {@code null}
         *                   for no permission
         * @return This builder, for chaining
         */
        Builder permission(@Nullable String permission);

        /**
         * Provides a simple description for this command, typically no more
         * than one line.
         *
         * <p>Fuller descriptions should be provided through
         * {@link #extendedDescription(Text)}</p>
         *
         * @param description The description to use, or {@code null} for no
         *                    description
         * @return This builder, for chaining
         */
        Builder simpleDescription(@Nullable Text description);

        /**
         * Builds this command.
         *
         * @return The command, ready for registration
         */
        CommandSpecification build();

    }

}
