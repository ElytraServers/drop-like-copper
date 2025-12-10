package cn.elytra.mod.drop_like_copper.api.utils;

import cn.elytra.mod.drop_like_copper.mixins.CompositeEntryBaseAccessor;
import com.google.common.collect.AbstractIterator;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;

public class LootPoolWalker {

    public interface WalkResult {

        boolean terminal();

        /**
         * Continue the iterating.
         */
        WalkResult CONTINUE = new WalkResult() {

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public boolean terminal() {
                return false;
            }
        };

        /**
         * Terminate the interating.
         */
        WalkResult TERMINAL = new WalkResult() {

            @Override
            public int hashCode() {
                return 1;
            }

            @Override
            public boolean terminal() {
                return true;
            }
        };

        /**
         * Replace the current iterated value to {@link #newValue} and choose whether or not to continue by
         * {@link #terminal}.
         */
        record Replace(LootPoolEntryContainer newValue, boolean terminal) implements WalkResult {

        }

        static WalkResult replaceWith(LootPoolEntryContainer newValue, boolean terminal) {
            return new Replace(newValue, terminal);
        }
    }

    /**
     * Iterate the entries in the entries. The {@link CompositeEntryBase CompositedEntries} will be unfold and its inner
     * children will be also iterated.
     * <p>
     * The consumer returns {@link WalkResult} to choose whether or not to continue iterating.
     *
     * @param entries   the entry list from the loot pool
     * @param entryKind the entry class to iterate
     * @param consumer  the consumer to handle the entries
     * @param <T>       the entry class to iterate
     * @see cn.elytra.mod.drop_like_copper.api.delegate.CopperLootPool
     */
    @ApiStatus.Internal
    public static <T extends LootPoolEntryContainer> void walkEntries(List<LootPoolEntryContainer> entries,
        Class<T> entryKind, Function<T, WalkResult> consumer) {
        for (int i = 0; i < entries.size(); i++) {
            LootPoolEntryContainer entry = entries.get(i);
            if (entryKind.isAssignableFrom(entry.getClass())) {
                WalkResult result = consumer.apply(entryKind.cast(entry));
                if (result instanceof WalkResult.Replace replace) {
                    entries.set(i, replace.newValue());
                }
                if (result.terminal()) {
                    break;
                }
            }

            if (entry instanceof CompositeEntryBase composite) { // continue walking into deeper
                walkEntries(((CompositeEntryBaseAccessor) composite).getChildren(), entryKind, consumer);
            }
        }
    }

    @ApiStatus.Internal
    public static <T extends LootPoolEntryContainer> Iterator<T> iterateEntries(List<LootPoolEntryContainer> entries,
        Class<T> entryKind) {
        return new AbstractIterator<>() {

            private final Queue<LootPoolEntryContainer> queue = new LinkedList<>(entries);

            @Nullable
            @Override
            protected T computeNext() {
                while (!queue.isEmpty()) {
                    LootPoolEntryContainer entry = queue.poll();
                    if (entry instanceof CompositeEntryBase composite) {
                        queue.addAll(((CompositeEntryBaseAccessor) composite).getChildren());
                    }
                    if (entryKind.isAssignableFrom(entry.getClass())) {
                        return entryKind.cast(entry);
                    }
                }
                return endOfData();
            }
        };
    }

}
