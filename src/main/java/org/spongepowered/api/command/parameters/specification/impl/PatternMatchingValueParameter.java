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
package org.spongepowered.api.command.parameters.specification.impl;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.parameters.CommandExecutionContext;
import org.spongepowered.api.command.parameters.ParameterParseException;
import org.spongepowered.api.command.parameters.specification.ValueParameter;
import org.spongepowered.api.command.parameters.tokens.TokenizedArgs;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.spongepowered.api.util.SpongeApiTranslationHelper.t;

/**
 * Abstract {@link ValueParameter} that matches values based on pattern.
 */
public abstract class PatternMatchingValueParameter implements ValueParameter {

    @Override
    public List<String> complete(CommandSource source, TokenizedArgs args, CommandExecutionContext context) throws ParameterParseException {
        Iterable<String> choices = getChoices(source);
        final Optional<String> nextArg = args.nextIfPresent();
        if (nextArg.isPresent()) {
            choices = StreamSupport.stream(choices.spliterator(), false)
                    .filter(input -> getFormattedPattern(nextArg.get()).matcher(input).find())
                    .collect(Collectors.toList());
        }
        return ImmutableList.copyOf(choices);
    }

    @Override
    public Optional<Object> getValue(CommandSource source, TokenizedArgs args, CommandExecutionContext context)
            throws ParameterParseException {
        final String unformattedPattern = args.next();
        return Optional.of(getValue(source, args, unformattedPattern));
    }

    public Collection<Object> getValue(CommandSource source, TokenizedArgs args, String unformattedPattern) throws ParameterParseException {
        Pattern pattern = getFormattedPattern(unformattedPattern);
        Iterable<String> filteredChoices =
                StreamSupport.stream(getChoices(source).spliterator(), false).filter(element -> pattern.matcher(element).find())
                        .collect(Collectors.toList());
        for (String el : filteredChoices) { // Match a single value
            if (el.equalsIgnoreCase(unformattedPattern)) {
                return Collections.singleton(getValue(el));
            }
        }
        Collection<Object> ret = StreamSupport.stream(filteredChoices.spliterator(), false).map(this::getValue).collect(Collectors.toList());

        if (!ret.iterator().hasNext()) {
            throw args.createError(t("No values matching pattern '%s' present!", unformattedPattern));
        }
        return ret;
    }

    protected Pattern getFormattedPattern(String input) {
        if (!input.startsWith("^")) { // Anchor matches to the beginning -- this lets us use find()
            input = "^" + input;
        }
        return Pattern.compile(input, Pattern.CASE_INSENSITIVE);

    }

    /**
     * Gets the available choices for this command source.
     *
     * @param source The source requesting choices
     * @return the possible choices
     */
    protected abstract Iterable<String> getChoices(CommandSource source);

    /**
     * Gets the value for a given choice. For any result in
     * {@link #getChoices(CommandSource)}, this must return a non-null value.
     * Otherwise, an {@link IllegalArgumentException} may be thrown.
     *
     * @param choice The specified choice
     * @return the choice's value
     * @throws IllegalArgumentException if the input string is not any return
     *         value of {@link #getChoices(CommandSource)}
     */
    protected abstract Object getValue(String choice) throws IllegalArgumentException;

}
