package dev.niessen.webhookservice.model

data class MenuModel(
    val menuName: String, // Pflaumencrumble + Vanillecreme
    val restaurant: MenuRestaurant, // PAPA, CANTEEN
    val description: String?, // veganer Pflaumencrumble + Vanillecreme
    val subtitle: String?, // DESSERTS_
    val price: String?, // 1.70€
    val properties: List<MenuProperty> = mutableListOf(),
)

enum class MenuRestaurant(val value: String) {
    PAPA("Papa"),
    CANTEEN("Canteen"),
    DINER("Diner"),
    CAFE("Cafe"),
    JOURNALIST_CLUB("Journalisten Club"),
    DELI("Deli"),
    UNKNOWN("Unbekannt");

    companion object {
        fun byName(name: String?): MenuRestaurant {
            return when {
                name.equals("papa", true) -> PAPA
                name.equals("canteen", true) -> CANTEEN
                name.equals("journalistenclub", true) -> JOURNALIST_CLUB
                name.equals("diner", true) -> DINER
                name.equals("deli", true) -> DELI
                name.equals("cafe", true) -> CAFE
                else -> UNKNOWN
            }
        }
    }
}

enum class MenuProperty(val value: String) {
    VEGAN("Vegan"),
    VEGETARIAN("Vegetarisch"),
    PORK("Schweinefleisch"),
    NO_SUGAR("Ohne Zucker");

    companion object {
        fun byName(name: String?): MenuProperty? {
            return when {
                name.equals("vegan", true) -> VEGAN
                name.equals("vegetarisch", true) -> VEGETARIAN
                name.equals("schwein", true) -> PORK
                name.equals("ohne_zucker", true) -> NO_SUGAR
                else -> null
            }
        }
    }
}
