import java.io.FileInputStream;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AirQualityService {

    private String loadApiKey() throws Exception {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream("config.properties");
        props.load(fis);
        fis.close();

        return props.getProperty("AIR_API_KEY");
    }

    public AirQualityData getCurrentAirQuality(String stationNameInput) {
        int pm10 = 0;
        int pm25 = 0;
        int khaiValue = 0;
        int khaiGrade = 0;

        try {
            String serviceKey = loadApiKey();

            String stationName = URLEncoder.encode(stationNameInput, StandardCharsets.UTF_8);

            String apiUrl = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty"
                    + "?stationName=" + stationName
                    + "&dataTerm=DAILY"
                    + "&pageNo=1"
                    + "&numOfRows=1"
                    + "&returnType=xml"
                    + "&ver=1.0"
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

            pm10 = parseIntSafe(getTagValue(xml, "pm10Value"));
            pm25 = parseIntSafe(getTagValue(xml, "pm25Value"));
            khaiValue = parseIntSafe(getTagValue(xml, "khaiValue"));
            khaiGrade = parseIntSafe(getTagValue(xml, "khaiGrade"));

        } catch (Exception e) {
            System.out.println("대기질 API 호출 중 오류가 발생했습니다.");
            e.printStackTrace();
        }

        return new AirQualityData(pm10, pm25, khaiValue, khaiGrade);
    }

    public AirQualityData getCurrentAirQuality() {
        return getCurrentAirQuality("종로구");
    }

    private String getTagValue(String xml, String tagName) {
        String startTag = "<" + tagName + ">";
        String endTag = "</" + tagName + ">";

        int startIndex = xml.indexOf(startTag);

        if (startIndex == -1) {
            return "0";
        }

        startIndex += startTag.length();

        int endIndex = xml.indexOf(endTag, startIndex);

        if (endIndex == -1) {
            return "0";
        }

        return xml.substring(startIndex, endIndex).trim();
    }

    private int parseIntSafe(String value) {
        if (value == null || value.equals("") || value.equals("-")) {
            return 0;
        }

        return Integer.parseInt(value);
    }
}