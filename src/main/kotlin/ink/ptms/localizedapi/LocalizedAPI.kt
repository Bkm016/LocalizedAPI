package ink.ptms.localizedapi

import com.google.gson.JsonParser
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.library.xseries.XEnchantment
import taboolib.library.xseries.XMaterial
import taboolib.library.xseries.XPotion
import taboolib.module.nms.getI18nName
import taboolib.module.nms.getInternalKey
import taboolib.module.nms.getInternalName
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.jar.JarFile

object LocalizedAPI : Plugin() {

    val regex = ".+/lang/\\S{2}_\\S{2}\\.json".toRegex()
    val localization = HashMap<String, HashMap<String, String>>()

    override fun onLoad() {
        File("mods").listFiles()?.forEach { file ->
            JarFile(file).use { jarFile ->
                kotlin.runCatching {
                    for (entry in jarFile.entries()) {
                        if (entry.name.matches(regex)) {
                            val json = JsonParser().parse(jarFile.getInputStream(entry).readBytes().toString(StandardCharsets.UTF_8)).asJsonObject
                            json.entrySet().forEach { (k, v) ->
                                val name = entry.name.substringAfterLast('/').substringBeforeLast('.').lowercase()
                                localization.computeIfAbsent(k) { HashMap() }[name] = v.asString
                            }
                        }
                    }
                }
            }
        }
    }

    fun getLocalizedName(item: Material): LocalizedName {
        return getLocalizedName(ItemStack(item))
    }

    fun getLocalizedName(entity: Entity): LocalizedName {
        return try {
            val internalName = entity.getInternalName()
            if (localization.containsKey(internalName)) {
                LocalizedName(internalName, localization[internalName]!!, false)
            } else {
                LocalizedName(internalName, hashMapOf("zh_cn" to entity.getI18nName()), true)
            }
        } catch (ex: Throwable) {
            LocalizedName("null", emptyMap(), false, error = ex.message.toString())
        }
    }

    fun getLocalizedName(itemStack: ItemStack): LocalizedName {
        return try {
            val internalName = itemStack.getInternalName()
            if (XMaterial.matchXMaterial(itemStack.type.name).isPresent) {
                LocalizedName(internalName, hashMapOf("zh_cn" to itemStack.getI18nName()), true)
            } else {
                LocalizedName(internalName, localization[internalName] ?: emptyMap(), false)
            }
        } catch (ex: Throwable) {
            LocalizedName("null", emptyMap(), false, error = ex.message.toString())
        }
    }

    fun getLocalizedName(enchantment: Enchantment): LocalizedName {
        return try {
            val internalName = enchantment.getInternalName()
            if (XEnchantment.matchXEnchantment(enchantment.name).isPresent) {
                LocalizedName(internalName, hashMapOf("zh_cn" to enchantment.getI18nName()), true)
            } else {
                LocalizedName(internalName, localization[internalName] ?: emptyMap(), false)
            }
        } catch (ex: Throwable) {
            LocalizedName("null", emptyMap(), false, error = ex.message.toString())
        }
    }

    fun getLocalizedName(potionEffectType: PotionEffectType?): LocalizedName {
        if (potionEffectType == null) {
            return LocalizedName("null", emptyMap(), false)
        }
        return try {
            val internalName = potionEffectType.getInternalName()
            if (XPotion.matchXPotion(potionEffectType.name).isPresent) {
                LocalizedName(internalName, hashMapOf("zh_cn" to potionEffectType.getI18nName()), true)
            } else {
                LocalizedName(internalName, localization[internalName] ?: emptyMap(), false)
            }
        } catch (ex: Throwable) {
            LocalizedName("null", emptyMap(), false, error = ex.message.toString())
        }
    }
}