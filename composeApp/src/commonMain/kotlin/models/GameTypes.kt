package models

object GameTypes {
    object Effect {
        const val RedTurn = "red-turn"
        const val ClimaxBlink = "climax-blink"
    }

    object Text {
        const val Normal = "normal"
        const val Speech = "speech"
        const val SpecialWhite = "special-white"
    }

    object Back {
        private const val Room = "room"
        private const val Studio = "studio"
        private const val Building = "building"
        private const val Street = "street"
        private const val Door = "door"

        fun getImage(tag: String) = when (tag) {
            Room -> "drawable/img_room.png"
            Studio -> "drawable/img_studio.png"
            Building -> "drawable/img_building.png"
            Street -> "drawable/img_street.png"
            Door -> "drawable/img_door.png"
            else -> null
        }
    }

    object Front {
        private const val WryNeck = "girl-wry-neck"
        private const val Serious = "girl-serious"
        private const val Happy = "girl-happy"
        private const val Shy = "girl-shy"
        private const val Mature = "girl-mature"

        fun getImage(tag: String) = when (tag) {
            WryNeck -> "drawable/img_girl_wry_neck.png"
            Serious -> "drawable/img_girl_serious.png"
            Happy -> "drawable/img_girl_happy.png"
            Shy -> "drawable/img_girl_shy.png"
            Mature -> "drawable/img_girl_mature.png"
            else -> null
        }
    }
}