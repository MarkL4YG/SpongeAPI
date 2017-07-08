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
package org.spongepowered.api.command.parameters;

import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.parameters.specification.CatalogedValueParameterModifiers;
import org.spongepowered.api.command.parameters.specification.CatalogedValueParameters;
import org.spongepowered.api.command.parameters.specification.ValueCompleter;
import org.spongepowered.api.command.parameters.specification.ValueParameter;
import org.spongepowered.api.command.parameters.specification.ValueParameterModifier;
import org.spongepowered.api.command.parameters.specification.ValueParameterModifiers;
import org.spongepowered.api.command.parameters.specification.ValueParameters;
import org.spongepowered.api.command.parameters.specification.ValueParser;
import org.spongepowered.api.command.parameters.specification.ValueUsage;
import org.spongepowered.api.command.parameters.tokens.TokenizedArgs;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.ResettableBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import javax.annotation.Nullable;

/**
 * Defines how an element of a command argument string should be parsed.
 */
public interface Parameter {

    /**
     * Gets a builder that builds a {@link Parameter}.
     *
     * @return The {@link Builder}
     */
    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    /**
     * Returns a {@link Parameter} that attempts to parse an argument using the
     * supplied parameters in order. Once a parameter has parsed the argument
     * successfully, no more parameters supplied here will be attempted.
     *
     * @param parameters The {@link Parameter}s
     * @return The {@link Parameter}
     */
    static Parameter firstOf(Parameter... parameters) {
        return Sponge.getRegistry().createBuilder(SequenceBuilder.class).requireAll(false).add(parameters).build();
    }

    /**
     * Returns a {@link Parameter} that attempts to parse an argument using the
     * supplied parameters in order. Once a parameter has parsed the argument
     * successfully, no more parameters supplied here will be attempted.
     *
     * @param parameters The {@link Parameter}s
     * @return The {@link Parameter}
     */
    static Parameter firstOf(Iterable<Parameter> parameters) {
        return Sponge.getRegistry().createBuilder(SequenceBuilder.class).requireAll(false).add(parameters).build();
    }

    /**
     * Returns a {@link Parameter} that parses arguments using the supplied
     * parameters in order.
     *
     * @param parameters The {@link Parameter}s
     * @return The {@link Parameter}
     */
    static Parameter seq(Parameter... parameters) {
        return Sponge.getRegistry().createBuilder(SequenceBuilder.class).requireAll(true).add(parameters).build();
    }

    /**
     * Returns a {@link Parameter} that parses arguments using the supplied
     * parameters in order.
     *
     * @param parameters The {@link Parameter}s
     * @return The {@link Parameter}
     */
    static Parameter seq(Iterable<Parameter> parameters) {
        return Sponge.getRegistry().createBuilder(SequenceBuilder.class).requireAll(true).add(parameters).build();
    }

    /**
     * The key for the parameter. Parsed values will be stored under a string
     * representation of this key.
     *
     * @return The key.
     */
    Text getKey();

    /**
     * Parses the next element(s) in the {@link CommandExecutionContext}
     *
     * @param source The {@link CommandSource} executing this command.
     * @param args The {@link TokenizedArgs} containing the strings that need
     *             to be parsed
     * @param context The {@link CommandExecutionContext} that contains the
     *                current state of the execution.
     * @throws ParameterParseException thrown if the parameter could not be
     *                                 parsed
     */
    void parse(CommandSource source, TokenizedArgs args, CommandExecutionContext context) throws ParameterParseException;

    /**
     * Returns potential completions of the current tokenized argument.
     *
     * @param source The {@link CommandSource} executing this command.
     * @param args The {@link TokenizedArgs} containing the strings that need
     *             to be parsed
     * @param context The {@link CommandExecutionContext} that contains the
     *                current state of the execution.
     * @return The potential completions.
     * @throws ParameterParseException thrown if the parameter could not be
     *                                 parsed
     */
    List<String> complete(CommandSource source, TokenizedArgs args, CommandExecutionContext context) throws ParameterParseException;

    /**
     * Gets the usage of this parameter.
     *
     * @param source The {@link CommandSource} that requested the usage
     * @return The usage
     */
    Text getUsage(CommandSource source);

    /**
     * Builds a {@link Parameter} from constituent components.
     */
    interface Builder extends ResettableBuilder<Parameter, Builder> {

