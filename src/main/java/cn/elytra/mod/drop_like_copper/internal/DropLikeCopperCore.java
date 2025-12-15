package cn.elytra.mod.drop_like_copper.internal;

import cn.elytra.mod.drop_like_copper.api.DropLikeCopperAPI;
import cn.elytra.mod.drop_like_copper.api.LootingEvent;
import cn.elytra.mod.drop_like_copper.api.delegate.CopperLootPool;
import cn.elytra.mod.drop_like_copper.api.delegate.CopperLootPoolSingletonContainer;
import cn.elytra.mod.drop_like_copper.api.utils.LootPoolWalker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

@EventBusSubscriber
public class DropLikeCopperCore {

    private static final Logger LOG = LoggerFactory.getLogger(DropLikeCopperCore.class);
    private static final UniformGenerator COPPER_UG = UniformGenerator.between(2.0F, 5.0F);

    @SubscribeEvent
    public static void onLootingPool(LootingEvent.LootingPool event) {
        // ignore when in lib mode
        if (DropLikeCopperAPI.isLibraryMode()) return;
        // check if the block is ore
        BlockState blockState = event.getLootContext().getParamOrNull(LootContextParams.BLOCK_STATE);
        if (blockState != null && blockState.is(Tags.Blocks.ORES)) {
            CopperLootPool.of(event.setResultPoolCloned()).walkAllSingletonEntries(c -> {
                modifyOreDrops(c);
                return LootPoolWalker.WalkResult.CONTINUE;
            });
        }
    }

    private static void modifyOreDrops(CopperLootPoolSingletonContainer c) {
        if (c.getLootItem().map(holder -> holder.is(Tags.Items.RAW_MATERIALS)).orElse(false)) {
            c.modifyFunctions(functions -> {
                AtomicBoolean found = new AtomicBoolean();
                // replace the set_count in functions
                functions.replaceAll(func -> {
                    if (func instanceof SetItemCountFunction) {
                        found.set(true);
                        return SetItemCountFunction.setCount(COPPER_UG).build();
                    } else {
                        return func;
                    }
                });
                // if not found, add one to the top of the functions
                if (!found.get()) {
                    functions.addFirst(SetItemCountFunction.setCount(COPPER_UG).build());
                }
            });
        }
    }
}
