package cc.mewcraft.orientation.protect

import it.unimi.dsi.fastutil.objects.ObjectArraySet

interface ProtectGroup {
    companion object {
        fun create(vararg protect: Protect): ProtectGroup {
            return ProtectGroupImpl().apply {
                protect.forEach { addProtect(it) }
            }
        }
    }

    fun hasProtect(protect: Protect): Boolean
}

private class ProtectGroupImpl : ProtectGroup {
    private val protects: ObjectArraySet<Protect> = ObjectArraySet()

    override fun  hasProtect(protect: Protect): Boolean {
        return protects.contains(protect)
    }

    fun addProtect(protect: Protect) {
        protects.add(protect)
    }
}