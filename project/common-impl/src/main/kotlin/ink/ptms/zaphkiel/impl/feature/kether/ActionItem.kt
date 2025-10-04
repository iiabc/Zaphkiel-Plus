package ink.ptms.zaphkiel.impl.feature.kether

import ink.ptms.zaphkiel.api.ItemSignal
import ink.ptms.zaphkiel.api.ItemStream
import ink.ptms.zaphkiel.impl.Translator
import ink.ptms.zaphkiel.impl.feature.damageItem
import ink.ptms.zaphkiel.impl.feature.getCurrentDurability
import ink.ptms.zaphkiel.impl.feature.getMaxDurability
import ink.ptms.zaphkiel.impl.feature.repairItem
import ink.ptms.zaphkiel.impl.item.toItemStream
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.kether.*
import taboolib.module.nms.ItemTagData
import taboolib.platform.util.isNotAir

/**
 * item select {ItemStack}
 * item is-zaphkiel
 * item id
 * item durability
 * item max-durability
 * item consume
 * item repair 1
 * item damage 1
 * item update
 * item data key
 * item data key to 1
 * item data key to ~
 */
@KetherParser(["item", "zitem"], namespace = "zaphkiel", shared = true)
private fun parserItem() = scriptParser {
    it.switch {
        // 判断是否为 zaphkiel 物品
        case("is-zaphkiel", "is-extension") {
            val expr = it.nextParsedAction()
            actionFuture { future ->
                run(expr).thenAccept { result ->
                    val stream = when (result) {
                        is ItemStream -> result
                        is ItemStack -> try {
                            if (result.isNotAir()) {
                                result.toItemStream()
                            } else {
                                null
                            }
                        } catch (_: Throwable) {
                            null
                        }

                        else -> null
                    }
                    future.complete(stream?.isExtension() == true)
                }
            }
        }
        // 选择物品：将给定的 ItemStack/ItemStream 写入 @ItemStream
        case("select") {
            val expr = it.nextParsedAction()
            actionFuture { future ->
                run(expr).thenAccept { result ->
                    val selected = when (result) {
                        is ItemStream -> result
                        is ItemStack -> try {
                            if (result.isNotAir()) {
                                result.toItemStream()
                            } else {
                                null
                            }
                        } catch (ex: Throwable) {
                            error("Not a zaphkiel item")
                        }

                        else -> error("Not a zaphkiel item")
                    }
                    variables().set("@ItemStream", selected)
                    future.complete(selected)
                }
            }
        }
        case("id") {
            actionNow { itemStream().getZaphkielId() }
        }
        case("durability") {
            actionNow { itemStream().getCurrentDurability() }
        }
        case("max-durability", "max_durability") {
            actionNow { itemStream().getMaxDurability() }
        }
        case("consume") {
            actionNow { itemStream().sourceItem.amount-- }
        }
        case("repair") {
            val value = it.nextParsedAction()
            actionTake {
                run(value).int { value -> itemStream().repairItem(value, script().sender?.castSafely<Player>()) }
            }
        }
        case("damage") {
            val value = it.nextParsedAction()
            actionTake {
                run(value).int { value -> itemStream().damageItem(value, script().sender?.castSafely<Player>()) }
            }
        }
        // 更新
        // 下次检查时更新，不是立即更新
        case("update") {
            actionNow { itemStream().signal.add(ItemSignal.UPDATE_CHECKED) }
        }
        // 数据
        case("data") {
            val key = it.nextParsedAction()
            val value = try {
                it.mark()
                expect("to")
                it.nextParsedAction()
            } catch (_: Throwable) {
                it.reset()
                null
            }
            actionFuture { f ->
                run(key).str { key ->
                    // 获取
                    if (value == null) {
                        val unsafeData = itemStream().getZaphkielData().getDeep(key)?.unsafeData()
                        f.complete(if (unsafeData != null) Translator.fromItemTag(unsafeData) else null)
                    }
                    // 设置
                    else if (key != "~") {
                        run(value).str { value ->
                            f.complete(
                                itemStream().getZaphkielData().putDeep(key, ItemTagData.toNBT(value))
                            )
                        }
                    }
                    // 移除
                    else {
                        itemStream().getZaphkielData().removeDeep(key)
                        f.complete(null)
                    }
                }
            }
        }
    }
}