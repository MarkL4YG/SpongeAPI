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
package org.spongepowered.api.command.parameter.managed.factories;

import com.google.common.collect.Lists;
import org.spongepowered.api.command.parameter.ArgumentParseException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.managed.ValueParameter;
import org.spongepowered.api.command.parameter.managed.ValueParameterModifier;
import org.spongepowered.api.entity.Entity;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Provides {@link ValueParameterModifier}s that cannot be cataloged.
 */
public interface ValueParameterModifierFactory {

    /**
     * Requires the parameter to be provided a certain number of times.
     *
     * <p>Command values will be stored under their provided keys in the
     * {@link CommandContext}.</p>
     *
     * @param times The number of times to repeat the element.
     * @return The {@link ValueParameterModifier}
     */
    ValueParameterModifier repeated(int times);

    /**
     * Specifies that the parameter could be satisfied by entities returned from
     * a selector instead.
     *
     * @param supportedEntity The {@link Class} that represents the entity
     *                        that should be returned by the selector
     * @param requireOne If only one object is required, this should be true
     * @param strict If true, if the selector returns <em>any</em> entity that
     *               is not of a type in the supported list, an
     *               {@link ArgumentParseException} will be thrown, else they
     *               will just be removed from the returned entities
     * @return The {@link ValueParameterModifier}
     */
    default ValueParameterModifier selector(boolean requireOne, boolean strict, Class<? extends Entity> supportedEntity) {
        return selector(Lists.newArrayList(supportedEntity), requireOne, strict);
    }

    /**
     * Specifies that the parameter could be satisfied by entities returned from
     * a selector instead.
     *
     * @param supportedEntities The {@link Class}es that represent the entities
     *                          that should be returned by the selector
     * @param requireOne If only one object is required, this should be true
     * @param strict If true, if the selector returns <em>any</em> entity that
     *               is not of a type in the supported list, an
     *               {@link ArgumentParseException} will be thrown, else they
     *               will just be removed from the returned entities
     * @return The {@link ValueParameterModifier}
     */
    ValueParameterModifier selector(Collection<Class<? extends Entity>> supportedEntities, boolean requireOne, boolean strict);

    /**
     * If after a {@link ValueParameter}'s execution there is no value stored
     * in the associated key, this modifier will put the supplied default into
     * the context.
     *
     * @param defaultValue The default value.
     * @return The {@link ValueParameterModifier}
     */
    ValueParameterModifier defaultValue(Object defaultValue);

    /**
     * If after a {@link ValueParameter}'s execution there is no value stored
     * in the associated key, this modifier will put the default obtained from
     * the supplied {@link Supplier} into the context, if the default is
     * non-null.
     *
     * @param defaultValueSupplier The default value {@link Supplier}.
     * @return The {@link ValueParameterModifier}
     */
    ValueParameterModifier defaultValueSupplier(Supplier<Object> defaultValueSupplier);

}
