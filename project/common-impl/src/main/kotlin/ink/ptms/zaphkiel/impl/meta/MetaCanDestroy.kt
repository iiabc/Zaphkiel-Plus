package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.item.meta.Meta
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.util.asList
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.*

@MetaKey("can-destroy")
class MetaCanDestroy(root: ConfigurationSection) : Meta(root) {

    val canDestroy = root["meta.can-destroy"]?.asList()

    override val id: String
        get() = "can-destroy"

    override fun build(player: Player?, compound: ItemTag) {
        if (MinecraftVersion.versionId < 12005) {
            if (canDestroy == null || compound.containsKey("CanDestroy")) {
                return
            }
            compound.putDeep("CanDestroy", ItemTagList.of(*canDestroy.toTypedArray()))
        }
    }

    override fun drop(player: Player?, compound: ItemTag) {
        compound.remove("CanDestroy")
    }

    override fun toString(): String {
        return "MetaCanDestroy(canDestroy=$canDestroy)"
    }

    override fun build(itemStack: ItemStack): ItemStack {
        if (canDestroy == null || MinecraftVersion.versionId < 12005) {
            return itemStack
        }
        return itemStack.setItemCanBreak(canDestroy)
    }

    override fun drop(itemStack: ItemStack): ItemStack {
        if (MinecraftVersion.versionId < 12005) return itemStack
        return itemStack.removeItemCanBreak()
    }

}