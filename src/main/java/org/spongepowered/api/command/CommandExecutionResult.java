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
package org.spongepowered.api.command;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.ResettableBuilder;

import java.util.OptionalInt;

import javax.annotation.Nullable;

/**
 * Represents the result of a command in Sponge.
 */
public interface CommandExecutionResult {

    /**
     * Creates a builder that creates {@link CommandExecutionResult}s.
     *
     * @return The {@link Builder}
     */
    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    /**
     * Builds a result that indicates successes.
     *
     * @return The {@link CommandExecutionResult}
     */
    static CommandExecutionResult success() {
        return builder().successCount(1).build();
    }

    /**
     * Builds en empty result.
     *
     * @return The {@link CommandExecutionResult}
     */
    static CommandExecutionResult empty() {
        return builder().build();
    }

    /**
     * Returns a result indicating the command was processed with an
     * amount of affected blocks.
     *
     * @param count The amount of blocks affected
     * @return The result
     */
    static CommandExecutionResult affectedBlocks(int count) {
        return builder().affectedBlocks(count).build();
    }

    /**
     * Returns a result indicating the command was processed with an
     * amount of affected entities.
     *
     * @param count The amount of entities affected
     * @return The result
     */
    static CommandExecutionResult affectedEntities(int count) {
        return builder().affectedEntities(count).build();
    }

    /**
     * Returns a result indicating the command was processed with an
     * amount of affected items.
     *
     * @param count The amount of items affected
     * @return The result
     */
    static CommandExecutionResult affectedItems(int count) {
        return builder().affectedItems(count).build();
    }

    /**
     * Returns a result indicating the command was processed with an
     * amount of queries.
     *
     * @param count The amount of queries
     * @return The result
     */
    static CommandExecutionResult queryResult(int count) {
        return builder().queryResult(count).build();
    }

    /**
     * Gets the success count of the command.
     *
     * @return The success count of the command
     */
    OptionalInt successCount();

    /**
     * Gets the number of blocks affected by the command.
     *
     * @return The number of blocks affected by the command, if such a count
     *         exists
     */
    OptionalInt affectedBlocks();

    /**
     * Gets the number of entities affected by the command.
     *
     * @return The number of entities affected by the command, if such a count
     *         exists
     */
    OptionalInt affectedEntities();

    /**
     * Gets the number of items affected by the command.
     *
     * @return The number of items affected by the command, if such a count
     *         exists
     */
    OptionalInt affectedItems();

    /**
     * Gets the query result of the command, e.g. the time of the day,
     * an amount of money or a player's amount of XP.
     *
     * @return The query result of the command, if one exists
     */
    OptionalInt queryResult();

    /**
     * Builds {@link CommandExecutionResult}s
     */
    interface Builder extends ResettableBuilder<CommandExecutionResult, Builder> {

        /**
         * Sets if the command has been processed.
         *
         * @param successCount If the command has been processed
         * @return This builder, for chaining
         */
        Builder successCount(@Nullable Integer successCount);

        /**
         * Sets the amount of blocks affected by the command.
         *
         * @param affectedBlocks The amount of blocks affected by the command
         * @return This builder, for chaining
         */
        Builder affectedBlocks(@Nullable Integer affectedBlocks);

        /**
         * Sets the amount of entities affected by the command.
         *
         * @param affectedEntities The amount of entities affected by the
         *     command
         * @return This builder, for chaining
         */
        Builder affectedEntities(@Nullable Integer affectedEntities);

        /**
         * Sets the amount of items affected by the command.
         *
         * @param affectedItems The amount of items affected by the command
         * @return This builder, for chaining
         */
        Builder affectedItems(@Nullable Integer affectedItems);

        /**
         * Sets the query result of the command, e.g. the time of the day,
         * an amount of money or a player's amount of XP.
         *
         * @param queryResult The query result of the command
         * @return This builder, for chaining
         */
        Builder queryResult(@Nullable Integer queryResult);

        /**
         * Builds the {@link CommandExecutionResult}.
         *
         * @return A CommandResult with the specified settings
         */
        CommandExecutionResult build();

    }

}
