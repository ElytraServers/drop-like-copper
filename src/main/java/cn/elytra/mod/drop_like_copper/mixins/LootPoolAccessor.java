package cn.elytra.mod.drop_like_copper.mixins;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@Mixin(LootPool.class)
public interface LootPoolAccessor {

    @Accessor("entries")
    List<LootPoolEntryContainer> getEntries();

    @Accessor("entries")
    @Mutable
    void setEntries(List<LootPoolEntryContainer> entries);

    @Accessor("conditions")
    List<LootItemCondition> getConditions();

    @Accessor("conditions")
    @Mutable
    void setConditions(List<LootItemCondition> conditions);

    @Accessor("compositeCondition")
    Predicate<LootContext> getCompositeCondition();

    @Accessor("compositeCondition")
    @Mutable
    void setCompositeCondition(Predicate<LootContext> compositeCondition);

    @Accessor("functions")
    List<LootItemFunction> getFunctions();

    @Accessor("functions")
    @Mutable
    void setFunctions(List<LootItemFunction> functions);

    @Accessor("compositeFunction")
    BiFunction<ItemStack, LootContext, ItemStack> getCompositeFunction();

    @Accessor("compositeFunction")
    @Mutable
    void setCompositeFunction(BiFunction<ItemStack, LootContext, ItemStack> compositeFunction);

}
