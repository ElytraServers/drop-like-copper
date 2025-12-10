package cn.elytra.mod.drop_like_copper.mixins;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.BiFunction;

@Mixin(LootPoolSingletonContainer.class)
public interface LootPoolSingletonContainerAccessor {

    @Accessor("weight")
    int getWeight();

    @Accessor("weight")
    @Mutable
    void setWeight(int value);

    @Accessor("quality")
    int getQuality();

    @Accessor("quality")
    @Mutable
    void setQuality(int value);

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
