package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.item.meta.Meta
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.util.asList
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.*

@MetaKey("can-place-on")
class MetaCanPlaceOn(root: ConfigurationSection) : Meta(root) {

    val canPlaceOn = root["meta.can-place-on"]?.asList()

    override val id: String
        get() = "can-place-on"

    override fun build(player: Player?, compound: ItemTag) {
        if (MinecraftVersion.versionId < 12005) {
            if (canPlaceOn == null || compound.containsKey("CanPlaceOn")) {
                return
            }
            compound.putDeep("CanPlaceOn", ItemTagList.of(*canPlaceOn.toTypedArray()))
        }
    }

    override fun drop(player: Player?, compound: ItemTag) {
        compound.remove("CanPlaceOn")
    }

    override fun toString(): String {
        return "MetaCanPlaceOn(canPlaceOn=$canPlaceOn)"
    }

    override fun build(itemStack: ItemStack): ItemStack {
        if (canPlaceOn == null || MinecraftVersion.versionId < 12005) {
            return itemStack
        }
        return itemStack.setItemCanPlaceOn(canPlaceOn)
    }

    override fun drop(itemStack: ItemStack): ItemStack {
        if (MinecraftVersion.versionId < 12005) return itemStack
        return itemStack.removeItemCanPlaceOn()
    }

}