package ink.ptms.zaphkiel.impl

import ink.ptms.zaphkiel.api.ItemSignal
import ink.ptms.zaphkiel.api.ItemStream
import ink.ptms.zaphkiel.api.ItemUpdater
import ink.ptms.zaphkiel.api.event.ItemBuildEvent
import ink.ptms.zaphkiel.impl.item.DefaultItemStream
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.isAir

/**
 * Zaphkiel
 * ink.ptms.zaphkiel.impl.DefaultItemUpdater
 *
 * @author 坏黑
 * @since 2022/7/23 16:15
 */
class DefaultItemUpdater : ItemUpdater {

    override fun checkUpdate(player: Player?, inventory: Inventory) {
        // 遍历背包中的每个物品槽位
        (0 until inventory.size).forEach { i ->
            val item = inventory.getItem(i)
            // 跳过空气方块（空槽位）
            if (item.isAir()) {
                return@forEach
            }
            // 检查单个物品是否需要更新，返回重建后的 ItemStream
            val rebuild = checkUpdate(player, item)
            // 如果物品标记为需要更新，则将重建后的物品替换回背包
            if (ItemSignal.UPDATE_CHECKED in rebuild.signal) {
                // 将 ItemStream 转换为 ItemStack 并更新到背包对应位置
                // 这会触发 ItemReleaseEvent.Final 事件，应用所有 meta 修改，包含 ItemStack 级别修改
                inventory.setItem(i, rebuild.toItemStack(player))
            }
        }
    }

    override fun checkUpdate(player: Player?, item: ItemStack): ItemStream {
        if (item.isAir()) {
            error("air")
        }
        val itemStream = DefaultItemStream(item)
        if (itemStream.isVanilla()) {
            return itemStream
        }
        val event = ItemBuildEvent.CheckUpdate(player, itemStream, itemStream.isOutdated())
        return if (event.call()) {
            // 使用 ItemStream#rebuild 方法会生成新的 ItemStreamGenerated 实例
            // 将会重新生成物品名称与描述，产生更多的计算
            // 现在看来 nameLock、loreLock 这种设计并不是特别出色
            // 在 1.6.1 版本时想过移除，但是没有意义
            itemStream.signal += ItemSignal.UPDATE_CHECKED
            itemStream.getZaphkielItem().build(player, itemStream)
        } else {
            itemStream
        }
    }
}