        /**
         * The key that the parameter will place parsed values into.
         *
         * <p>Mandatory</p>
         *
         * @param key The key.
         * @return This builder, for chaining
         */
        default Builder key(String key) {
            return key(Text.of(key));
        }

        /**
         * The key that the parameter will place parsed values into.
         *
         * <p>Mandatory</p>
         *
         * @param key The key.
         * @return This builder, for chaining
         */
        Builder key(Text key);

        /**
         * The {@link ValueParser} that will extract the value(s) from the
         * parameters. If this is a {@link ValueParameter}, the object's
         * complete and usage methods will be used for completion and usage
         * unless this builder's {@link #onComplete(ValueCompleter)} and
         * {@link #usage(ValueUsage)} methods are specified.
         *
         * @param parser The {@link ValueParameter} to use
         * @return This builder, for chaining
         */
        Builder parser(ValueParser parser);

        /**
         * Provides a function that provides tab completions
         *
         * <p>Optional. If this is <code>null</code> (or never set),
         * completions will either be done via the supplied
         * {@link #parser(ValueParser)} or will just return an empty
         * list. If this is supplied, no modifiers will run on completion.</p>
         *
         * @param completer The {@link ValueCompleter}
         * @return This builder, for chaining
         */
        Builder onComplete(@Nullable ValueCompleter completer);

        /**
         * Sets the usage. The {@link BiFunction} accepts the parameter key
         * and the calling {@link CommandSource}.
         *
         * <p>Optional. If this is <code>null</code> (or never set),
         * the usage string will either be provided via the supplied
         * {@link #parser(ValueParser)} or will just return
         * the parameter's key. If this is supplied, no modifiers will run on
         * usage.</p>
         *
         * @param usage The function
         * @return This builder, for chaining
         */
        Builder usage(@Nullable ValueUsage usage);

        /**
         * Adds {@link ValueParameterModifier}s that modify the behavior of the
         * parameter, for example, by requiring that only one output is
         * obtained.
         *
         * <p>Note that the modifiers wrap around the call to the value parser,
         * the first will be called which will be expected to call into
         * later modifiers. They will be called in this order.</p>
         *
         * @param modifiers The modifiers, in the order that they should
         *                  be executed
         * @return This builder, for chaining
         */
        Builder addModifiers(ValueParameterModifier... modifiers);

        /**
         * Adds {@link ValueParameterModifier}s that modify the behavior of the
         * parameter, for example, by requiring that only one output is
         * obtained.
         *
         * <p>Note that the modifiers wrap around the call to the value parser,
         * the first will be called which will be expected to call into
         * later modifiers. They will be called in this order.</p>
         *
         * @param modifiers The modifiers, in the order that they should
         *                  be executed
         * @return This builder, for chaining
         */
        Builder addModifiers(List<ValueParameterModifier> modifiers);

        /**
         * Adds a {@link ValueParameterModifier} to the beginning of the current
         * set of modifiers - that is, the supplied modifier will be called
         * first.
         *
         * @param modifier The modifier to add to the beginning of the queue
         * @return This builder, for chaining
         */
        Builder addModifierToBeginning(ValueParameterModifier modifier);

        /**
         * Sets the permission that the executing {@link CommandSource} is
         * required to have in order for this parameter to be parsed.
         *
         * <p>If the source does not have this permission, this parameter
         * will simply be skipped. Consider combining this with
         * the {@link CatalogedValueParameterModifiers#OPTIONAL} or
         * {@link CatalogedValueParameterModifiers#OPTIONAL_WEAK} modifiers,
         * so that those with permission can also skip this parameter.</p>
         *
         * @param permission The permission to check for, or {@code null} for
         *                   no check.
         * @return This builder, for chaining
         */
        Builder permission(@Nullable String permission);

