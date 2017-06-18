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

import com.flowpowered.math.vector.Vector3d;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.command.parameters.specification.CatalogedValueParameter;
import org.spongepowered.api.command.parameters.specification.CatalogedValueParameterModifiers;
import org.spongepowered.api.command.parameters.specification.CatalogedValueParameters;
import org.spongepowered.api.command.parameters.specification.ValueParameterModifiers;
import org.spongepowered.api.command.parameters.specification.ValueParameters;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.storage.WorldProperties;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Contains wrapper methods for creating parameters, as an alternative to using
 * the {@link Parameter.Builder}s.
 *
 * <p>This is intended for ease of transition. It is strongly suggested that
 * developers use {@link Parameter#builder()} instead.</p>
 */
public final class GenericParameters {

    private GenericParameters() {}

    /**
     * Expects no arguments.
     *
     * @return An expectation of no arguments
     */
    public static Parameter.Builder none(Text key) {
        return Parameter.builder().key(key).parser(CatalogedValueParameters.NONE);
    }

    /**
     * Expect an argument to represent an online player, or if nothing matches
     * and the source is a {@link Player}, give the player. If nothing matches
     * and the source is not a player, throw an exception.
     *
     * <p>Gives value of type {@link Player}.</p>
     *
     * @param key The key to store under
     * @return the argument
     */
    public static Parameter.Builder playerOrSource(Text key) {
        return Parameter.builder().key(key).playerOrSource();
    }

    /**
     * Expect an argument to represent an online player.
     * Gives value of type {@link Player}
     *
     * @param key The key to store under
     * @return the argument
     */
    public static Parameter.Builder player(Text key) {
        return Parameter.builder().key(key).player();
    }

    /**
     * Expect an argument to represent a player who has been online at some
     * point, as a {@link User}.
     *
     * @param key The key to store under
     * @return the argument
     */
    public static Parameter.Builder user(Text key) {
        return Parameter.builder().key(key).user();
    }

    /**
     * Expect an argument to represent a player who has been online at some
     * point, as a {@link User}, or if nothing matches and the source is a
     * {@link User}, give the user. If nothing matches and the source is not
     * a {@link User}, throw an exception.
     *
     * @param key The key to store under
     * @return the argument
     */
    public static Parameter.Builder userOrSource(Text key) {
        return Parameter.builder().key(key).userOrSource();
    }

    /**
     * Expect an argument to represent a world. This gives a WorldProperties
     * object rather than an actual world in order to include unloaded worlds
     * as well. Gives values of type {@link WorldProperties}.
     *
     * @param key The key to store under
     * @return the argument
     */
    public static Parameter.Builder world(Text key) {
        return Parameter.builder().key(key).worldProperties();
    }

    /**
     * Expect an argument to represent a dimension.
     * Gives values of tye {@link DimensionType}
     *
     * @param key The key to store under
     * @return the argument
     */
    public static Parameter.Builder dimension(Text key) {
        return Parameter.builder().key(key).dimension();
    }

    /**
     * Expect an argument to represent a {@link Vector3d}.
     *
     * @param key The key to store under
     * @return the argument
     */
    public static Parameter.Builder vector3d(Text key) {
        return Parameter.builder().key(key).vector3d();
    }

    /**
     * Expect an argument to represent a {@link Location}.
     *
     * @param key The key to store under
     * @return the argument
     */
    public static Parameter.Builder location(Text key) {
        return Parameter.builder().key(key).location();
    }

    /**
     * Expect an argument that is a member of the specified dummy type T.
     *
     * @param key The key to store the resolved value under
     * @param catalogType The type expected
     * @param <T> The type to return
     * @return the argument
     */
    public static <T extends CatalogType> Parameter.Builder catalogedElement(Text key, Class<T> catalogType) {
        return Parameter.builder().key(key).catalogedElement(catalogType);
    }

    /**
     * Expect an argument to represent a {@link PluginContainer}'s id.
     *
     * @param key The key to store under
     * @return the argument
     */
    public static Parameter.Builder plugin(Text key) {
        return Parameter.builder().key(key).plugin();
    }

    /**
     * Consumes a series of arguments. Usage is the elements concatenated
     *
     * @param elements The series of arguments to expect
     * @return the element to match the input
     */
    public static Parameter seq(Parameter... elements) {
        return Parameter.seq(elements);
    }

    /**
     * Consumes a series of arguments. Usage is the elements concatenated
     *
     * @param elements The series of arguments to expect
     * @return the element to match the input
     */
    public static Parameter seq(Parameter.Builder... elements) {
        return Parameter.seq(Arrays.stream(elements).map(Parameter.Builder::build).collect(Collectors.toList()));
    }

    /**
     * Return an argument that allows selecting from a limited set of values.
     *
     * <p>If there are 5 or fewer choices available, the choices will be shown
     * in the command usage. Otherwise, the usage will only display only the
     * key.</p>
     *
     * <p>To override this behavior, see {@link #choices(Text, Map, boolean)}.
     * </p>
     *
     * @param key The key to store the resulting value under
     * @param choices The choices users can choose from
     * @return the element to match the input
     */
    public static Parameter.Builder choices(Text key, Map<String, ?> choices) {
        return choices(key, choices, true);
    }

    /**
     * Return an argument that allows selecting from a limited set of values.
     *
     * <p>Unless {@code choicesInUsage} is true, general command usage will only
     * display the provided key.</p>
     *
     * @param key The key to store the resulting value under
     * @param choices The choices users can choose from
     * @param choicesInUsage Whether to display the available choices, or simply
     *      the provided key, as part of usage
     * @return the element to match the input
     */
    public static Parameter.Builder choices(Text key, Map<String, ?> choices, boolean choicesInUsage) {
        Map<String, Object> immChoices = ImmutableMap.copyOf(choices);
        return choices(key, immChoices::keySet, immChoices::get, choicesInUsage);
    }

    /**
     * Return an argument that allows selecting from a limited set of values.
     *
     * <p>If there are 5 or fewer choices available, the choices will be shown
     * in the command usage. Otherwise, the usage will only display only the
     * key.</p>
     *
     * <p>To override this behavior, see {@link #choices(Text, Map, boolean)}.
     * </p>
     *
     * @param key The key to store the resulting value under
     * @param keys The function that will supply available keys
     * @param values The function that maps an element of {@code key} to a value
     *      and any other key to {@code null}
     * @return the element to match the input
     */
    public static Parameter.Builder choices(Text key, Supplier<Collection<String>> keys, Function<String, ?> values) {
        return choices(key, keys, values, true);
    }

    /**
     * Return an argument that allows selecting from a limited set of values.
     * Unless {@code choicesInUsage} is true, general command usage will only
     * display the provided key.
     *
     * @param key The key to store the resulting value under
     * @param keys The function that will supply available keys
     * @param values The function that maps an element of {@code key} to a value
     *      and any other key to {@code null}
     * @param choicesInUsage Whether to display the available choices, or simply
     *      the provided key, as part of usage
     * @return the element to match the input
     */
    public static Parameter.Builder choices(Text key, Supplier<Collection<String>> keys, Function<String, ?> values, boolean choicesInUsage) {
        return Parameter.builder().key(key).parser(ValueParameters.choices(choicesInUsage, keys, values));
    }

    /**
     * Specifies that this parameter should only produce one result.
     *
     * @param element The element to modify
     * @return The element
     */
    public static Parameter.Builder onlyOne(Parameter.Builder element) {
        return element.addModifierToBeginning(CatalogedValueParameterModifiers.ONLY_ONE);
    }

    /**
     * Specifies that this parameter requires the user to have a permission
     * before it will perform any parsing.
     *
     * @param element The element to modify
     * @param permission The permission
     * @return The element
     */
    public static Parameter.Builder requiringPermission(Parameter.Builder element, @Nullable String permission) {
        return element.permission(permission);
    }

    /**
     * Specifies that this parameter should parse as many times as possible.
     *
     * @param element The element to modify
     * @return The element
     */
    public static Parameter.Builder allOf(Parameter.Builder element) {
        return element.addModifierToBeginning(CatalogedValueParameterModifiers.ALL_OF);
    }

    /**
     * Specifies that this parameter should be repeated the specified number of
     * times.
     *
     * @param element The element to modify
     * @param times The number of times to repeat the element
     * @return The element
     */
    public static Parameter.Builder repeated(Parameter.Builder element, int times) {
        return element.addModifierToBeginning(ValueParameterModifiers.repeated(times));
    }

    /**
     * Specifies that this parameter is optional.
     *
     * @param element The element to modify
     * @return The element
     */
    public static Parameter.Builder optional(Parameter.Builder element) {
        return element.addModifierToBeginning(CatalogedValueParameterModifiers.OPTIONAL);
    }

    /**
     * Specifies that this parameter is optional, and the element has a default
     * value.
     *
     * @param element The element to modify
     * @param value The default value
     * @return The element
     */
    public static Parameter.Builder optional(Parameter.Builder element, Object value) {
        return optional(element).addModifierToBeginning(ValueParameterModifiers.defaultValue(value));
    }

    /**
     * Specifies that this parameter is optional and weak.
     *
     * @param element The element to modify
     * @return The element
     */
    public static Parameter.Builder optionalWeak(Parameter.Builder element) {
        return element.addModifierToBeginning(CatalogedValueParameterModifiers.OPTIONAL_WEAK);
    }

    /**
     * Specifies that this parameter is optional and weak, and the element has
     * a default value.
     *
     * @param element The element to modify
     * @param value The default value
     * @return The element
     */
    public static Parameter.Builder optionalWeak(Parameter.Builder element, Object value) {
        return optionalWeak(element).addModifierToBeginning(ValueParameterModifiers.defaultValue(value));
    }

}
