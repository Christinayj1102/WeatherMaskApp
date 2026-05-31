import java.io.FileInputStream;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherService {

    private String loadApiKey() throws Exception {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream("config.properties");
        props.load(fis);
        fis.close();

        return props.getProperty("KMA_API_KEY");
    }

    public WeatherData getCurrentWeather() {
        double temperature = 0.0;
        int humidity = 0;
        int rainType = 0;
        double rainAmount = 0.0;

        try {
            String serviceKey = loadApiKey();

            LocalDateTime now = LocalDateTime.now().minusMinutes(40);

            String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String baseTime = String.format("%02d00", now.getHour());

            String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"
                    + "?numOfRows=10"
                    + "&pageNo=1"
                    + "&base_date=" + baseDate
                    + "&base_time=" + baseTime
                    + "&nx=55"
                    + "&ny=127"
                    + "&serviceKey=" + serviceKey;

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8")
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();
            conn.disconnect();

            String xml = response.toString();

            temperature = Double.parseDouble(getValueByCategory(xml, "T1H"));
            humidity = Integer.parseInt(getValueByCategory(xml, "REH"));
            rainType = Integer.parseInt(getValueByCategory(xml, "PTY"));
            rainAmount = Double.parseDouble(getValueByCategory(xml, "RN1"));

        } catch (Exception e) {
            System.out.println("날씨 API 호출 중 오류가 발생했습니다.");
            e.printStackTrace();
        }

        return new WeatherData(temperature, humidity, rainType, rainAmount);
    }

    private String getValueByCategory(String xml, String category) {
        String categoryTag = "<category>" + category + "</category>";
        int categoryIndex = xml.indexOf(categoryTag);

        if (categoryIndex == -1) {
            return "0";
        }

        int valueStart = xml.indexOf("<obsrValue>", categoryIndex) + "<obsrValue>".length();
        int valueEnd = xml.indexOf("</obsrValue>", valueStart);

        if (valueStart == -1 || valueEnd == -1) {
            return "0";
        }

        return xml.substring(valueStart, valueEnd);
    }
}