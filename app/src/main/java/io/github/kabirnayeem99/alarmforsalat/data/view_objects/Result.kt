package io.github.kabirnayeem99.alarmforsalat.data.view_objects

data class Result(
    val address_components: List<AddressComponent>,
    val adr_address: String,
    val formatted_address: String,
    val geometry: Geometry,
    val id: String,
    val name: String,
    val place_id: String,
    val reference: String,
    val types: List<String>,
    val utc_offset: Int,
)