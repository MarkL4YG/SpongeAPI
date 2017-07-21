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
package org.spongepowered.api.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameters.Parameter;
import org.spongepowered.api.command.parameters.flags.Flags;
import org.spongepowered.api.command.parameters.tokens.InputTokenizer;
import org.spongepowered.api.command.managed.ChildExceptionBehavior;
import org.spongepowered.api.command.managed.ChildExceptionBehaviors;
import org.spongepowered.api.command.managed.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.ResettableBuilder;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

/**
 * The Command interface is the low-level interface that all commands in the
 * Sponge ecosystem inherit.
 *
 * <p><strong>Most</strong> plugins are highly recommended (but not obligated)
 * to use {@link Command#builder()} to create commands. The
 * {@link Command.Builder} allows plugins to take advantage of a higher level
 * of abstraction, such as argument parsers and simple child command handling,
 * removing the need for boilerplate code.</p>
 *
 * <p>Plugins are free to implement this interface should they prefer to do so.
 * Custom implementations of this class are not required to implement a sane
 * {@link Object#equals(Object)}, but are highly encouraged to do so.</p>
 */
public interface Command {

    /**
     * Gets a builder for building a {@link Command}
     *
     * @return The {@link Builder}
     */
    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    /**
     * Execute the command based on input arguments.
     *
     * <p>The implementing class must perform the necessary permission
     * checks.</p>
     *
     * @param source The caller of the command
     * @param arguments The raw arguments for this command
     * @return The result of a command being processed
     * @throws CommandException Thrown on a command error
     */
    CommandResult process(CommandSource source, String arguments) throws CommandException;

    /**
     * Gets a list of suggestions based on input.
     *
     * <p>If a suggestion is chosen by the user, it will replace the last
     * word.</p>
     *
     * @param source The command source
     * @param arguments The arguments entered up to this point
     * @param targetPosition The position the source is looking at when
     *     performing tab completion
     * @return A list of suggestions
     * @throws CommandException Thrown if there was a parsing error
     */
    List<String> getSuggestions(CommandSource source, String arguments, @Nullable Location<World> targetPosition) throws CommandException;

    /**
     * Test whether this command can probably be executed by the given source.
     *
     * <p>If implementations are unsure if the command can be executed by
     * the source, {@code true} should be returned. Return values of this method
     * may be used to determine whether this command is listed in command
     * listings.</p>
     *
     * @param source The caller of the command
     * @return Whether permission is (probably) granted
     */
    boolean testPermission(CommandSource source);

    /**
     * Gets a short one-line description of this command.
     *
     * <p>The help system may display the description in the command list.</p>
     *
     * @param source The source of the help request
     * @return A description
     */
    Optional<Text> getShortDescription(CommandSource source);

    /**
     * Gets a longer formatted help message about this command.
     *
     * <p>It is recommended to use the default text color and style. Sections
     * with text actions (e.g. hyperlinks) should be underlined.</p>
     *
     * <p>Multi-line messages can be created by separating the lines with
     * {@code \n}.</p>
     *
     * <p>The help system may display this message when a source requests
     * detailed information about a command.</p>
     *
     * @param source The source of the help request
     * @return A help text
     */
    Optional<Text> getHelp(CommandSource source);

    /**
     * Gets the usage string of this command.
     *
     * <p>A usage string may look like
     * {@code [-w &lt;world&gt;] &lt;var1&gt; &lt;var2&gt;}.</p>
     *
     * <p>The string must not contain the command alias.</p>
     *
     * @param source The source of the help request
     * @return A usage string
     */
    Text getUsage(CommandSource source);


    /**
     * A high level {@link Builder} for creating a {@link Command}.
     *
     * <p>When creating a command, ensure that a {@link CommandExecutor}
     * <strong>and/or</strong> a child command is specified.</p>
     */
    interface Builder extends ResettableBuilder<Command, Builder> {

        /**
         * Adds a {@link Command} as a child to this command, under the
         * supplied keys. The keys are case insensitive.
         *
         * @param child The {@link Command} that is a child.
         * @param keys The keys to register as a sub command.
         * @return This builder, for chaining
         */
        Builder addChild(Command child, String... keys);

        /**
         * Adds a {@link Command} as a child to this command, under the
         * supplied keys. The keys are case insensitive.
         *
         * @param child The {@link Command} that is a child.
         * @param keys The keys to register as a sub command.
         * @return This builder, for chaining
         */
        Builder addChild(Command child, Iterable<String> keys);

        /**
         * Adds multiple {@link Command} as children to this command,
         * under the supplied keys. The keys are case insensitive.
         *
         * @param children The {@link Map} that contains a mapping of keys to
         *                 their respective {@link Command} children.
         * @return This builder, for chaining
         */
        Builder addChildren(Map<? extends Iterable<String>, ? extends Command> children);

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
         * <p>Defaults to {@link ChildExceptionBehaviors#SUPPRESS}, which means
         * that if a child command fails to execute, the error will be ignored
         * and this base command will attempt to execute.</p>
         *
         * <p>See {@link ChildExceptionBehaviors} for other possible behaviors.
         * </p>
         *
         * @param exceptionBehavior The {@link ChildExceptionBehavior} to adhere
         *                          to.
         * @return This builder, for chaining
         */
        Builder childExceptionBehavior(ChildExceptionBehavior exceptionBehavior);

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
        Builder description(@Nullable Text description);

        /**
         * Provides the logic of the command.
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
         * {@link #description(Text)}</p>
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
         * Builds this command.
         *
         * @return The command, ready for registration
         */
        Command build();

    }

}
