package com.neelvis.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.FileDescriptor

data class Main(
    @Json(name = "temp")
    val currentTemperature: Double = 0.0,
    @Json(name = "feels_like")
    val temperatureFeels: Double = 0.0,
    @Json(name = "temp_min")
    val temperatureTodayMin: Double = 0.0,
    @Json(name = "temp_max")
    val temperatureTodayMax: Double = 0.0,
    val pressure: Int = 1,
    val humidity: Int = 146
)

data class WeatherInfo(
    val description: String = "Apocalypse"
)

@JsonClass (generateAdapter = true)
data class Weather(
    val name: String = "Unknown City",
    val main: Main = Main(),
    @Json(name = "weather")
    val info: Array<WeatherInfo> = arrayOf<WeatherInfo>()
)

//    {"coord":{"lon":-122.084,"lat":37.422},
//    "weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],
//    "base":"stations",
//    "main":{"temp":300.32,"feels_like":300.67,"temp_min":288.8,"temp_max":307.33,"pressure":1015,"humidity":49},
//    "visibility":10000,
//    "wind":{"speed":3.13,"deg":0,"gust":8.05},
//    "clouds":{"all":1},
//    "dt":1631397274,
//    "sys":{"type":2,
//    "id":2003086,
//    "country":"US",
//    "sunrise":1631368046,
//    "sunset":1631413353},
//    "timezone":-25200,
//    "id":5375480,
//    "name":"Mountain View",
//    "cod":200}