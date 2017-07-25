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
package org.spongepowered.api.command.parameter;

import com.google.common.collect.Lists;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.parameter.managed.CatalogedValueParameterModifiers;
import org.spongepowered.api.command.parameter.managed.CatalogedValueParameters;
import org.spongepowered.api.command.parameter.managed.ValueCompleter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.ValueParameterModifier;
import org.spongepowered.api.command.parameter.managed.ValueParameterModifiers;
import org.spongepowered.api.command.parameter.managed.ValueParameters;
import org.spongepowered.api.command.parameter.managed.ValueParser;
import org.spongepowered.api.command.parameter.managed.ValueUsage;
import org.spongepowered.api.command.parameter.token.CommandArgs;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.ResettableBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
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
     * Returns a {@link Parameter.FirstOfBuilder} that allows plugins to attempt
     * to parse an argument using the supplied parameters in order. Once a
     * parameter has parsed the argument successfully, no more parameters
     * supplied here will be attempted.
     *
     * @param parameter The first {@link Parameter}
     * @return The {@link Parameter.FirstOfBuilder} to continue chaining
     */
    static Parameter.FirstOfBuilder firstOf(Parameter parameter) {
        return Sponge.getRegistry().createBuilder(FirstOfBuilder.class).or(parameter);
    }

    /**
     * Returns a {@link Parameter} that attempts to parse an argument using the
     * supplied parameters in order. Once a parameter has parsed the argument
     * successfully, no more parameters supplied here will be attempted.
     *
     * @param first The first {@link Parameter} that should be used for parsing
     * @param second The second {@link Parameter} that should be used for
     *               parsing, should the first {@link Parameter} fail to do so
     * @param parameters The remaining {@link Parameter}s
     * @return The {@link Parameter}
     */
    static Parameter firstOf(Parameter first, Parameter second, Parameter... parameters) {
        return Sponge.getRegistry().createBuilder(FirstOfBuilder.class).or(first).or(second).orFirstOf(parameters).build();
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
        return Sponge.getRegistry().createBuilder(FirstOfBuilder.class).orFirstOf(parameters).build();
    }

    /**
     * Returns a {@link Parameter.SequenceBuilder} that parses arguments using
     * the supplied parameters in order.
     *
     * @param parameter The first {@link Parameter} in the sequence
     * @return The {@link Parameter.SequenceBuilder}, to continue building the
     *         chain
     */
    static Parameter.SequenceBuilder seq(Parameter parameter) {
        return Sponge.getRegistry().createBuilder(SequenceBuilder.class).then(parameter);
    }

    /**
     * Returns a {@link Parameter} that parses arguments using the supplied
     * parameters in order.
     *
     * @param parameters The {@link Parameter}s
     * @return The {@link Parameter}
     */
    static Parameter seq(Parameter first, Parameter second, Parameter... parameters) {
        return Sponge.getRegistry().createBuilder(SequenceBuilder.class).then(first).then(second).then(parameters).build();
    }

    /**
     * Returns a {@link Parameter} that parses arguments using the supplied
     * parameters in order.
     *
     * @param parameters The {@link Parameter}s
     * @return The {@link Parameter}
     */
    static Parameter seq(Iterable<Parameter> parameters) {
        return Sponge.getRegistry().createBuilder(SequenceBuilder.class).then(parameters).build();
    }

    /**
     * Parses the next element(s) in the {@link CommandContext}
     *
     * @param source The {@link CommandSource} executing this command.
     * @param args The {@link CommandArgs} containing the strings that need
     *             to be parsed
     * @param context The {@link CommandContext} that contains the
     *                current state of the execution.
     * @throws ArgumentParseException thrown if the parameter could not be
     *                                 parsed
     */
    void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException;

    /**
     * Returns potential completions of the current tokenized argument.
     *
     * @param source The {@link CommandSource} executing this command.
     * @param args The {@link CommandArgs} containing the strings that need
     *             to be parsed
     * @param context The {@link CommandContext} that contains the
     *                current state of the execution.
     * @return The potential completions.
     * @throws ArgumentParseException thrown if the parameter could not be
     *                                 parsed
     */
    List<String> complete(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException;

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
        default Builder setKey(String key) {
            return setKey(Text.of(key));
        }

        /**
         * The key that the parameter will place parsed values into.
         *
         * <p>Mandatory</p>
         *
         * @param key The key.
         * @return This builder, for chaining
         */
        Builder setKey(Text key);

        /**
         * The {@link ValueParser} that will extract the value(s) from the
         * parameters. If this is a {@link ValueParameter}, the object's
         * complete and usage methods will be used for completion and usage
         * unless this builder's {@link #setSuggestions(ValueCompleter)}} and
         * {@link #setUsage(ValueUsage)} methods are specified.
         *
         * @param parser The {@link ValueParameter} to use
         * @return This builder, for chaining
         */
        Builder setParser(ValueParser parser);

        /**
         * Provides a function that provides tab completions
         *
         * <p>Optional. If this is <code>null</code> (or never set),
         * completions will either be done via the supplied
         * {@link #setParser(ValueParser)} or will just return an empty
         * list. If this is supplied, no modifiers will run on completion.</p>
         *
         * @param completer The {@link ValueCompleter}
         * @return This builder, for chaining
         */
        Builder setSuggestions(@Nullable ValueCompleter completer);

        /**
         * Sets the usage. The {@link BiFunction} accepts the parameter key
         * and the calling {@link CommandSource}.
         *
         * <p>Optional. If this is <code>null</code> (or never set),
         * the usage string will either be provided via the supplied
         * {@link #setParser(ValueParser)} or will just return
         * the parameter's key. If this is supplied, no modifiers will run on
         * usage.</p>
         *
         * @param usage The function
         * @return This builder, for chaining
         */
        Builder setUsage(@Nullable ValueUsage usage);

        /**
         * Adds a {@link ValueParameterModifier} that modify the behavior of the
         * parameter, for example, by requiring that only one output is
         * obtained.
         *
         * <p>Note that the modifiers wrap around the call to the value parser,
         * the first will be called which will be expected to call into
         * later modifiers. They will be called in the order they are added to
         * the builder.</p>
         *
         * @param modifier  The modifier
         * @return This builder, for chaining
         */
        Builder modifier(ValueParameterModifier modifier);

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
        default Builder modifiers(ValueParameterModifier... modifiers) {
            return modifiers(Arrays.asList(modifiers));
        }

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
        default Builder modifiers(List<ValueParameterModifier> modifiers) {
            for (ValueParameterModifier modifier : modifiers) {
                modifier(modifier);
            }

            return this;
        }

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
        Builder setRequiredPermission(@Nullable String permission);

        // Convenience methods

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#BOOLEAN}
         *
         * @return This builder, for chaining
         */
        default Builder bool() {
            return setParser(CatalogedValueParameters.BOOLEAN);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#DIMENSION}
         *
         * @return This builder, for chaining
         */
        default Builder dimension() {
            return setParser(CatalogedValueParameters.DIMENSION);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#DURATION}
         *
         * @return This builder, for chaining
         */
        default Builder duration() {
            return setParser(CatalogedValueParameters.DURATION);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#DOUBLE}
         *
         * @return This builder, for chaining
         */
        default Builder doubleNumber() {
            return setParser(CatalogedValueParameters.DOUBLE);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#ENTITY}
         *
         * @return This builder, for chaining
         */
        default Builder entity() {
            return setParser(CatalogedValueParameters.ENTITY);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#ENTITY_OR_SOURCE}
         *
         * @return This builder, for chaining
         */
        default Builder entityOrSource() {
            return setParser(CatalogedValueParameters.ENTITY_OR_SOURCE);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#INTEGER}
         *
         * @return This builder, for chaining
         */
        default Builder integer() {
            return setParser(CatalogedValueParameters.INTEGER);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#LOCATION}
         *
         * @return This builder, for chaining
         */
        default Builder location() {
            return setParser(CatalogedValueParameters.LOCATION);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#LONG}
         *
         * @return This builder, for chaining
         */
        default Builder longNumber() {
            return setParser(CatalogedValueParameters.LONG);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#PLAYER}
         *
         * @return This builder, for chaining
         */
        default Builder player() {
            return setParser(CatalogedValueParameters.PLAYER);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#PLAYER_OR_SOURCE}
         *
         * @return This builder, for chaining
         */
        default Builder playerOrSource() {
            return setParser(CatalogedValueParameters.PLAYER_OR_SOURCE);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#PLUGIN}
         *
         * @return This builder, for chaining
         */
        default Builder plugin() {
            return setParser(CatalogedValueParameters.PLUGIN);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#REMAINING_JOINED_STRINGS}
         *
         * @return This builder, for chaining
         */
        default Builder remainingJoinedStrings() {
            return setParser(CatalogedValueParameters.REMAINING_JOINED_STRINGS);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#REMAINING_RAW_JOINED_STRINGS}
         *
         * @return This builder, for chaining
         */
        default Builder remainingRawJoinedStrings() {
            return setParser(CatalogedValueParameters.REMAINING_RAW_JOINED_STRINGS);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#STRING}
         *
         * @return This builder, for chaining
         */
        default Builder string() {
            return setParser(CatalogedValueParameters.STRING);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#USER}
         *
         * @return This builder, for chaining
         */
        default Builder user() {
            return setParser(CatalogedValueParameters.USER);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#USER_OR_SOURCE}
         *
         * @return This builder, for chaining
         */
        default Builder userOrSource() {
            return setParser(CatalogedValueParameters.USER_OR_SOURCE);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#VECTOR3D}
         *
         * @return This builder, for chaining
         */
        default Builder vector3d() {
            return setParser(CatalogedValueParameters.VECTOR3D);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link CatalogedValueParameters#WORLD_PROPERTIES}
         *
         * @return This builder, for chaining
         */
        default Builder worldProperties() {
            return setParser(CatalogedValueParameters.WORLD_PROPERTIES);
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link ValueParameters#catalogedElement(Class)}
         *
         * @param type The type of {@link CatalogType} to retrieve
         * @return This builder, for chaining
         */
        default <T extends CatalogType> Builder catalogedElement(Class<T> type) {
            return setParser(ValueParameters.catalogedElement(type));
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link ValueParameters#choices(String...)}
         *
         * @param choices The choices.
         * @return This builder, for chaining
         */
        default Builder choices(String... choices) {
            return setParser(ValueParameters.choices(choices));
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link ValueParameters#choices(Map)}
         *
         * @param choices The choices.
         * @return This builder, for chaining
         */
        default Builder choices(Map<String, ?> choices) {
            return setParser(ValueParameters.choices(choices));
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link ValueParameters#choices(String...)}
         *
         * @param choices The choices.
         * @param valueFunction A function that transforms the choice into a
         *                      returnable value
         * @return This builder, for chaining
         */
        default Builder choices(Supplier<Collection<String>> choices, Function<String, ?> valueFunction) {
            return setParser(ValueParameters.choices(true, choices, valueFunction));
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link ValueParameters#enumValue(Class)}
         *
         * @param enumClass The {@link Enum} to use.
         * @return This builder, for chaining
         */
        default <T extends Enum<T>> Builder enumValue(Class<T> enumClass) {
            return setParser(ValueParameters.enumValue(enumClass));
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link ValueParameters#literal(Object, String...)}
         *
         * @param returnedValue The value to return if one of the provided
         *                      literals is matched
         * @param literal The literals, one of which needs to match
         * @return This builder, for chaining
         */
        default Builder literal(@Nullable Object returnedValue, String... literal) {
            return setParser(ValueParameters.literal(returnedValue, literal));
        }

        /**
         * Equivalent to {@link #setParser(ValueParser)} with
         * {@link ValueParameters#literal(Object, Supplier)}
         *
         * @param returnedValue The value to return if one of the provided
         *                      literals is matched
         * @param literalSupplier A {@link Supplier} that will return the
         *                        allowable literals at runtime
         * @return This builder, for chaining
         */
        default Builder literal(@Nullable Object returnedValue, Supplier<Iterable<String>> literalSupplier) {
            return setParser(ValueParameters.literal(returnedValue, literalSupplier));
        }

        /**
         * Equivalent to {@link #modifiers(ValueParameterModifier...)} with
         * {@link CatalogedValueParameterModifiers#ONLY_ONE}}
         *
         * @return This builder, for chaining
         */
        default Builder onlyOne() {
            return modifiers(CatalogedValueParameterModifiers.ONLY_ONE);
        }

        /**
         * Equivalent to {@link #modifiers(ValueParameterModifier...)} with
         * {@link CatalogedValueParameterModifiers#ALL_OF}}
         *
         * @return This builder, for chaining
         */
        default Builder allOf() {
            return modifiers(CatalogedValueParameterModifiers.ALL_OF);
        }

        /**
         * Equivalent to {@link #modifiers(ValueParameterModifier...)} with
         * {@link CatalogedValueParameterModifiers#OPTIONAL}
         *
         * @return This builder, for chaining
         */
        default Builder optional() {
            return modifiers(CatalogedValueParameterModifiers.OPTIONAL);
        }

        /**
         * Equivalent to {@link #modifiers(ValueParameterModifier...)} with
         * {@link CatalogedValueParameterModifiers#OPTIONAL_WEAK}
         *
         * @return This builder, for chaining
         */
        default Builder optionalWeak() {
            return modifiers(CatalogedValueParameterModifiers.OPTIONAL_WEAK);
        }

        /**
         * Equivalent to {@link #modifiers(ValueParameterModifier...)} with
         * {@link ValueParameterModifiers#defaultValue(Object)}
         *
         * @param defaultValue The default value if this parameter does not
         *                     enter a value into the
         *                     {@link CommandContext}
         * @return This builder, for chaining
         */
        default Builder optionalOrDefault(Object defaultValue) {
            return modifiers(ValueParameterModifiers.defaultValue(defaultValue));
        }

        /**
         * Equivalent to {@link #modifiers(ValueParameterModifier...)} with
         * {@link ValueParameterModifiers#defaultValue(Object)}
         *
         * @param defaultValueSupplier Supplies a default value if this
         *                             parameter does not enter a value into
         *                             the {@link CommandContext}
         * @return This builder, for chaining
         */
        default Builder optionalOrDefaultSupplier(Supplier<Object> defaultValueSupplier) {
            return modifiers(ValueParameterModifiers.defaultValueSupplier(defaultValueSupplier));
        }

        /**
         * Equivalent to {@link #modifiers(ValueParameterModifier...)} with
         * {@link ValueParameterModifiers#repeated(int)}
         *
         * @param times The number of times to repeat this parameter
         * @return This builder, for chaining
         */
        default Builder repeated(int times) {
            return modifiers(ValueParameterModifiers.repeated(times));
        }

        /**
         * Equivalent to {@link #modifiers(ValueParameterModifier...)} with
         * {@link ValueParameterModifiers#selector(Class, boolean, boolean)}.
         *
         * <p>This instructs the parameter to attempt to parse a selector if it
         * is there, restricting the return to the entity type provided.</p>
         *
         * <p>In-built parameter types where it makes sense to support selectors
         * (such as {@link ValueParameters#player()} do not need this modifier
         * as they will already check selectors.
         * </p>
         *
         * <p>If a selector is detected, the associated {@link ValueParameter}
         * will not run.</p>
         *
         * @param entityType The type of {@link Entity} that can be returned by
         *                   the selector
         * @param onlyOne    Whether only one entity should be returned
         * @param strict     If true, the parser will fail if an entity that is
         *                   not of the provided type, else it will just remove
         *                   any non-conforming entities
         * @return This builder, for chaining
         */
        default Builder supportSelectors(Class<? extends Entity> entityType, boolean onlyOne, boolean strict) {
           return modifiers(ValueParameterModifiers.selector(entityType, onlyOne, strict));
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
     * parameter that concatenates all parameters into a single
     * parameter to be executed one by one.
     */
    interface SequenceBuilder extends ResettableBuilder<Parameter, SequenceBuilder> {

        /**
         * Defines the next parameter in the parameter sequence
         *
         * @param parameter The parameter
         * @return This builder, for chaining
         */
        SequenceBuilder then(Parameter parameter);

        /**
         * Adds a set of {@link Parameter}s to this builder.
         *
         * <p>The parameters will be parsed in the provided order.</p>
         *
         * @param parameters The parameters to add
         * @return This builder, for chaining
         */
        default SequenceBuilder then(Parameter... parameters) {
            return then(Arrays.asList(parameters));
        }

        /**
         * Adds a set of {@link Parameter}s to this builder.
         *
         * <p>The parameters will be parsed in the provided order.</p>
         *
         * @param parameters The parameters to add
         * @return This builder, for chaining
         */
        default SequenceBuilder then(Iterable<Parameter> parameters) {
            for (Parameter parameter : parameters) {
                then(parameter);
            }

            return this;
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
     * parameter that concatenates all parameters into a single
     * parameter to be executed one by one.
     */
    interface FirstOfBuilder extends ResettableBuilder<Parameter, FirstOfBuilder> {

        /**
         * Adds a parameter that can be used to parse an argument. Parameters
         * are checked in the order they are added to the builder.
         *
         * @param parameter The parameter
         * @return This builder, for chaining
         */
        FirstOfBuilder or(Parameter parameter);

        /**
         * Adds a set of {@link Parameter}s to this builder.
         *
         * <p>The parameters will be parsed in the provided order until one
         * succeeds.</p>
         *
         * @param parameters The parameters to add
         * @return This builder, for chaining
         */
        default FirstOfBuilder orFirstOf(Parameter... parameters) {
            return orFirstOf(Arrays.asList(parameters));
        }

        /**
         * Adds a set of {@link Parameter}s to this builder.
         *
         * <p>The parameters will be parsed in the provided order until one
         * succeeds.</p>
         *
         * @param parameters The parameters to add
         * @return This builder, for chaining
         */
        default FirstOfBuilder orFirstOf(Iterable<Parameter> parameters) {
            for (Parameter parameter : parameters) {
                or(parameter);
            }

            return this;
        }

        /**
         * Creates a {@link Parameter} from the builder.
         *
         * @return The {@link Parameter}
         */
        Parameter build();

    }

}
