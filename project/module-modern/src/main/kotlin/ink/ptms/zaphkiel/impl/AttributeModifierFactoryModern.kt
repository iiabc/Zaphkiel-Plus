package ink.ptms.zaphkiel.impl

import ink.ptms.zaphkiel.api.AttributeModifierFactory
import ink.ptms.zaphkiel.api.Attributes
import org.bukkit.NamespacedKey
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.util.*

object AttributeModifierFactoryModern : AttributeModifierFactory {

    @Awake(LifeCycle.LOAD)
    fun register() {
        Attributes.factory = this
    }

    override fun create(
        name: String,
        amount: Double,
        operation: AttributeModifier.Operation,
        equipmentSlot: EquipmentSlot?
    ): AttributeModifier {
        val key = NamespacedKey.fromString("zaphkiel:" + UUID.randomUUID().toString().replace("-", ""))
        if (key != null) {
            if (equipmentSlot != null) {
                return AttributeModifier(key, amount, operation, equipmentSlot.group)
            }
        }
        error("AttributeModifier is not supported")
    }
}


