package cn.elytra.mod.drop_like_copper.mixins;

import cn.elytra.mod.drop_like_copper.api.LootingEvent;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(LootTable.class)
public class LootTableMixin {

    @Inject(
        order = 500,
        method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/loot/LootPool;addRandomItems(Ljava/util/function/Consumer;Lnet/minecraft/world/level/storage/loot/LootContext;)V"),
        cancellable = true)
    private void beforeLootPool(LootContext context, Consumer<ItemStack> output, CallbackInfo ci,
        @Local LootPool lootPool) {
        LootTable lootTable = (LootTable) (Object) this;
        LootingEvent.BeforeLootingPool event = NeoForge.EVENT_BUS.post(new LootingEvent.BeforeLootingPool(lootTable, lootPool));
        if (event.isCanceled()) ci.cancel();
    }

    @ModifyReceiver(
        method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/loot/LootPool;addRandomItems(Ljava/util/function/Consumer;Lnet/minecraft/world/level/storage/loot/LootContext;)V"))
    private LootPool replaceLootPool(LootPool lootPool, Consumer<ItemStack> consumer, LootContext context) {
        LootTable lootTable = (LootTable) (Object) this;
        return NeoForge.EVENT_BUS.post(new LootingEvent.LootingPool(lootTable, lootPool, context)).getResultPool();
    }

}
