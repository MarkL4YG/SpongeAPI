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
package org.spongepowered.api.command.parameters.specification.factories;

import org.spongepowered.api.CatalogType;
import org.spongepowered.api.command.parameters.specification.ValueParameter;

import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Provides {@link ValueParameter}s that cannot be cataloged.
 */
public interface ValueParameterFactory {

    /**
     * Returns a parameter that attempts to select a member of the specified
     * {@link CatalogType} by the provided ID. If there is no match, and no ID
     * prefix was specified, the provided prefixes are prepended to the
     * provided string and the registry is rescanned.
     *
     * @param catalogType The {@link Class} type of the {@link CatalogType} that
     *                    should be returned.
     * @param <T> The type
     * @return The constructed {@link ValueParameter}.
     */
    <T extends CatalogType> ValueParameter catalogedElement(Class<T> catalogType, String... prefixes);

    /**
     * Returns a parameter that allows selecting from a limited set of values,
     * specified by the keys in the provided map, which is able to change
     * through the use of the {@link Supplier}. These choices are case
     * insensitive. If a choice is matched, the corresponding value in the map
     * will be returned.
     *
     * <p>If there are 5 or fewer choices available, and "showUsage" is true,
     * the choices will be shown in the command usage. Otherwise, the usage
     * will only display only the key.</p>
     *
     * @param showUsage Whether to show the usage.
     * @param choices The choices for this parameter.
     * @return The constructed {@link ValueParameter}.
     */
    ValueParameter choices(boolean showUsage, Supplier<Map<String, ?>> choices);

    /**
     * Returns a parameter that allows selecting from a limited set of values,
     * specified by the keys in the provided collection, which is able to change
     * through the use of the {@link Supplier}. These choices are case
     * insensitive. If a choice is matched, the corresponding value in the map
     * will be returned.
     *
     * <p>If there are 5 or fewer choices available, and "showUsage" is true,
     * the choices will be shown in the command usage. Otherwise, the usage
     * will only display only the key.</p>
     *
     * @param showUsage Whether to show the usage.
     * @param choices The choices for this parameter.
     * @param valueFunction The function that converts a choice into a value.
     * @return The constructed {@link ValueParameter}.
     */
    ValueParameter choices(boolean showUsage, Supplier<Collection<String>> choices, Function<String, ?> valueFunction);

    /**
     * Require the argument to be a key under the provided enum.
     *
     * <p>Gives values of provide enum type. The matcher is case insensitive.
     * </p>
     *
     * @param enumClass The {@link Class} of the {@link Enum} to use
     * @param <T> The type
     * @return The constructed {@link ValueParameter}
     */
    <T extends Enum<T>> ValueParameter enumValue(Class<T> enumClass);

    /**
     * Expect a literal sequence of arguments. This element matches the input
     * against a predefined array of arguments expected to be present,
     * case-insensitively.
     *
     * @param returnedValue The value to put at key if this argument matches.
     *                      May be null to indicate to add nothing to the
     *                      context
     * @param literalSupplier A {@link Supplier} containing sequence of
     *                        arguments expected
     * @return The constructed {@link ValueParameter}
     */
    ValueParameter literal(@Nullable Object returnedValue, Supplier<Iterable<String>> literalSupplier);

}
