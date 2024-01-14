package models

object GameModels {
    data class Scenario(
        val language: String,
        val scenes: List<Scene> = emptyList()
    )

    data class Scene(
        val id: Int,
        val back: String,               // 背景图片
        val front: String? = null,      // 前景图片（立绘）
        val frontAlpha: Float = 1f,     // 前景图片透明度（0～1）
        val elements: List<SceneElement> = emptyList()
    )

    data class Text(
        override val type: String,
        val character: String? = null,  // 角色名，仅当type为speech时需要
        val time: String? = null,       // 时间，仅当type为speech时需要
        val content: String
    ) : SceneElement(type)

    data class Effect(
        override val type: String,
        val front: String? = null,      // 仅当type为change-front时需要
        val image: String? = null       // 仅当type为show-image时需要
    ) : SceneElement(type)

    open class SceneElement(open val type: String)
}