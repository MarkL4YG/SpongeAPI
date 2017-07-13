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

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.parameters.specification.factories.ValueParameterFactory;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;
import org.spongepowered.api.world.Dimension;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.storage.WorldProperties;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

/**
 * Class containing common {@link ValueParameters}s.
 */
public final class ValueParameters {

    private ValueParameters() {}

    private static final ValueParameterFactory factory = DummyObjectProvider.createFor(ValueParameterFactory.class, "factory");

    /**
     * Require an argument to be a boolean.
     *
     * <p>The recognized true values are:</p>
     *
     * <ul>
     *     <li>true</li>
     *     <li>t</li>
     *     <li>yes</li>
     *     <li>y</li>
     *     <li>veryumuchso</li>
     * </ul>
     *
     * <p>The recognized false values are:</p>
     *
     * <ul>
     *     <li>false</li>
     *     <li>f</li>
     *     <li>no</li>
     *     <li>n</li>
     *     <li>notatall</li>
     * </ul>
     *
     * <p>Returns a {@link Boolean}</p>
     */
    public static ValueParameter bool() {
        return CatalogedValueParameters.BOOLEAN;
    };

    /**
     * Expect an argument to represent a dimension.
     *
     * <p>Returns a {@link Dimension}</p>
     */
    public static ValueParameter dimension() {
        return CatalogedValueParameters.DIMENSION;
    }

    /**
     * Require an argument to be an double-precision floating point number.
     *
     * <p>Returns a {@link Double}</p>
     */
    public static ValueParameter doubleNumber() {
        return CatalogedValueParameters.DOUBLE;
    }

    /**
     * Require an argument to be a duration.
     *
     * <p>The duration can be specified in one of these ways:</p>
     *
     * <ul>
     *     <li>A number, which will be assumed to be seconds.</li>
     *     <li>An element of the form {@code D:HH:MM:SS} - days and hours can
     *     be omitted.</li>
     *     <li>An element of the form
     *     {@code [weeks]w[days]d[hours]h[minutes]m[seconds]s[millis]ms},
     *     where any zeroed unit can be omitted.</li>
     * </ul>
     *
     * <p>Returns a {@link Duration}</p>
     */
    public static ValueParameter duration() {
        return CatalogedValueParameters.DIMENSION;
    }

    /**
     * TODO: This
     *
     * <p>This parameter accepts selectors.</p>
     *
     * <p>Returns an {@link Entity}.</p>
     */
    public static ValueParameter entity() {
        return CatalogedValueParameters.ENTITY;
    }

    /**
     * TODO: This
     *
     * <p>This parameter accepts selectors.</p>
     *
     * <p>Returns an {@link Entity}.</p>
     */
    public static ValueParameter entityOrSource() {
        return CatalogedValueParameters.ENTITY_OR_SOURCE;
    }

    /**
     * Require an argument to be an integer (base 10) number.
     *
     * <p>Returns an {@link Integer}.</p>
     */
    public static ValueParameter integer() {
        return CatalogedValueParameters.INTEGER;
    }

    /**
     * Expect an argument to represent a {@link Location}.
     *
     * <p>Listens to:</p>
     *
     * <ul>
     *     <li>#spawn:&lt;world></li>
     *     <li>#me: Location of the current source</li>
     * </ul>
     *
     * <p>Returns a {@link Location}.</p>
     */
    public static ValueParameter location() {
        return CatalogedValueParameters.LOCATION;
    }

    /**
     * Require an argument to be a long (base 10).
     *
     * <p>Returns a {@link Long}.</p>
     */
    public static ValueParameter longNum() {
        return CatalogedValueParameters.LONG;
    }

    /**
     * Does not parse any arguments, returning nothing.
     *
     * <p>Returns nothing - no entry will be placed into any provided key.</p>
     */
    public static ValueParameter none() {
        return CatalogedValueParameters.NONE;
    }

    /**
     * Expect an argument to represent an online player.
     *
     * <p>This parameter accepts selectors.</p>
     *
     * <p>Returns a {@link Player}.</p>
     */
    public static ValueParameter player() {
        return CatalogedValueParameters.PLAYER;
    }

