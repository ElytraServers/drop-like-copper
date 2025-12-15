package cn.elytra.mod.drop_like_copper.api;

import cn.elytra.mod.drop_like_copper.api.delegate.CopperLootPoolBuilder;
import cn.elytra.mod.drop_like_copper.api.utils.LootUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

import java.util.function.Consumer;

public abstract class LootingEvent extends Event {

    private final LootTable table;

    public LootingEvent(LootTable table) {
        this.table = table;
    }

    public LootTable getTable() {
        return table;
    }

    /**
     * This event is fired before looting a pool in {@link LootTable#getRandomItemsRaw(LootContext, Consumer)}.
     * <p>
     * Cancelling this event will prevent from looting this pool.
     */
    @SuppressWarnings("deprecation")
    public static class BeforeLootingPool extends LootingEvent implements ICancellableEvent {

        /**
         * The looting pool.
         */
        private final LootPool pool;

        public BeforeLootingPool(LootTable table, LootPool pool) {
            super(table);
            this.pool = pool;
        }

        public LootPool getPool() {
            return pool;
        }
    }

    /**
     * This event is fired during looting the pools iteration in
     * {@link LootTable#getRandomItemsRaw(LootContext, Consumer)}.
     */
    @SuppressWarnings("deprecation")
    public static class LootingPool extends LootingEvent {

        /**
         * The original delegate to roll the loots.
         */
        private final LootPool originalPool;

        /**
         * The loot context.
         */
        private final LootContext lootContext;

        /**
         * The final delegate used to roll the loots.
         */
        private LootPool resultPool;

        public LootingPool(LootTable table, LootPool originalPool, LootContext lootContext) {
            super(table);
            this.originalPool = originalPool;
            this.lootContext = lootContext;
            this.resultPool = originalPool;
        }

        public LootPool getOriginalPool() {
            return originalPool;
        }

        public LootContext getLootContext() {
            return lootContext;
        }

        public LootPool getResultPool() {
            return resultPool;
        }

        public void setResultPool(LootPool resultPool) {
            this.resultPool = resultPool;
        }

        /**
         * Make {@link #getResultPool()} cloned, so that any changes directly to the pool won't affect the next roll.
         *
         * @return the cloned result pool
         */
        public LootPool setResultPoolCloned() {
            this.resultPool = LootUtils.clone(resultPool, getRegistryAccess());
            return resultPool;
        }

        /**
         * Modify the executing loot delegate.
         * <p>
         * A builder with copied data (entries, conditions, functions) from the existing delegate that triggered the
         * event will be passed to the consumer. It's safe to operate on the builder, but not the included existing
         * elements, because they're not deep cloned.
         * <p>
         * The result of the builder will be used to replaced the {@link #resultPool}, and as the final result of the
         * delegate that used to loot.
         *
         * @param builderConsumer the builder consumer
         */
        public void modifyResultPool(Consumer<CopperLootPoolBuilder> builderConsumer) {
            CopperLootPoolBuilder builder = CopperLootPoolBuilder.fromExisting(resultPool);
            builderConsumer.accept(builder);
            this.resultPool = builder.build();
        }

        public RegistryAccess.Frozen getRegistryAccess() {
            return getLootContext().getLevel().getServer().registryAccess();
        }
    }

}
