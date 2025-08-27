package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.impl.Translator
import ink.ptms.zaphkiel.item.meta.Meta
import org.bukkit.entity.Player
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.ItemTag

/**
 * 自定义 NBT
 */
@MetaKey("native")
class MetaNative(root: ConfigurationSection) : Meta(root) {

    val nativeTag = ItemTag().also { nbt ->
        root.getConfigurationSection("meta.native")?.let { section ->
            Translator.toItemTag(nbt, section)
        }
    }

    override val id: String
        get() = "native"

    override fun build(player: Player?, compound: ItemTag) {
        nativeTag.forEach { (t, u) -> compound[t] = u }
    }

    override fun toString(): String {
        return "MetaNative(nativeNBT=$nativeTag)"
    }
}