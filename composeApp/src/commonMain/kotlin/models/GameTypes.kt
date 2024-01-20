package models

object GameTypes {
    object Effect {
        const val RedTurn = "red-turn"
        const val ClimaxBlink = "climax-blink"
        const val InvertColor = "invert-color"
        const val TurnWhite = "turn-white"
        const val ChangeFront = "change-front"
    }

    object Text {
        const val Normal = "normal"
        const val Speech = "speech"
        const val SpecialWhite = "special-white"
        const val SpecialRed = "special-red"
        const val Scary = "scary"
    }

    object Back {
        private const val Room = "room"
        private const val Studio = "studio"
        private const val Building = "building"
        private const val Street = "street"
        private const val Door = "door"
        private const val RoomScary = "room-scary"
        private const val Scary = "scary"
        private const val Hospital = "hospital"
        private const val Smoke = "smoke"
        private const val Trash = "trash"

        fun getImage(tag: String) = when (tag) {
            Room -> "drawable/img_room.webp"
            Studio -> "drawable/img_studio.webp"
            Building -> "drawable/img_building.webp"
            Street -> "drawable/img_street.webp"
            Door -> "drawable/img_door.webp"
            RoomScary -> "drawable/img_room_scary.webp"
            Scary -> "drawable/img_scary.webp"
            Hospital -> "drawable/img_hospital.webp"
            Smoke -> "drawable/img_smoke.webp"
            Trash -> "drawable/img_trash.webp"
            else -> null
        }
    }

    object Front {
        private const val WryNeck = "girl-wry-neck"
        private const val Serious = "girl-serious"
        private const val Happy = "girl-happy"
        private const val Shy = "girl-shy"
        private const val Mature = "girl-mature"
        private const val Scared = "girl-scared"

        fun getImage(tag: String) = when (tag) {
            WryNeck -> "drawable/img_girl_wry_neck.webp"
            Serious -> "drawable/img_girl_serious.webp"
            Happy -> "drawable/img_girl_happy.webp"
            Shy -> "drawable/img_girl_shy.webp"
            Mature -> "drawable/img_girl_mature.webp"
            Scared -> "drawable/img_girl_scared.webp"
            else -> null
        }
    }
}