package skywolf46.mestapi.forge

import net.minecraftforge.fml.common.Mod
import skywolf46.mestapi.forge.MESTAPIForge.Companion.MODID
import java.util.logging.LogManager
import java.util.logging.Logger

@Mod(modid = MODID)
class MESTAPIForge {
    companion object {
        const val MODID = "MESTAPI"

        val LOGGER: Logger = LogManager.getLogManager().getLogger(MODID)
    }

    init {

    }


}