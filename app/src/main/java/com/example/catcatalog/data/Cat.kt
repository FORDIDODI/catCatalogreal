package com.example.catcatalog.data

import com.google.gson.annotations.SerializedName

// Data model untuk item kucing dari TheCatApi
data class Cat(
    @SerializedName("id") val id: String,
    @SerializedName("url") val url: String,
    @SerializedName("breeds") val breeds: List<Breed>?
)

data class Breed(
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("origin") val origin: String?,
    @SerializedName("temperament") val temperament: String?,
    @SerializedName("life_span") val lifeSpan: String?,
    @SerializedName("wikipedia_url") val wikipediaUrl: String?,
    @SerializedName("weight") val weight: Weight?
)

data class Weight(
    @SerializedName("imperial") val imperial: String?,
    @SerializedName("metric") val metric: String?
) 