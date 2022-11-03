package com.aajeevika.clf.model.data_model

data class CountryModel(
    var country: ArrayList<CountryData>
)

data class StateModel(
    var states: ArrayList<StateData>
)

data class CityModel(
    var district: ArrayList<CityData>
)

data class BlockModel(
    var block: ArrayList<BlockData>
)

data class CountryData(
    var id: Int,
    var sortname: String,
    var name: String,
    var phonecode: Int,
)

data class StateData(
    var id: Int,
    var name: String,
    var country_id: Int,
)

data class CityData(
    var id: Int,
    var name: String,
    var state_id: Int,
)

data class BlockData(
    var id: Int,
    var name: String,
    var city_id: Int,
)