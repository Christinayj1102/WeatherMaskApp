public class AirQualityData {
    public int pm10;
    public int pm25;
    public int khaiValue;
    public int khaiGrade;

    public AirQualityData(int pm10, int pm25, int khaiValue, int khaiGrade) {
        this.pm10 = pm10;
        this.pm25 = pm25;
        this.khaiValue = khaiValue;
        this.khaiGrade = khaiGrade;
    }

    public void printAirQuality() {
        System.out.println("PM10: " + pm10 + "㎍/㎥");
        System.out.println("PM2.5: " + pm25 + "㎍/㎥");
        System.out.println("통합대기환경수치: " + khaiValue);
        System.out.println("통합대기환경등급: " + khaiGrade);
    }
}