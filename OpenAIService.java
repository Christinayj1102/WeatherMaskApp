import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class OpenAIService {

    private String loadApiKey() throws Exception {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream("config.properties");
        props.load(fis);
        fis.close();
        return props.getProperty("OPENAI_API_KEY");
    }

    public String generateRecommendation(WeatherData weather, AirQualityData air, String maskRecommendation) {
        try {
            String apiKey = loadApiKey();

            String prompt =
                    "Temperature: " + weather.temperature + "°C\n" +
                    "Humidity: " + weather.humidity + "%\n" +
                    "PM10: " + air.pm10 + "\n" +
                    "PM2.5: " + air.pm25 + "\n" +
                    "Basic recommendation: " + maskRecommendation + "\n\n" +
                    "Write a short, friendly mask recommendation in English. Maximum 3 sentences.";

            URL url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            String safePrompt = prompt
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n");

            String requestBody =
                    "{"
                            + "\"model\":\"gpt-4o-mini\","
                            + "\"messages\":["
                            + "{\"role\":\"user\",\"content\":\"" + safePrompt + "\"}"
                            + "]"
                            + "}";

            OutputStream os = conn.getOutputStream();
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
            os.close();

            InputStream inputStream;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8)
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();

            String result = response.toString();

            String keyword = "\"content\":";
            int contentIndex = result.indexOf(keyword);

            if (contentIndex == -1) {
                return "AI response parsing failed.\nRaw response:\n" + result;
            }

            int firstQuote = result.indexOf("\"", contentIndex + keyword.length());
            int secondQuote = result.indexOf("\"", firstQuote + 1);

            String content = result.substring(firstQuote + 1, secondQuote);

            return content
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"");

        } catch (Exception e) {
            e.printStackTrace();
            return "AI recommendation generation failed.";
        }
    }
}