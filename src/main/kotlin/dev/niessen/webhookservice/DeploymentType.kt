package dev.niessen.webhookservice

enum class DeploymentType {
    CLOUD_FUNCTION,
    DAEMON,
    REST;

    companion object {
        fun findByName(name: String?): DeploymentType? =
            entries.find { it.name == name }
    }
}
