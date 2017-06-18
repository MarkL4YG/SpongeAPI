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
package org.spongepowered.api.command.specification;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.ResettableBuilder;

import java.util.Optional;

/**
 * Represents the result from a command.
 */
public interface CommandResult {

    /**
     * Returns a {@link CommandResult.Builder}.
     *
     * @return A new command result builder
     */
    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    /**
     * Returns a new {@link org.spongepowered.api.command.CommandResult} indicating that a command was
     * processed.
     *
     * @return The command result
     */
    static CommandResult empty() {
        return builder().build();
    }

    /**
     * Returns a result indicating the command was processed with a single
     * success.
     *
     * @return The result
     */
    static CommandResult success() {
        return builder().successCount(1).build();
    }

    /**
     * Returns a result indicating the command was processed with a single
     * success.
     *
     * @param count The success count
     * @return The result
     */
    static CommandResult successCount(int count) {
        return builder().successCount(count).build();
    }

    /**
     * Returns a result indicating the command was processed with an
     * amount of affected blocks.
     *
     * @param count The amount of blocks affected
     * @return The result
     */
    static CommandResult affectedBlocks(int count) {
        return builder().affectedBlocks(count).build();
    }

    /**
     * Returns a result indicating the command was processed with an
     * amount of affected entities.
     *
     * @param count The amount of entities affected
     * @return The result
     */
    static CommandResult affectedEntities(int count) {
        return builder().affectedEntities(count).build();
    }

    /**
     * Returns a result indicating the command was processed with an
     * amount of affected items.
     *
     * @param count The amount of items affected
     * @return The result
     */
    static CommandResult affectedItems(int count) {
        return builder().affectedItems(count).build();
    }

    /**
     * Returns a result indicating the command was processed with an
     * amount of queries.
     *
     * @param count The amount of queries
     * @return The result
     */
    static CommandResult queryResult(int count) {
        return builder().queryResult(count).build();
    }

    /**
     * Gets the success count of the command.
     *
     * @return The success count of the command
     */
    Optional<Integer> getSuccessCount();

    /**
     * Gets the number of blocks affected by the command.
     *
     * @return The number of blocks affected by the command, if such a count
     *         exists
     */
    Optional<Integer> getAffectedBlocks();

    /**
     * Gets the number of entities affected by the command.
     *
     * @return The number of entities affected by the command, if such a count
     *         exists
     */
    Optional<Integer> getAffectedEntities();

    /**
     * Gets the number of items affected by the command.
     *
     * @return The number of items affected by the command, if such a count
     *         exists
     */
    Optional<Integer> getAffectedItems();

    /**
     * Gets the query result of the command, e.g. the time of the day,
     * an amount of money or a player's amount of XP.
     *
     * @return The query result of the command, if one exists
     */
    Optional<Integer> getQueryResult();

    /**
     * Builds a {@link CommandResult}
     */
    interface Builder extends ResettableBuilder<CommandResult, Builder> {

        /**
         * Sets if the command has been processed.
         *
         * @param successCount If the command has been processed
         * @return This builder, for chaining
         */
        Builder successCount(int successCount);

        /**
         * Sets the amount of blocks affected by the command.
         *
         * @param affectedBlocks The amount of blocks affected by the command
         * @return This builder, for chaining
         */
        Builder affectedBlocks(int affectedBlocks);

        /**
         * Sets the amount of entities affected by the command.
         *
         * @param affectedEntities The amount of entities affected by the
         *     command
         * @return This builder, for chaining
         */
        Builder affectedEntities(int affectedEntities);

        /**
         * Sets the amount of items affected by the command.
         *
         * @param affectedItems The amount of items affected by the command
         * @return This builder, for chaining
         */
        Builder affectedItems(int affectedItems);

        /**
         * Sets the query result of the command, e.g. the time of the day,
         * an amount of money or a player's amount of XP.
         *
         * @param queryResult The query result of the command
         * @return This builder, for chaining
         */
        Builder queryResult(int queryResult);

        /**
         * Builds the {@link CommandResult}
         *
         * @return The built {@link CommandResult}
         */
        CommandResult build();

    }

}
