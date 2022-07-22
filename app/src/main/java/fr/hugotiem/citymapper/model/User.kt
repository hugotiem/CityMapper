package fr.hugotiem.citymapper.model

class User(val email: String, val firstname: String, val lastname: String, val historical: List<HistoryItem>) {
}