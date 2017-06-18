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
package org.spongepowered.api.command.parameters.specification;

import org.spongepowered.api.command.parameters.CommandExecutionContext;
import org.spongepowered.api.command.parameters.specification.factories.ValueParameterModifierFactory;
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;

import java.util.function.Supplier;

/**
 * Class containing {@link ValueParameterModifier}s.
 */
public final class ValueParameterModifiers {

    private ValueParameterModifiers() {}

    private static final ValueParameterModifierFactory factory = DummyObjectProvider.createFor(ValueParameterModifierFactory.class, "factory");

    /**
     * Indicates that the parameter should continue to parse all remaining
     * elements in the provided set of arguments.
     */
    public static ValueParameterModifier allOf() {
        return CatalogedValueParameterModifiers.ALL_OF;
    }

    /**
     * Indicates that there should only be one element returned by the
     * parameter.
     */
    public final CatalogedValueParameterModifier onlyOne() {
        return CatalogedValueParameterModifiers.ONLY_ONE;
    }

    /**
     * Indicates that the parameter is optional, but will throw an exception
     * if an argument exists to be parsed.
     */
    public final CatalogedValueParameterModifier optional() {
        return CatalogedValueParameterModifiers.OPTIONAL;
    }

    /**
     * Indicates that the parameter is optional, and will not prevent the rest
     * of the command arguments from being parsed if this cannot parse an
     * argument.
     */
    public final CatalogedValueParameterModifier optionalWeak() {
        return CatalogedValueParameterModifiers.OPTIONAL_WEAK;
    }

    /**
     * Requires the parameter to be provided a certain number of times.
     *
     * <p>Command values will be stored under their provided keys in the
     * {@link CommandExecutionContext}.</p>
     *
     * @param times The number of times to repeat the element.
     * @return The {@link ValueParameterModifier}
     */
    public static ValueParameterModifier repeated(int times) {
        return factory.repeated(times);
    }

    /**
     * If after a {@link ValueParameter}'s execution there is no value stored
     * in the associated key, this modifier will put the supplied default into
     * the context.
     *
     * @param defaultValue The default value.
     * @return The {@link ValueParameterModifier}
     */
    public static ValueParameterModifier defaultValue(Object defaultValue) {
        return factory.defaultValue(defaultValue);
    }

    /**
     * If after a {@link ValueParameter}'s execution there is no value stored
     * in the associated key, this modifier will put the default obtained from
     * the supplied {@link Supplier} into the context, if the default is
     * non-null.
     *
     * @param defaultValueSupplier The default value {@link Supplier}.
     * @return The {@link ValueParameterModifier}
     */
    public static ValueParameterModifier defaultValueSupplier(Supplier<Object> defaultValueSupplier) {
        return factory.defaultValueSupplier(defaultValueSupplier);
    }

}
