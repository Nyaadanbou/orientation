package cc.mewcraft.orientation.protect

interface ProtectGroup {
    companion object {
        fun create(vararg clazz: Class<out Protect>): ProtectGroup {
            return ProtectGroupImpl().apply {
                clazz.forEach { addProtect(it) }
            }
        }
    }

    fun <T : Protect> hasProtect(protectClazz: Class<T>): Boolean
}

inline fun <reified T : Protect> ProtectGroup.hasProtect(): Boolean {
    return hasProtect(T::class.java)
}

private class ProtectGroupImpl : ProtectGroup {
    private val protects = mutableSetOf<Class<out Protect>>()

    override fun <T : Protect> hasProtect(protectClazz: Class<T>): Boolean {
        return protects.contains(protectClazz)
    }

    fun <T : Protect> addProtect(protectClazz: Class<T>) {
        protects.add(protectClazz)
    }
}