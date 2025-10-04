package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.item.meta.Meta
import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.ItemMeta
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.MinecraftVersion

@MetaKey("item-model")
class MetaItemModel(root: ConfigurationSection) : Meta(root) {

    private val modelId: String? = root.getString("meta.item-model")

    override val id: String
        get() = "item-model"

    override fun build(itemMeta: ItemMeta) {
        if (MinecraftVersion.versionId < 12102) return
        val key = modelId?.trim()?.let { NamespacedKey.fromString(it) }
        itemMeta.itemModel = key
    }

    override fun drop(itemMeta: ItemMeta) {
        if (MinecraftVersion.versionId < 12102) return
        itemMeta.itemModel = null
    }

    override fun toString(): String {
        return "MetaItemModel(modelId=$modelId)"
    }
}


