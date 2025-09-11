package dev.niessen.webhookservice

enum class DeploymentType {
    CLOUD_FUNCTION,
    DAEMON,
    REST,
    TEST;

    companion object {
        fun findByName(name: String?): DeploymentType? =
            entries.find { it.name == name }?.takeIf { it != TEST }
    }
}
