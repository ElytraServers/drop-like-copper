package cn.elytra.mod.drop_like_copper.api.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;

public class LootUtils {

    private LootUtils() {
    }

    private static <T> T deepCopy(T from, Codec<T> tCodec, RegistryAccess registryAccess) {
        // add registry info to JsonOps to prevent from "Can't access registry ResourceKey[...]"
        var ops = registryAccess.createSerializationContext(JsonOps.INSTANCE);
        var json = tCodec.encodeStart(ops, from).getOrThrow();
        return tCodec.decode(ops, json).getOrThrow().getFirst();
    }

    /**
     * Deep clone a LootTable by encoding then decoding it.
     *
     * @param from           the original loot table
     * @param registryAccess the registry access
     * @return the deep cloned table
     */
    public static LootTable clone(LootTable from, RegistryAccess registryAccess) {
        return deepCopy(from, LootTable.DIRECT_CODEC, registryAccess);
    }

    /**
     * Deep clone a LootPool by encoding then decoding it.
     *
     * @param from           the original loot pool
     * @param registryAccess the registry access
     * @return the deep cloned pool
     */
    public static LootPool clone(LootPool from, RegistryAccess registryAccess) {
        return deepCopy(from, LootPool.CODEC, registryAccess);
    }

}
