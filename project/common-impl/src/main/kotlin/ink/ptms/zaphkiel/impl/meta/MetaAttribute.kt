package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.item.meta.Meta
import ink.ptms.zaphkiel.api.Attributes
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.util.NumberConversions
import taboolib.common5.Coerce
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.xseries.XAttribute
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.ItemTagList
import taboolib.module.nms.MinecraftVersion
import taboolib.type.BukkitEquipment
import java.util.*

@MetaKey("attribute")
class MetaAttribute(root: ConfigurationSection) : Meta(root) {

    val attributeListLegacy = ItemTagList()
    val attributeList = ArrayList<Pair<org.bukkit.attribute.Attribute, AttributeModifier>>()

    init {
        root.getConfigurationSection("meta.attribute")?.getKeys(false)?.forEach { hand ->
            root.getConfigurationSection("meta.attribute.$hand")!!.getKeys(false).forEach { name ->
                val xAttribute = XAttribute.of(name).orElse(null)
                if (xAttribute != null && xAttribute.isSupported) {
                    val bukkitAttribute = xAttribute.get()
                    if (bukkitAttribute != null) {
                        if (MinecraftVersion.majorLegacy >= 11600) {
                            var equipmentSlot: EquipmentSlot? = null
                            if (hand != "all") {
                                equipmentSlot = BukkitEquipment.fromString(hand)?.bukkit
                            }
                            val amount: Double
                            val operation: AttributeModifier.Operation
                            val attributeValue = root.getString("meta.attribute.$hand.$name")!!
                            if (attributeValue.endsWith("%")) {
                                amount = Coerce.toDouble(attributeValue.substring(0, attributeValue.length - 1)) / 100.0
                                operation = AttributeModifier.Operation.ADD_SCALAR
                            } else {
                                amount = Coerce.toDouble(attributeValue)
                                operation = AttributeModifier.Operation.ADD_NUMBER
                            }
                            val modifier = Attributes.createAttributeModifier("zaphkiel", amount, operation, equipmentSlot)
                            attributeList.add(bukkitAttribute to modifier)
                        } else {
                            try {
                                val uuid = UUID.randomUUID()
                                val attribute = ItemTag()
                                val attributeValue = root.getString("meta.attribute.$hand.$name")!!
                                if (attributeValue.endsWith("%")) {
                                    attribute["Amount"] = ItemTagData(
                                        NumberConversions.toDouble(
                                            attributeValue.dropLast(1)
                                        ) / 100.0
                                    )
                                    attribute["Operation"] = ItemTagData(1)
                                } else {
                                    attribute["Amount"] = ItemTagData(NumberConversions.toDouble(attributeValue))
                                    attribute["Operation"] = ItemTagData(0)
                                }
                                attribute["AttributeName"] = ItemTagData(bukkitAttribute.key.toString())
                                attribute["UUIDMost"] = ItemTagData(uuid.mostSignificantBits)
                                attribute["UUIDLeast"] = ItemTagData(uuid.leastSignificantBits)
                                attribute["Name"] = ItemTagData(bukkitAttribute.key.toString())
                                if (hand != "all") {
                                    BukkitEquipment.fromString(hand)?.run { attribute["Slot"] = ItemTagData(nms) }
                                }
                                attributeListLegacy.add(attribute)
                            } catch (t: Throwable) {
                                t.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
    }

    override val id: String
        get() = "attribute"

    override fun build(player: Player?, compound: ItemTag) {
        if (MinecraftVersion.majorLegacy < 11600) {
            compound["AttributeModifiers"] = attributeListLegacy
        } else {
            compound.remove("AttributeModifiers")
        }
    }

    override fun build(itemMeta: ItemMeta) {
        if (MinecraftVersion.majorLegacy >= 11600) {
            val modifiers = itemMeta.attributeModifiers
            attributeList.forEach {
                // Cannot register AttributeModifier. Modifier is already applied!
                if (modifiers == null || modifiers.values().none { a -> getModifierIdentity(a) == getModifierIdentity(it.second) }) {
                    itemMeta.addAttributeModifier(it.first, it.second)
                }
            }
        }
    }

    override fun drop(player: Player?, compound: ItemTag) {
        compound.remove("AttributeModifiers")
    }

    override fun toString(): String {
        return "MetaAttribute(attributeList=$attributeListLegacy)"
    }

    fun toArray(uuid: UUID): IntArray {
        return toArray(uuid.mostSignificantBits, uuid.leastSignificantBits)
    }

    fun toArray(m: Long, l: Long): IntArray {
        return intArrayOf((m shr 32).toInt(), m.toInt(), (l shr 32).toInt(), l.toInt())
    }

    // 适配逻辑已迁移至 common Attributes.factory

    /**
     * 获取 AttributeModifier 的稳定标识：优先使用 Key（新 API），否则回退到 UUID（旧 API）。
     */
    private fun getModifierIdentity(modifier: AttributeModifier): Any? {
        // 尝试调用 getKey()
        try {
            val m = modifier.javaClass.getMethod("getKey")
            val key = m.invoke(modifier)
            if (key != null) return key
        } catch (_: Throwable) {
            // 忽略
        }
        // 回退到 UUID（兼容旧版）
        return try {
            modifier.uniqueId
        } catch (_: Throwable) {
            // 最后回退到 name + amount + operation 组合
            modifier.name + ":" + modifier.amount + ":" + modifier.operation.name
        }
    }
}