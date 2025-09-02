package ink.ptms.zaphkiel.impl.feature.hook

import ink.ptms.zaphkiel.impl.item.toItemStream
import org.bukkit.entity.Player
import org.serverct.ersha.api.AttributeAPI
import org.serverct.ersha.attribute.data.AttributeData
import taboolib.common.platform.event.OptionalEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.Coerce
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.platform.util.isAir
import taboolib.type.BukkitEquipment

internal object AttributePlusHook {

    @SubscribeEvent(bind = "org.serverct.ersha.api.event.AttrUpdateAttributeEvent\$After")
    fun e1(e: OptionalEvent) {
        val attributeData = e.source.getProperty<AttributeData>("attributeData")!!
        val sourceEntity = attributeData.sourceEntity
        if (sourceEntity is Player) {
            val attrData = AttributeAPI.getAttrData(sourceEntity)
            val items = BukkitEquipment.values().mapNotNull { it.getItem(sourceEntity) }
            items.forEachIndexed { index, item ->
                AttributeAPI.takeSourceAttribute(attrData, "Zaphkiel.$index")
                if (item.isAir()) {
                    return@forEachIndexed
                }
                val itemStream = item.toItemStream()
                if (itemStream.isVanilla()) {
                    return@forEachIndexed
                }
                val attribute = itemStream.getZaphkielData()["attribute-plus"]?.asCompound() ?: return@forEachIndexed
                val map = HashMap<String, Array<Number>>()
                attribute.forEach { (key, data) ->
                    val args = data.asString().split("-")
                    map[key] = arrayOf(Coerce.toDouble(args[0]), Coerce.toDouble(args.getOrElse(1) { args[0] }))
                }
                AttributeAPI.addSourceAttribute(attrData, "Zaphkiel.$index", map, false)
            }
        }
    }

}
