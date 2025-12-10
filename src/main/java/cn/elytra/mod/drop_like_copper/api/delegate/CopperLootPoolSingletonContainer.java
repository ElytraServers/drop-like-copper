package cn.elytra.mod.drop_like_copper.api.delegate;

import cn.elytra.mod.drop_like_copper.mixins.LootItemAccessor;
import cn.elytra.mod.drop_like_copper.mixins.LootPoolSingletonContainerAccessor;
import cn.elytra.mod.drop_like_copper.mixins.NestedLootTableAccessor;
import cn.elytra.mod.drop_like_copper.mixins.TagEntryAccessor;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class CopperLootPoolSingletonContainer implements LootPoolSingletonContainerAccessor {

    protected final LootPoolSingletonContainer delegate;
    protected final LootPoolSingletonContainerAccessor accessor;

    public CopperLootPoolSingletonContainer(LootPoolSingletonContainer delegate) {
        this.delegate = delegate;
        this.accessor = (LootPoolSingletonContainerAccessor) delegate;
    }

    public static CopperLootPoolSingletonContainer of(LootPoolSingletonContainer delegate) {
        return new CopperLootPoolSingletonContainer(delegate);
    }

    public LootPoolSingletonContainer getDelegate() {
        return delegate;
    }

    public boolean isEmptyLoot() {
        return delegate instanceof EmptyLootItem;
    }

    public Optional<Holder<Item>> getLootItem() {
        if (delegate instanceof LootItemAccessor lootItemAccessor) {
            return Optional.of(lootItemAccessor.getItem());
        }
        return Optional.empty();
    }

    public Optional<TagKey<Item>> getLootTag() {
        if (delegate instanceof TagEntryAccessor tagEntryAccessor) {
            return Optional.of(tagEntryAccessor.getTag());
        }
        return Optional.empty();
    }

    /**
     * Use {@link cn.elytra.mod.drop_like_copper.api.utils.LootingUtils#getLootTableFromNested(Either, LootContext)} to
     * get the loot table instance.
     */
    public Optional<Either<ResourceKey<LootTable>, LootTable>> getNestedLootTable() {
        if (delegate instanceof NestedLootTableAccessor nestedLootTableAccessor) {
            return Optional.of(nestedLootTableAccessor.getContents());
        }
        return Optional.empty();
    }

    /**
     * Make {@link #getFunctions()} mutable.
     * <p>
     * Note that when you change functions, you must call {@link #refreshCompositeFunction()} to make them actually work.
     *
     * @see #modifyFunctions(Consumer)
     */
    public List<LootItemFunction> mutateFunctions() {
        if (!(getFunctions() instanceof ArrayList<LootItemFunction>)) {
            setFunctions(new ArrayList<>(getFunctions()));
        }
        return getFunctions();
    }

    /**
     * Update {@link #getCompositeFunction()} with latest {@link #getFunctions()}.
     *
     * @see #modifyFunctions(Consumer)
     */
    public void refreshCompositeFunction() {
        setCompositeFunction(LootItemFunctions.compose(getFunctions()));
    }

    public void modifyFunctions(Consumer<List<LootItemFunction>> consumer) {
        consumer.accept(mutateFunctions());
        refreshCompositeFunction();
    }

    @Override
    public int getWeight() {
        return accessor.getWeight();
    }

    @Override
    public void setWeight(int value) {
        accessor.setWeight(value);
    }

    @Override
    public int getQuality() {
        return accessor.getQuality();
    }

    @Override
    public void setQuality(int value) {
        accessor.setQuality(value);
    }

    @Override
    public List<LootItemFunction> getFunctions() {
        return accessor.getFunctions();
    }

    @Override
    public void setFunctions(List<LootItemFunction> functions) {
        accessor.setFunctions(functions);
    }

    @Override
    public BiFunction<ItemStack, LootContext, ItemStack> getCompositeFunction() {
        return accessor.getCompositeFunction();
    }

    @Override
    public void setCompositeFunction(BiFunction<ItemStack, LootContext, ItemStack> compositeFunction) {
        accessor.setCompositeFunction(compositeFunction);
    }
}
