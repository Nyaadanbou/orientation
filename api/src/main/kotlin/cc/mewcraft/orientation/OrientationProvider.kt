package cc.mewcraft.orientation

import org.jetbrains.annotations.ApiStatus

object OrientationProvider {
    private var instance: Orientation? = null

    /**
     * 获取 [Orientation] 实例
     *
     * @return [Orientation] 实例
     * @throws IllegalStateException 如果 [Orientation] 未初始化
     */
    fun get(): Orientation {
        return instance ?: throw IllegalStateException("Playtime is not initialized")
    }

    @ApiStatus.Internal
    fun register(instance: Orientation) {
        OrientationProvider.instance = instance
    }

    @ApiStatus.Internal
    fun unregister() {
        instance = null
    }
}