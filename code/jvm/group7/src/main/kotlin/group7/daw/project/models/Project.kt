package group7.daw.project.models

import group7.daw.project.data.Transition

data class Project (
        val user_id: Int,
        val project_id: Int,
        val name: String,
        val description: String,
        var labels: MutableList<String> = mutableListOf(),
        var states: MutableList<String> = mutableListOf(),
        var transitions: MutableList<Transition> = mutableListOf()
        )