package tn.esprit.freelancy.model

data class AnalysisResponse(
    val techniques: List<String>,
    val summary: String
)
data class Entity(
    val entity_group: String,
    val word: String,
    val score: Double
)
