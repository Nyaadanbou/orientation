package cc.mewcraft.orientation.protect

import it.unimi.dsi.fastutil.objects.ObjectArraySet

fun ProtectGroup(vararg protect: Protect): ProtectGroup {
    val protectGroup = ProtectGroupImpl()
    protect.forEach { protectGroup.addProtect(it) }
    return protectGroup
}

private class ProtectGroupImpl : ProtectGroup {
    private val protects: ObjectArraySet<Protect> = ObjectArraySet()

    override fun hasProtect(protect: Protect): Boolean {
        return protects.contains(protect)
    }

    fun addProtect(protect: Protect) {
        protects.add(protect)
    }
}