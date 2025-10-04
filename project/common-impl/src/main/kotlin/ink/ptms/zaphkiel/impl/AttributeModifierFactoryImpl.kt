package ink.ptms.zaphkiel.impl

import ink.ptms.zaphkiel.api.AttributeModifierFactory
import ink.ptms.zaphkiel.api.Attributes
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.util.*

object AttributeModifierFactoryImpl : AttributeModifierFactory {

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
		return if (equipmentSlot != null) {
			AttributeModifier(UUID.randomUUID(), name, amount, operation, equipmentSlot)
		} else {
			AttributeModifier(UUID.randomUUID(), name, amount, operation)
		}
	}
}


