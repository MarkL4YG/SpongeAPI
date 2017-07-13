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
package org.spongepowered.api.command.parameters.flags;

import org.spongepowered.api.CatalogType;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.parameters.CommandExecutionContext;
import org.spongepowered.api.command.parameters.ArgumentParseException;
import org.spongepowered.api.command.parameters.tokens.TokenizedArgs;
import org.spongepowered.api.util.annotation.CatalogedBy;

/**
 * An {@link UnknownFlagBehavior} specifies what should happen if what appears
 * to be a flag to the flag parser cannot be parsed.
 */
@CatalogedBy(UnknownFlagBehaviors.class)
public interface UnknownFlagBehavior extends CatalogType {

    /**
     * Parses the unknown flag. the {@link TokenizedArgs} and
     * {@link CommandExecutionContext} are in the post flag-parse position, but
     * this can be reverted with the provided states.
     *
     * <p>This method should leave/put the {@link TokenizedArgs} and
     * {@link CommandExecutionContext} in the state ready to parse the next
     * element.</p>
     *
     * @param source The {@link CommandSource} that executed the command
     * @param args The {@link TokenizedArgs}
     * @param context The {@link CommandExecutionContext}
     * @param tokenizedArgsPreviousState The previous {@link TokenizedArgs}
     *                                   state
     * @param contextPreviousState The previous {@link CommandExecutionContext}
     *                             state
     * @param flag
     * @throws ArgumentParseException thrown if there is an issue parsing the
     *                                 argument
     */
    void parse(CommandSource source, TokenizedArgs args, CommandExecutionContext context, Object tokenizedArgsPreviousState,
            Object contextPreviousState, String flag) throws ArgumentParseException;
}
