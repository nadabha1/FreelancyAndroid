package tn.esprit.freelancy.model

data class Job(
    val title: String,
    val description: String,
    val budget: String,
    val level: String,
    val tags: List<String>,
    val client: Client,
    val proposals: String
)
data class Client(
    val paymentVerified: Boolean,
    val spent: String,
    val location: String
)

val jobs = listOf(
    Job(
        title = "Need a super tiny Android APP / just day and time",
        description = "My developer account at Google is at risk of being closed because it's not being used actively...",
        budget = "$100",
        level = "Entry level",
        tags = listOf("Android"),
        client = Client(paymentVerified = true, spent = "$600+", location = "Germany"),
        proposals = "20 to 50"
    )
)