        // Convenience methods

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#BOOLEAN}
         *
         * @return This builder, for chaining
         */
        default Builder bool() {
            return parser(CatalogedValueParameters.BOOLEAN);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#DIMENSION}
         *
         * @return This builder, for chaining
         */
        default Builder dimension() {
            return parser(CatalogedValueParameters.DIMENSION);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#DURATION}
         *
         * @return This builder, for chaining
         */
        default Builder duration() {
            return parser(CatalogedValueParameters.DURATION);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#DOUBLE}
         *
         * @return This builder, for chaining
         */
        default Builder doubleNumber() {
            return parser(CatalogedValueParameters.DOUBLE);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#ENTITY}
         *
         * @return This builder, for chaining
         */
        default Builder entity() {
            return parser(CatalogedValueParameters.ENTITY);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#ENTITY_OR_SOURCE}
         *
         * @return This builder, for chaining
         */
        default Builder entityOrSource() {
            return parser(CatalogedValueParameters.ENTITY_OR_SOURCE);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#INTEGER}
         *
         * @return This builder, for chaining
         */
        default Builder integer() {
            return parser(CatalogedValueParameters.INTEGER);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#LOCATION}
         *
         * @return This builder, for chaining
         */
        default Builder location() {
            return parser(CatalogedValueParameters.LOCATION);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#LONG}
         *
         * @return This builder, for chaining
         */
        default Builder longNumber() {
            return parser(CatalogedValueParameters.LONG);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#PLAYER}
         *
         * @return This builder, for chaining
         */
        default Builder player() {
            return parser(CatalogedValueParameters.PLAYER);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#PLAYER_OR_SOURCE}
         *
         * @return This builder, for chaining
         */
        default Builder playerOrSource() {
            return parser(CatalogedValueParameters.PLAYER_OR_SOURCE);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#PLUGIN}
         *
         * @return This builder, for chaining
         */
        default Builder plugin() {
            return parser(CatalogedValueParameters.PLUGIN);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#REMAINING_JOINED_STRINGS}
         *
         * @return This builder, for chaining
         */
        default Builder remainingJoinedStrings() {
            return parser(CatalogedValueParameters.REMAINING_JOINED_STRINGS);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#REMAINING_RAW_JOINED_STRINGS}
         *
         * @return This builder, for chaining
         */
        default Builder remainingRawJoinedStrings() {
            return parser(CatalogedValueParameters.REMAINING_RAW_JOINED_STRINGS);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#STRING}
         *
         * @return This builder, for chaining
         */
        default Builder string() {
            return parser(CatalogedValueParameters.STRING);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#USER}
         *
         * @return This builder, for chaining
         */
        default Builder user() {
            return parser(CatalogedValueParameters.USER);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#USER_OR_SOURCE}
         *
         * @return This builder, for chaining
         */
        default Builder userOrSource() {
            return parser(CatalogedValueParameters.USER_OR_SOURCE);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#VECTOR3D}
         *
         * @return This builder, for chaining
         */
        default Builder vector3d() {
            return parser(CatalogedValueParameters.VECTOR3D);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link CatalogedValueParameters#WORLD_PROPERTIES}
         *
         * @return This builder, for chaining
         */
        default Builder worldProperties() {
            return parser(CatalogedValueParameters.WORLD_PROPERTIES);
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link ValueParameters#catalogedElement(Class)}
         *
         * @param type The type of {@link CatalogType} to retrieve
         * @return This builder, for chaining
         */
        default <T extends CatalogType> Builder catalogedElement(Class<T> type) {
            return parser(ValueParameters.catalogedElement(type));
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link ValueParameters#choices(String...)}
         *
         * @param choices The choices.
         * @return This builder, for chaining
         */
        default Builder choices(String... choices) {
            return parser(ValueParameters.choices(choices));
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link ValueParameters#choices(Map)}
         *
         * @param choices The choices.
         * @return This builder, for chaining
         */
        default Builder choices(Map<String, Object> choices) {
            return parser(ValueParameters.choices(choices));
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link ValueParameters#enumValue(Class)}
         *
         * @param enumClass The {@link Enum} to use.
         * @return This builder, for chaining
         */
        default <T extends Enum<T>> Builder enumValue(Class<T> enumClass) {
            return parser(ValueParameters.enumValue(enumClass));
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link ValueParameters#literal(Object, String...)}
         *
         * @param returnedValue The value to return if one of the provided
         *                      literals is matched
         * @param literal The literals, one of which needs to match
         * @return This builder, for chaining
         */
        default Builder literal(@Nullable Object returnedValue, String... literal) {
            return parser(ValueParameters.literal(returnedValue, literal));
        }

        /**
         * Equivalent to {@link #parser(ValueParser)} with
         * {@link ValueParameters#literal(Object, Supplier)}
         *
         * @param returnedValue The value to return if one of the provided
         *                      literals is matched
         * @param literalSupplier A {@link Supplier} that will return the
         *                        allowable literals at runtime
         * @return This builder, for chaining
         */
        default Builder literal(@Nullable Object returnedValue, Supplier<Iterable<String>> literalSupplier) {
            return parser(ValueParameters.literal(returnedValue, literalSupplier));
        }

        /**
         * Equivalent to {@link #addModifiers(ValueParameterModifier...)} with
         * {@link CatalogedValueParameterModifiers#ONLY_ONE}}
         *
         * @return This builder, for chaining
         */
        default Builder onlyOne() {
            return addModifiers(CatalogedValueParameterModifiers.ONLY_ONE);
        }

        /**
         * Equivalent to {@link #addModifiers(ValueParameterModifier...)} with
         * {@link CatalogedValueParameterModifiers#ALL_OF}}
         *
         * @return This builder, for chaining
         */
        default Builder allOf() {
            return addModifiers(CatalogedValueParameterModifiers.ALL_OF);
        }

        /**
         * Equivalent to {@link #addModifiers(ValueParameterModifier...)} with
         * {@link CatalogedValueParameterModifiers#OPTIONAL}}
         *
         * @return This builder, for chaining
         */
        default Builder optional() {
            return addModifiers(CatalogedValueParameterModifiers.OPTIONAL);
        }

        /**
         * Equivalent to {@link #addModifiers(ValueParameterModifier...)} with
         * {@link CatalogedValueParameterModifiers#OPTIONAL_WEAK}}
         *
         * @return This builder, for chaining
         */
        default Builder optionalWeak() {
            return addModifiers(CatalogedValueParameterModifiers.OPTIONAL_WEAK);
        }

        /**
         * Equivalent to {@link #addModifiers(ValueParameterModifier...)} with
         * {@link ValueParameterModifiers#repeated(int)}
         *
         * @param times The number of times to repeat this parameter
         * @return This builder, for chaining
         */
        default Builder repeated(int times) {
            return addModifiers(ValueParameterModifiers.repeated(times));
        }

        /**
         * Equivalent to {@link #addModifiers(ValueParameterModifier...)} with
         * {@link ValueParameterModifiers#defaultValue(Object)}
         *
         * @param defaultValue The default value if this parameter does not
         *                     enter a value into the
         *                     {@link CommandExecutionContext}
         * @return This builder, for chaining
         */
        default Builder defaultValue(Object defaultValue) {
            return addModifiers(ValueParameterModifiers.defaultValue(defaultValue));
        }

        /**
         * Equivalent to {@link #addModifiers(ValueParameterModifier...)} with
         * {@link ValueParameterModifiers#defaultValue(Object)}
         *
         * @param defaultValueSupplier Supplies a default value if this
         *                             parameter does not enter a value into
         *                             the {@link CommandExecutionContext}
         * @return This builder, for chaining
         */
        default Builder defaultValueSupplier(Supplier<Object> defaultValueSupplier) {
            return addModifiers(ValueParameterModifiers.defaultValueSupplier(defaultValueSupplier));
        }

        /**
         * Creates a {@link Parameter} from the builder.
         *
         * @return The {@link Parameter}
         */
        Parameter build();

    }

    /**
     * Specifies a builder for creating a {@link Parameter} that returns a
     * parameter that either concatanates all parameters into a single
     * parameter, or just attempts to parse elements until one that parses
     * sticks.
     */
    interface SequenceBuilder extends ResettableBuilder<Parameter, SequenceBuilder> {

        /**
         * Specifies whether this parameter will parse all elements in sequence
         * (true), or whether it will attempt to parse elements until one was
         * parsed (false)
         *
         * @param requireAll Defines the behaviour of the output parameter.
         * @return This builder, for chaining
         */
        SequenceBuilder requireAll(boolean requireAll);

        /**
         * Adds a set of {@link Parameter}s to this builder.
         *
         * <p>The parameters will be parsed in the provided order.</p>
         *
         * @param parameters The parameters to add
         * @return This builder, for chaining
         */
        SequenceBuilder add(Iterable<Parameter> parameters);

        /**
         * Adds a set of {@link Parameter}s to this builder.
         *
         * <p>The parameters will be parsed in the provided order.</p>
         *
         * @param parameters The parameters to add
         * @return This builder, for chaining
         */
        SequenceBuilder add(Parameter... parameters);

        /**
         * Creates a {@link Parameter} from the builder.
         *
         * @return The {@link Parameter}
         */
        Parameter build();

    }

}
