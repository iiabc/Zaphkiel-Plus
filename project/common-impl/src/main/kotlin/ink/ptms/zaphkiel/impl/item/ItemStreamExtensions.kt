package ink.ptms.zaphkiel.impl.item

import ink.ptms.zaphkiel.api.ItemStream
import ink.ptms.zaphkiel.impl.Translator
import taboolib.module.nms.ItemTag

/**
 * 获取物品的全局 NBT 数据（包括自定义路径）
 */
fun ItemStream.getGlobalNbtData(): Map<String, Any> {
    val result = mutableMapOf<String, Any>()

    // 递归遍历整个 NBT 结构
    fun traverseNbt(tag: ItemTag, prefix: String = "") {
        tag.forEach { (key, value) ->
            val fullKey = if (prefix.isEmpty()) key else "$prefix.$key"
            when (value) {
                is ItemTag -> traverseNbt(value, fullKey)
                else -> result[fullKey] = Translator.fromItemTag(value)
            }
        }
    }

    traverseNbt(sourceCompound)
    return result
}

/**
 * 根据路径获取 NBT 值
 */
fun ItemStream.getNbtValue(path: String): Any? {
    val parts = path.split(".")
    var current: Any = sourceCompound

    for (part in parts) {
        current = when (current) {
            is ItemTag -> current[part] ?: return null
            else -> return null
        }
    }

    return Translator.fromItemTag(current)
}

/**
 * 根据路径设置 NBT 值
 */
fun ItemStream.setNbtValue(path: String, value: Any) {
    val parts = path.split(".")
    var current = sourceCompound

    // 导航到父级
    for (i in 0 until parts.size - 1) {
        val part = parts[i]
        if (!current.containsKey(part)) {
            current[part] = ItemTag()
        }
        current = current[part]!!.asCompound()
    }

    // 设置最终值
    val finalKey = parts.last()
    current[finalKey] = Translator.toItemTag(value)
}