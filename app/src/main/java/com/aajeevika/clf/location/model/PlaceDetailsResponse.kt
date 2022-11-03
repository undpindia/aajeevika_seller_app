package com.aajeevika.clf.location.model

data class PlaceDetailsResponse(
    val result: PlaceDetails,
    val status: String
) {
    data class PlaceDetails(
        val geometry: Geometry,
        val name: String
    ) {
        data class Geometry(
            val location: Location
        ) {
            data class Location(
                val lat: Double,
                val lng: Double
            )
        }
    }
}