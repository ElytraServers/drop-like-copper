package cn.elytra.mod.drop_like_copper;

import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(DropLikeCopperMod.ID)
public class DropLikeCopperMod {

    public static final String ID = "drop_like_copper";

    public static final Logger LOG = LoggerFactory.getLogger(DropLikeCopperMod.class);

    public DropLikeCopperMod() {
        LOG.info("Drop...");
    }

}
