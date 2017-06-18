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
 * Class containing common {@link CatalogedValueParameters}s.
 */
public final class CatalogedValueParameters {

    private CatalogedValueParameters() {}

    // SORTFIELDS:ON

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
     * <p>Returns a {@link boolean}</p>
     */
    public final static CatalogedValueParameter BOOLEAN = DummyObjectProvider.createFor(CatalogedValueParameter.class, "BOOLEAN");

    /**
     * Expect an argument to represent a dimension.
     *
     * <p>Returns a {@link Dimension}</p>
     */
    public final static CatalogedValueParameter DIMENSION = DummyObjectProvider.createFor(CatalogedValueParameter.class, "DIMENSION");

    /**
     * Require an argument to be an double-precision floating point number.
     *
     * <p>Returns a {@link double}</p>
     */
    public final static CatalogedValueParameter DOUBLE = DummyObjectProvider.createFor(CatalogedValueParameter.class, "DOUBLE");

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
    public final static CatalogedValueParameter DURATION = DummyObjectProvider.createFor(CatalogedValueParameter.class, "DURATION");

    /**
     * TODO: This
     *
     * <p>This parameter accepts selectors.</p>
     *
     * <p>Returns an {@link Entity}.</p>
     */
    public final static CatalogedValueParameter ENTITY = DummyObjectProvider.createFor(CatalogedValueParameter.class, "ENTITY");

    /**
     * TODO: This
     *
     * <p>This parameter accepts selectors.</p>
     *
     * <p>Returns an {@link Entity}.</p>
     */
    public final static CatalogedValueParameter ENTITY_OR_SOURCE =
            DummyObjectProvider.createFor(CatalogedValueParameter.class, "ENTITY_OR_SOURCE");

    /**
     * Require an argument to be an integer (base 10) number.
     *
     * <p>Returns an {@link Integer}.</p>
     */
    public final static CatalogedValueParameter INTEGER = DummyObjectProvider.createFor(CatalogedValueParameter.class, "INTEGER");

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
    public final static CatalogedValueParameter LOCATION = DummyObjectProvider.createFor(CatalogedValueParameter.class, "LOCATION");

    /**
     * Require an argument to be a long (base 10).
     *
     * <p>Returns a {@link long}.</p>
     */
    public final static CatalogedValueParameter LONG = DummyObjectProvider.createFor(CatalogedValueParameter.class, "LONG");

    /**
     * Does not parse any arguments, returning nothing.
     *
     * <p>Returns nothing - no entry will be placed into any provided key.</p>
     */
    public final static CatalogedValueParameter NONE = DummyObjectProvider.createFor(CatalogedValueParameter.class, "NONE");

    /**
     * Expect an argument to represent an online player.
     *
     * <p>This parameter accepts selectors.</p>
     *
     * <p>Returns a {@link Player}.</p>
     */
    public final static CatalogedValueParameter PLAYER = DummyObjectProvider.createFor(CatalogedValueParameter.class, "PLAYER");

    /**
     * Expect an argument to represent an online player, or if nothing matches
     * and the calling {@link CommandSource} is a {@link Player}, returns the
     * calling player.
     *
     * <p>This parameter accepts selectors.</p>
     *
     * <p>Returns a {@link Player}.</p>
     */
    public final static CatalogedValueParameter PLAYER_OR_SOURCE =
            DummyObjectProvider.createFor(CatalogedValueParameter.class, "PLAYER_OR_SOURCE");

    /**
     * Expect an argument to represent a {@link PluginContainer}'s id.
     *
     * <p>Returns a {@link PluginContainer}</p>
     */
    public final static CatalogedValueParameter PLUGIN =
            DummyObjectProvider.createFor(CatalogedValueParameter.class, "PLAYER_OR_SOURCE");

    /**
     * Require one or more strings, without any processing, which are combined
     * into a single, space-separated string.
     *
     * <p>Returns a {@link String}.</p>
     */
    public final static CatalogedValueParameter REMAINING_JOINED_STRINGS =
            DummyObjectProvider.createFor(CatalogedValueParameter.class, "REMAINING_JOINED_STRINGS");

    /**
     * Require one or more strings, which are combined into a single,
     * space-separated string.
     *
     * <p>Returns a {@link String}.</p>
     */
    public final static CatalogedValueParameter REMAINING_RAW_JOINED_STRINGS =
            DummyObjectProvider.createFor(CatalogedValueParameter.class, "REMAINING_RAW_JOINED_STRINGS");

    /**
     * Require an argument to be a string.
     *
     * <p>Returns a {@link String}.</p>
     */
    public final static CatalogedValueParameter STRING = DummyObjectProvider.createFor(CatalogedValueParameter.class, "STRING");

    /**
     * Expect an argument to represent a player who has been online at some
     * point, as a {@link User}.
     *
     * <p>This parameter accepts selectors (to obtain players).</p>
     *
     * <p>Returns a {@link User}.</p>
     */
    public final static CatalogedValueParameter USER = DummyObjectProvider.createFor(CatalogedValueParameter.class, "USER");

    /**
     * Expect an argument to represent a player who has been online at some
     * point, as a {@link User}. If nothing matches and the calling
     * {@link CommandSource} is a {@link User}, return the caller.
     *
     * <p>This parameter accepts selectors (to obtain players).</p>
     *
     * <p>Returns a {@link User}.</p>
     */
    public final static CatalogedValueParameter USER_OR_SOURCE =
            DummyObjectProvider.createFor(CatalogedValueParameter.class, "USER_OR_SOURCE");

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
    public final static CatalogedValueParameter VECTOR3D = DummyObjectProvider.createFor(CatalogedValueParameter.class, "VECTOR3D");

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
    public final static CatalogedValueParameter WORLD_PROPERTIES = DummyObjectProvider.createFor(CatalogedValueParameter.class, "WORLD_PROPERTIES");

    // SORTFIELDS:OFF

}
