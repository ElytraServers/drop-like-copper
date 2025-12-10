package cn.elytra.mod.drop_like_copper.mixins;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootPool.Builder.class)
public interface LootPoolBuilderAccessor {

    @Accessor("entries")
    ImmutableList.Builder<LootPoolEntryContainer> getEntries();

    @Accessor("entries")
    @Mutable
    void setEntries(ImmutableList.Builder<LootPoolEntryContainer> value);

    @Accessor("conditions")
    ImmutableList.Builder<LootItemCondition> getConditions();

    @Accessor("conditions")
    @Mutable
    void setConditions(ImmutableList.Builder<LootItemCondition> value);

    @Accessor("functions")
    ImmutableList.Builder<LootItemFunction> getFunctions();

    @Accessor("functions")
    @Mutable
    void setFunctions(ImmutableList.Builder<LootItemFunction> value);

}
