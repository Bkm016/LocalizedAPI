package ink.ptms.localizedapi

import taboolib.common.platform.function.warning

/**
 * LocalizedAPI
 * ink.ptms.localizedapi.LocalizedName
 *
 * @author sky
 * @since 2021/9/11 7:05 下午
 */
class LocalizedName(val id: String, val name: Map<String, String>, val isVanilla: Boolean, val error: String? = null) {

    fun getName(): String? {
        if (error != null) {
            warning(error)
        }
        return name["zh_cn"]
    }

    override fun toString(): String {
        return getName().toString()
    }
}