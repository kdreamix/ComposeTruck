package me.kit.commonDomain.network.responses

import kotlinx.serialization.Serializable

/*
    lineId(清運路線編號)、
    car(車牌號碼)、
    time(回報時間)、
    location(所在位址)、
    longitude(經度)、
    latitude(緯度)、
    cityId(行政區代號)、
    cityName(行政區名稱)
 */

@Serializable
data class TruckLocation(
    val lineId: String?,
    val car: String?,
    val time: String?,
    val location: String?,
    val longitude: String?,
    val latitude: String?,
    val cityId: String?,
    val cityName: String?,
){
    val latitudeDouble = latitude?.toDouble()
    val longitudeDouble = longitude?.toDouble()
}