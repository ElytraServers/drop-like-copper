package cn.elytra.mod.drop_like_copper.api.utils;

import cn.elytra.mod.drop_like_copper.api.delegate.CopperLootPoolSingletonContainer;
import cn.elytra.mod.drop_like_copper.mixins.LootPoolAccessor;
import com.google.common.collect.Streams;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;

import java.util.List;
import java.util.function.Function;

public class LootingUtils {

    private LootingUtils() {
    }

    /**
     * Get all {@link LootPoolSingletonContainer} including nested ones.
     *
     * @param pool the loot pool
     * @return the containers
     */
    public static List<CopperLootPoolSingletonContainer> getAllLootPoolSingletonContainer(LootPool pool) {
        return Streams.stream(LootPoolWalker.iterateEntries(
                ((LootPoolAccessor) pool).getEntries(),
                LootPoolSingletonContainer.class))
            .map(CopperLootPoolSingletonContainer::of)
            .toList();
    }

    /**
     * Get or lookup the loot table from the Either.
     *
     * @param tableEither the loot table either
     * @param context     the looting context
     * @return the loot table by the key or {@link LootTable#EMPTY}
     */
    public static LootTable getLootTableFromNested(Either<ResourceKey<LootTable>, LootTable> tableEither,
        LootContext context) {
        return tableEither.map(
            key -> context.getResolver().get(key).map(Holder::value).orElse(LootTable.EMPTY),
            Function.identity());
    }
}
