package com.neelvis.model

import com.squareup.moshi.Json
//{"coord":\{"lon":126.9762,"lat":37.5387}

//    ,"weather":[\{"id":701,"main":"Mist","description":"mist","icon":"50n"}],"base":"stations","main":{"temp":297.48,"feels_like":298.06,"temp_min":297.03,"temp_max":298.15,"pressure":1017,"humidity":80},"visibility":9000,"wind":{"speed":0.51,"deg":230},"clouds":{"all":75},"dt":1630324047,"sys":{"type":1,"id":8096,"country":"KR","sunrise":1630270837,"sunset":1630317927},"timezone":32400,"id":1837055,"name":"Yongsan","cod":200}

    data class Weather(
    @Json (name = "name")
    val name: String = "Unknown City",
    @Json (name = "temp")
    val temperature: Float = 0.0f
    )

//data class WeatherResponse()