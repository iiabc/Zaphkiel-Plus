package ink.ptms.zaphkiel.api

import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import java.util.*

interface AttributeModifierFactory {

    fun create(
        name: String,
        amount: Double,
        operation: AttributeModifier.Operation,
        equipmentSlot: EquipmentSlot?
    ): AttributeModifier
}

object Attributes {

    @Volatile
    var factory: AttributeModifierFactory = object : AttributeModifierFactory {
        override fun create(
            name: String,
            amount: Double,
            operation: AttributeModifier.Operation,
            equipmentSlot: EquipmentSlot?
        ): AttributeModifier {
            return if (equipmentSlot != null) {
                AttributeModifier(UUID.randomUUID(), name, amount, operation, equipmentSlot)
            } else {
                AttributeModifier(UUID.randomUUID(), name, amount, operation)
            }
        }
    }

    fun createAttributeModifier(
        name: String,
        amount: Double,
        operation: AttributeModifier.Operation,
        equipmentSlot: EquipmentSlot?
    ): AttributeModifier {
        return factory.create(name, amount, operation, equipmentSlot)
    }
}


