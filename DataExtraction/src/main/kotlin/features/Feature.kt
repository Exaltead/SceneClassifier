package features

data class Feature(val location: String, val type: String, val features: FloatArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Feature

        if (location != other.location) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = location.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}