    /**
     * Expect an argument to represent an online player, or if nothing matches
     * and the calling {@link CommandSource} is a {@link Player}, returns the
     * calling player.
     *
     * <p>This parameter accepts selectors.</p>
     *
     * <p>Returns a {@link Player}.</p>
     */
    public static ValueParameter playerOrSource() {
        return CatalogedValueParameters.PLAYER_OR_SOURCE;
    }

    /**
     * Expect an argument to represent a {@link PluginContainer}'s id.
     *
     * <p>Returns a {@link PluginContainer}</p>
     */
    public static ValueParameter plugin() {
        return CatalogedValueParameters.PLUGIN;
    }

    /**
     * Require one or more strings, without any processing, which are combined
     * into a single, space-separated string.
     *
     * <p>Returns a {@link String}.</p>
     */
    public static ValueParameter remainingJoinedStrings() {
        return CatalogedValueParameters.REMAINING_JOINED_STRINGS;
    }

    /**
     * Require one or more strings, which are combined into a single,
     * space-separated string.
     *
     * <p>Returns a {@link String}.</p>
     */
    public static ValueParameter remainingRawJoinedStrings() {
        return CatalogedValueParameters.REMAINING_RAW_JOINED_STRINGS;
    }

    /**
     * Require an argument to be a string.
     *
     * <p>Returns a {@link String}.</p>
     */
    public static ValueParameter string() {
        return CatalogedValueParameters.STRING;
    }

    /**
     * Expect an argument to represent a player who has been online at some
     * point, as a {@link User}.
     *
     * <p>This parameter accepts selectors (to obtain players).</p>
     *
     * <p>Returns a {@link User}.</p>
     */
    public static ValueParameter user() {
        return CatalogedValueParameters.USER;
    }

    /**
     * Expect an argument to represent a player who has been online at some
     * point, as a {@link User}. If nothing matches and the calling
     * {@link CommandSource} is a {@link User}, return the caller.
     *
     * <p>This parameter accepts selectors (to obtain players).</p>
     *
     * <p>Returns a {@link User}.</p>
     */
    public static ValueParameter userOrSource() {
        return CatalogedValueParameters.USER_OR_SOURCE;
    }

    /**
     * Expect an argument to represent a {@link Vector3d}.
     *
     * <p>The expected syntax is:</p>
     *
     * <blockquote><pre> x,y,z
     * x y z.</pre></blockquote>
     *
     * <p>Each element can be relative to a location -- relative is ~(num)</p>
     *
     * <p>Returns a {@link Vector3d}.</p>
     */
    public static ValueParameter vector3d() {
        return CatalogedValueParameters.VECTOR3D;
    }

    /**
     * Expect an argument to represent a world.
     *
     * <p>Accepted formats:</p>
     *
     * <ul>
     *     <li>#first</li>
     *     <li>#&lt;dimension></li>
     *     <li>&lt;name></li>
     *     <li>#me</li>
     * </ul>
     *
     * <p>Returns a {@link WorldProperties}.</p>
     */
    public static ValueParameter worldProperties() {
        return CatalogedValueParameters.WORLD_PROPERTIES;
    }

    /**
     * Returns a parameter that attempts to select a member of the specified
     * {@link CatalogType} by the provided ID. If there is no match, and no
     * ID prefix was specified, the prefixes "minecraft:" and "sponge:"
     * are assumed to be candidates (in that order) and the registry is
     * rescanned.
     *
     * <p>To override this behaviour, use {@link #choices(boolean, String...)}
     * instead.</p>
     *
     * @param catalogType The {@link Class} type of the {@link CatalogType} that
     *                    should be returned.
     * @param <T> The type
     * @return The constructed {@link ValueParameter}.
     */
    public static <T extends CatalogType> ValueParameter catalogedElement(Class<T> catalogType) {
        return catalogedElement(catalogType, "minecraft", "sponge");
    }

    /**
     * Returns a parameter that attempts to select a member of the specified
     * {@link CatalogType} by the provided ID. If there is no match, and no
     * ID prefix was specified, the provided prefixes are prepended
     * to the provided string and the registry is rescanned.
     *
     * @param catalogType The {@link Class} type of the {@link CatalogType} that
     *                    should be returned.
     * @param <T> The type
     * @return The constructed {@link ValueParameter}.
     */
    public static <T extends CatalogType> ValueParameter catalogedElement(Class<T> catalogType, String... prefixes) {
        return factory.catalogedElement(catalogType, prefixes);
    }

