package cn.elytra.mod.drop_like_copper.api.delegate;

import cn.elytra.mod.drop_like_copper.api.utils.LootPoolWalker;
import cn.elytra.mod.drop_like_copper.api.utils.Utils;
import cn.elytra.mod.drop_like_copper.mixins.LootPoolAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class CopperLootPool implements LootPoolAccessor {

    protected final LootPool delegate;
    protected final LootPoolAccessor accessor; // delegate LootPoolAccessor

    public CopperLootPool(LootPool delegate) {
        this.delegate = delegate;
        this.accessor = (LootPoolAccessor) delegate;
    }

    public static CopperLootPool of(LootPool delegate) {
        return new CopperLootPool(delegate);
    }

    public LootPool getDelegate() {
        return delegate;
    }

    public void walkAllEntries(Function<LootPoolEntryContainer, LootPoolWalker.WalkResult> consumer) {
        LootPoolWalker.walkEntries(getEntries(), LootPoolEntryContainer.class, consumer);
    }

    public void walkAllSingletonEntries(
        Function<CopperLootPoolSingletonContainer, LootPoolWalker.WalkResult> consumer) {
        LootPoolWalker.walkEntries(
            getEntries(),
            LootPoolSingletonContainer.class,
            Utils.transformFunctionInputType(consumer, CopperLootPoolSingletonContainer::of));
    }

    @Override
    public List<LootPoolEntryContainer> getEntries() {
        return accessor.getEntries();
    }

    @Override
    public void setEntries(List<LootPoolEntryContainer> entries) {
        accessor.setEntries(entries);
    }

    @Override
    public List<LootItemCondition> getConditions() {
        return accessor.getConditions();
    }

    @Override
    public void setConditions(List<LootItemCondition> conditions) {
        accessor.setConditions(conditions);
    }

    @Override
    public Predicate<LootContext> getCompositeCondition() {
        return accessor.getCompositeCondition();
    }

    @Override
    public void setCompositeCondition(Predicate<LootContext> compositeCondition) {
        accessor.setCompositeCondition(compositeCondition);
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
