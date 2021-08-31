package me.kit.commonDomain.network.responses

import kotlinx.serialization.Serializable

/*city(行政區)
、lineId(清運路線編號
、lineName(清運路線名稱)
、rank(清運序)、name(清運點名稱)
、village(清運點所屬里)
、longitude(經度)
、latitude(緯度)
、time(表定抵達清運時間)
、memo(備註)
、garbageSunday(一般垃圾清運-星期日)
、garbageMonday(一般垃圾清運-星期一)
、garbageTuesday(一般垃圾清運-星期二)
、garbageWednesday(一般垃圾清運-星期三)
、garbageThursday(一般垃圾清運-星期四)
、garbageFriday(一般垃圾清運-星期五)
、garbageSaturday(一般垃圾清運-星期六)
、recyclingSunday(資源回收清運-星期日)
、recyclingMonday(資源回收清運-星期一)
、recyclingTuesday(資源回收清運-星期二)
、recyclingWednesday(資源回收清運-星期三)
、recyclingThursday(資源回收清運-星期四)
、recyclingFriday(資源回收清運-星期五)
、recyclingSaturday(資源回收清運-星期六)
、foodScrapsSunday(廚餘清運-星期日)
、foodScrapsMonday(廚餘清運-星期一)
、foodScrapsTuesday(廚餘清運-星期二)
、foodScrapsWednesday(廚餘清運-星期三)
、foodScrapsThursday(廚餘清運-星期四)
、foodScrapsFriday(廚餘清運-星期五)
、foodScrapsSaturday(廚餘清運-星期六)
、twd97X(twd97緯度)
、twd97Y(twd97經度)
、wgs84aX(wgs84a緯度)
、wgs84aY(wgs84a經度)*/

@Serializable
data class TruckRoute(
    val recyclingSunday: String? = null,
    val wgs84aX: String? = null,
    val wgs84aY: String? = null,
    val garbageThursday: String? = "",
    val city: String? = "",
    val latitude: String? = "",
    val lineName: String? = "",
    val memo: String? = null,
    val foodScrapsSunday: String? = null,
    val foodScrapsSaturday: String? = "",
    val foodScrapsWednesday: String? = null,
    val rank: String? = "",
    val foodScrapsMonday: String? = "",
    val village: String? = "",
    val recyclingSaturday: String? = "",
    val foodScrapsFriday: String? = "",
    val longitude: String? = "",
    val recyclingTuesday: String? = "",
    val recyclingThursday: String? = "",
    val garbageMonday: String? = "",
    val lineId: String? = "",
    val recyclingFriday: String? = "",
    val garbageTuesday: String? = "",
    val recyclingMonday: String? = "",
    val garbageWednesday: String? = null,
    val foodScrapsTuesday: String? = "",
    val recyclingWednesday: String? = null,
    val twd97X: String? = "",
    val twd97Y: String? = "",
    val garbageFriday: String? = "",
    val name: String? = "",
    val garbageSunday: String? = null,
    val foodScrapsThursday: String? = "",
    val time: String? = "",
    val garbageSaturday: String? = ""
)