    /**
     * Returns a parameter that allows selecting from a limited set of values.
     * These choices are case insensitive. The matched choice will be returned.
     *
     * <p>If there are 5 or fewer choices available, the choices will be shown
     * in the command usage. Otherwise, the usage will only display only the
     * key.</p>
     *
     * <p>To override this behavior, see {@link #choices(boolean, String...)}.
     * </p>
     *
     * @param choices The choices for this parameter.
     * @return The constructed {@link ValueParameter}.
     */
    public static ValueParameter choices(String... choices) {
        return choices(true, choices);
    }

    /**
     * Returns a parameter that allows selecting from a limited set of values.
     * These choices are case insensitive. The matched choice will be returned.
     *
     * <p>If there are 5 or fewer choices available, and "showUsage" is true,
     * the choices will be shown in the command usage. Otherwise, the usage
     * will only display only the key.</p>
     *
     * @param choices The choices for this parameter.
     * @return The constructed {@link ValueParameter}.
     */
    public static ValueParameter choices(boolean showUsage, String... choices) {
        return choices(showUsage, Arrays.stream(choices).collect(Collectors.toMap(x -> x.toLowerCase(Locale.ENGLISH), Function.identity())));
    }

    /**
     * Returns a parameter that allows selecting from a limited set of values,
     * specified by the keys in the provided map. These choices are case
     * insensitive. If a choice is matched, the corresponding value in the map
     * will be returned.
     *
     * <p>If there are 5 or fewer choices available, the choices will be shown
     * in the command usage. Otherwise, the usage will only display only the
     * key.</p>
     *
     * <p>To override this behavior, see {@link #choices(boolean, Map)}.</p>
     *
     * @param choices The choices for this parameter.
     * @return The constructed {@link ValueParameter}.
     */
    public static ValueParameter choices(Map<String, ?> choices) {
        return choices(true, choices);
    }

    /**
     * Returns a parameter that allows selecting from a limited set of values,
     * specified by the keys in the provided map. These choices are case
     * insensitive. If a choice is matched, the corresponding value in the map
     * will be returned.
     *
     * <p>If there are 5 or fewer choices available, and "showUsage" is true,
     * the choices will be shown in the command usage. Otherwise, the usage
     * will only display only the key.</p>
     *
     * @param choices The choices for this parameter.
     * @return The constructed {@link ValueParameter}.
     */
    public static ValueParameter choices(boolean showUsage, Map<String, ?> choices) {
        return choices(showUsage, () -> choices);
    }

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
    public static ValueParameter choices(boolean showUsage, Supplier<Map<String, ?>> choices) {
        return factory.choices(showUsage, choices);
    }

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
    public static ValueParameter choices(boolean showUsage, Supplier<Collection<String>> choices, Function<String, ?> valueFunction) {
        return factory.choices(showUsage, choices, valueFunction);
    }

    /**
     * Require the argument to be a key under the provided enum.
     *
     * <p>Gives values of the provided enum type. The matcher is case
     * insensitive.</p>
     *
     * @param enumClass The {@link Class} of the {@link Enum} to use
     * @param <T> The type
     * @return The constructed {@link ValueParameter}
     */
    public static <T extends Enum<T>> ValueParameter enumValue(Class<T> enumClass) {
        return factory.enumValue(enumClass);
    }

    /**
     * Expect a literal sequence of arguments. This element matches the input
     * against a predefined array of arguments expected to be present,
     * case-insensitively.
     *
     * @param returnedValue The value to put at key if this argument matches.
     *                      May be null to indicate to add nothing to the
     *                      context
     * @param literal The sequence of arguments expected
     * @return The constructed {@link ValueParameter}
     */
    public static ValueParameter literal(@Nullable Object returnedValue, String... literal) {
        List<String> literalList = Arrays.asList(literal);
        return literal(returnedValue, () -> literalList);
    }

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
    public static ValueParameter literal(@Nullable Object returnedValue, Supplier<Iterable<String>> literalSupplier) {
        return factory.literal(returnedValue, literalSupplier);
    }

}
