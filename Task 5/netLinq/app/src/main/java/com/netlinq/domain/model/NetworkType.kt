package com.netlinq.domain.model

enum class NetworkType(val label: String) {
    WIFI("WiFi"),
    FIVE_G("5G"),
    FOUR_G("4G"),
    THREE_G("3G"),
    TWO_G("2G"),
    UNKNOWN("Unknown"),
    NONE("No connection");

    companion object {
        fun fromLabel(label: String): NetworkType =
            entries.find { it.label.equals(label, ignoreCase = true) } ?: UNKNOWN
    }
}
