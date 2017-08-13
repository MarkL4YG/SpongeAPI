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
package org.spongepowered.api.command.parameter.managed.standard;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.parameter.ArgumentParseException;
import org.spongepowered.api.command.parameter.managed.ValueParameterModifier;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.util.ResettableBuilder;

import java.util.Optional;
import java.util.function.Function;

// TODO: Better name?
public class VariableValueParameterModifiers {

    private VariableValueParameterModifiers() {}

    /**
     * Creates a builder that can build a modifier that supplies a default value
     * if a parameter cannot parse an argument.
     *
     * @return The builder
     */
    public static DefaultValueModifierBuilder defaultValueModifierBuilder() {
        return Sponge.getRegistry().createBuilder(DefaultValueModifierBuilder.class);
    }

    /**
     * Creates a builder that can build a modifier that instructs a parameter to
     * execute the specified number of times.
     *
     * @return The builder
     */
    public static RepeatedValueModifierBuilder repeatedValueModifierBuilder() {
        return Sponge.getRegistry().createBuilder(RepeatedValueModifierBuilder.class);
    }

    /**
     * Creates a builder that can build a modifier that parses selectors, should
     * one be passed to the parameter.
     *
     * <p>In-built parameter types where it makes sense to support selectors
     * (such as {@link CatalogedValueParameters#PLAYER} do not need this
     * modifier as they will already check selectors.
     * </p>
     *
     * @return The builder
     */
    public static SelectorValueModifierBuilder selectorValueModifierBuilder() {
        return Sponge.getRegistry().createBuilder(SelectorValueModifierBuilder.class);
    }

    /**
     * A builder that creates a {@link ValueParameterModifier} that supplies
     * a default value if the parameter in question fails to parse an argument.
     */
    public interface DefaultValueModifierBuilder extends ResettableBuilder<ValueParameterModifier, DefaultValueModifierBuilder> {

        /**
         * Sets the {@link Function} that supplies the default value.
         *
         * <p>If this function returns {@link Optional#empty()}, the parameter
         * will throw an {@link ArgumentParseException}, as if the element was
         * not optional.</p>
         *
         * @param defaultValueFunction The {@link Function} that supplies the
         *                             default value, if any
         * @return This builder, for chaining
         */
        DefaultValueModifierBuilder setDefaultValueFunction(Function<CommandSource, Optional<?>> defaultValueFunction);

        /**
         * Builds this {@link ValueParameterModifier}.
         *
         * @return The modifier
         * @throws IllegalStateException if the default value function has not
         *                               been set
         */
        ValueParameterModifier build();

    }

    /**
     * A builder the creates a {@link ValueParameterModifier} that causes a
     * parameter to parse a specified number of arguments.
     */
    public interface RepeatedValueModifierBuilder extends ResettableBuilder<ValueParameterModifier, RepeatedValueModifierBuilder> {

        /**
         * Sets how many times a parameter should execute
         *
         * @param numberOfTimes The number of times to execute
         * @return This builder, for chaining
         * @throws IllegalArgumentException if numberOfTimes is zero or negative
         */
        RepeatedValueModifierBuilder setNumberOfTimes(int numberOfTimes);

        /**
         * Builds this {@link ValueParameterModifier}.
         *
         * @return The modifier
         * @throws IllegalStateException if the number of arguments to parse
         *                               has not been set
         */
        ValueParameterModifier build();

    }

    /**
     * A builder that creates a {@link ValueParameterModifier} that will
     * attempt to parse the argument as a selector if the element begins
     * with an "@" symbol, and will take over all parsing in that scenario.
     */
    public interface SelectorValueModifierBuilder extends ResettableBuilder<ValueParameterModifier, SelectorValueModifierBuilder> {

        /**
         * Specifies that entities that are of the specified type can be
         * returned by this selector
         *
         * @param entityType The class
         * @return This builder, for chaining
         */
        SelectorValueModifierBuilder entityType(Class<? extends Entity> entityType);

        /**
         * Specifies that entities that are the specified {@link EntityType}
         * can be returned by this selector
         *
         * @param entityType The {@link EntityType}
         * @return This builder, for chaining
         */
        default SelectorValueModifierBuilder entityType(EntityType entityType) {
            return entityType(entityType.getEntityClass());
        }

        /**
         * Sets whether to throw an exception if more than one entity is
         * returned by the selector.
         *
         * <p>If this is false, then developers <strong>must</strong> account for
         * the fact that more than one entity can be returned.</p>
         *
         * @param expectOne Whether to expect only one entity to be
         *                  returned
         * @return This builder, for chaining
         */
        SelectorValueModifierBuilder setExpectOne(boolean expectOne);

        /**
         * If strict mode is enabled, if an entity that is not included
         * this modifier (through the entityType methods), an exception
         * is thrown. Otherwise, the nonconforming entities are just
         * ignored and not returned.
         *
         * @param strict true to enable strict mode, false otherwise
         * @return This builder, for chaining
         */
        SelectorValueModifierBuilder setStrict(boolean strict);

        /**
         * Builds this {@link ValueParameterModifier}.
         *
         * @return The modifier
         * @throws IllegalStateException if the entity type has not been
         *                               set
         */
        ValueParameterModifier build();

    }

}
