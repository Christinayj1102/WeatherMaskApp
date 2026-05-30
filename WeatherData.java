public class WeatherData {
    public double temperature;
    public int humidity;
    public int rainType;
    public double rainAmount;

    public WeatherData(double temperature, int humidity, int rainType, double rainAmount) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.rainType = rainType;
        this.rainAmount = rainAmount;
    }

    public String getRainTypeText() {
        if (rainType == 0) return "No Rain";
        if (rainType == 1) return "Rain";
        if (rainType == 2) return "Rain/Snow";
        if (rainType == 3) return "Snow";
        return "Other";
    }

    public void printWeather() {
        System.out.println("현재 기온: " + temperature + "℃");
        System.out.println("습도: " + humidity + "%");
        System.out.println("강수 형태: " + getRainTypeText());
        System.out.println("1시간 강수량: " + rainAmount + "mm");
    }
}