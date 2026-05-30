import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JLabel tempLabel;
    private JLabel humidityLabel;
    private JLabel rainLabel;
    private JLabel pm10Label;
    private JLabel pm25Label;
    private JLabel khaiLabel;
    private JTextArea ruleArea;
    private JTextArea aiArea;

    private WeatherData currentWeather;
    private AirQualityData currentAir;
    private String currentRuleResult;

    public MainFrame() {
        setTitle("AI Weather Mask Recommendation App");
        setSize(900, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("AI Weather Mask Recommendation");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel locationLabel = new JLabel("Location: Jongno-gu, Seoul");
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        locationLabel.setForeground(new Color(90, 90, 90));
        locationLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setBackground(new Color(245, 247, 250));
        headerPanel.add(titleLabel);
        headerPanel.add(locationLabel);

        JPanel cardPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        cardPanel.setBackground(new Color(245, 247, 250));

        tempLabel = createValueLabel("-");
        humidityLabel = createValueLabel("-");
        rainLabel = createValueLabel("-");
        pm10Label = createValueLabel("-");
        pm25Label = createValueLabel("-");
        khaiLabel = createValueLabel("-");

        cardPanel.add(createCard("Temperature", tempLabel));
        cardPanel.add(createCard("Humidity", humidityLabel));
        cardPanel.add(createCard("Rain Status", rainLabel));
        cardPanel.add(createCard("PM10", pm10Label));
        cardPanel.add(createCard("PM2.5", pm25Label));
        cardPanel.add(createCard("Air Quality", khaiLabel));

        ruleArea = createTextArea();
        aiArea = createTextArea();

        JPanel recommendationPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        recommendationPanel.setBackground(new Color(245, 247, 250));
        recommendationPanel.add(createTextCard("Rule-Based Recommendation", ruleArea));
        recommendationPanel.add(createTextCard("AI Recommendation", aiArea));

        JButton refreshAllButton = createButton("Refresh All Data", new Color(65, 105, 225));
        JButton refreshAiButton = createButton("Regenerate AI Recommendation", new Color(46, 139, 87));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(245, 247, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));
        buttonPanel.add(refreshAllButton);
        buttonPanel.add(refreshAiButton);

        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.add(cardPanel, BorderLayout.NORTH);
        centerPanel.add(recommendationPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        refreshAllButton.addActionListener(e -> refreshAllData());
        refreshAiButton.addActionListener(e -> refreshAiOnly());

        SwingUtilities.invokeLater(this::refreshAllData);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(330, 48));
        return button;
    }

    private JPanel createCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        titleLabel.setForeground(new Color(80, 80, 80));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTextCard(String title, JTextArea area) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235)),
                BorderFactory.createEmptyBorder(15, 18, 15, 18)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(area, BorderLayout.CENTER);

        return card;
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(new Color(40, 40, 40));
        return label;
    }

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea("-");
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Arial", Font.PLAIN, 15));
        area.setBackground(Color.WHITE);
        area.setBorder(null);
        return area;
    }

    private void refreshAllData() {
        setLoadingState("Loading all data...");

        WeatherService weatherService = new WeatherService();
        AirQualityService airService = new AirQualityService();
        MaskRecommender recommender = new MaskRecommender();
        OpenAIService aiService = new OpenAIService();

        currentWeather = weatherService.getCurrentWeather();
        currentAir = airService.getCurrentAirQuality();
        currentRuleResult = recommender.recommendMask(currentWeather, currentAir);

        updateDataLabels();

        ruleArea.setText(currentRuleResult);
        aiArea.setText("Generating AI recommendation...");

        String aiResult = aiService.generateRecommendation(currentWeather, currentAir, currentRuleResult);
        aiArea.setText(aiResult);
    }

    private void refreshAiOnly() {
        if (currentWeather == null || currentAir == null || currentRuleResult == null) {
            refreshAllData();
            return;
        }

        aiArea.setText("Regenerating AI recommendation...");

        OpenAIService aiService = new OpenAIService();
        String aiResult = aiService.generateRecommendation(currentWeather, currentAir, currentRuleResult);

        aiArea.setText(aiResult);
    }

    private void updateDataLabels() {
        tempLabel.setText(currentWeather.temperature + "℃");
        humidityLabel.setText(currentWeather.humidity + "%");
        rainLabel.setText(getRainTypeTextEnglish(currentWeather.rainType));
        pm10Label.setText(currentAir.pm10 + " ㎍/㎥");
        pm25Label.setText(currentAir.pm25 + " ㎍/㎥");
        khaiLabel.setText(getKhaiGradeText(currentAir.khaiGrade));
    }

    private void setLoadingState(String message) {
        tempLabel.setText("Loading...");
        humidityLabel.setText("Loading...");
        rainLabel.setText("Loading...");
        pm10Label.setText("Loading...");
        pm25Label.setText("Loading...");
        khaiLabel.setText("Loading...");
        ruleArea.setText(message);
        aiArea.setText(message);
    }

    private String getRainTypeTextEnglish(int rainType) {
        if (rainType == 0) return "No Rain";
        if (rainType == 1) return "Rain";
        if (rainType == 2) return "Rain/Snow";
        if (rainType == 3) return "Snow";
        return "Other";
    }

    private String getKhaiGradeText(int grade) {
        if (grade == 1) return "Good";
        if (grade == 2) return "Moderate";
        if (grade == 3) return "Bad";
        if (grade == 4) return "Very Bad";
        return "Unknown";
    }
}