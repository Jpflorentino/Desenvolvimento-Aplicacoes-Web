package group7.daw.project.models

data class ProjectInputModel(
        val name: String,
        val description: String,
        val allowed_labels_set: String?,
        val allowed_states_set: String?,
        val allowed_transitions_set: String? //["open:closed"]
)
