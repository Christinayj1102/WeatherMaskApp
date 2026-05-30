public class MaskRecommender {

    public String recommendMask(WeatherData weather, AirQualityData air) {
        int pm10 = air.pm10;
        int pm25 = air.pm25;
        int humidity = weather.humidity;
        int rainType = weather.rainType;

        if (pm25 >= 76 || pm10 >= 151) {
            return "Fine dust levels are very high. Wearing a KF94 mask is strongly recommended.";
        } else if (pm25 >= 36 || pm10 >= 81) {
            return "Fine dust levels are high. Wearing a KF80 or KF94 mask is recommended.";
        } else if (pm25 >= 16 || pm10 >= 31) {
            if (humidity < 40) {
                return "Air quality is moderate, and the air is dry. Wearing a mask is recommended, especially for respiratory protection.";
            }
            return "Air quality is moderate. People sensitive to fine dust are advised to wear a mask.";
        } else {
            if (rainType != 0) {
                return "It is raining and the air quality is good, so wearing a mask is less necessary.";
            }
            return "Air quality is good. Wearing a mask is not necessary for most people.";
        }
    }
}