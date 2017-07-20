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
package org.spongepowered.api.command.parameters.spec.impl;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.parameters.CommandContext;
import org.spongepowered.api.command.parameters.ArgumentParseException;
import org.spongepowered.api.command.parameters.spec.ValueParameter;
import org.spongepowered.api.command.parameters.tokens.CommandArgs;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.selector.Selector;

import java.util.Optional;
import java.util.Set;

import static org.spongepowered.api.util.SpongeApiTranslationHelper.t;

/**
 * Abstract {@link ValueParameter} that matches values based on a {@link Selector}.
 */
public abstract class SelectorValueParameter extends PatternMatchingValueParameter {

    private final Class<? extends Entity> entityTarget;

    public SelectorValueParameter(Class<? extends Entity> entityTarget) {
        this.entityTarget = entityTarget;
    }

    @Override
    public Optional<Object> getValue(CommandSource source, CommandArgs args, CommandContext context)
            throws ArgumentParseException {
        String arg = args.peek();
        if (arg.startsWith("@")) { // Possibly a selector
            try {
                Set<Entity> entities = Selector.parse(args.next()).resolve(source);
                if (!entities.stream().allMatch(this.entityTarget::isInstance)) {
                    throw args.createError(t("The selector returned entities that are not valid for this argument."));
                }

                // No need to cast in this case.
                return Optional.of(entities);
            } catch (IllegalArgumentException ex) {
                throw args.createError(Text.of(ex.getMessage()));
            }
        }

        return super.getValue(source, args, context);
    }

}
