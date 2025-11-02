package com.moodflix.gui;

import com.moodflix.service.QuoteService;

import javax.swing.*;
import java.awt.*;
import java.nio.file.*;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MoodflixGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String userName;
    private JLabel authorLabel, moodLabel;
    private JTextArea quoteArea;
    private String selectedMood;
    private final QuoteService quoteService = new QuoteService();

    // ✅ Local fallback quotes
    private final Map<String, String[]> moodQuotes = Map.of(
            "happy", new String[] { "Happiness depends upon ourselves.", "Aristotle" },
            "sad", new String[] { "Tears come from the heart, not from the brain.", "Leonardo da Vinci" },
            "depressed", new String[] { "This too shall pass.", "Persian Proverb" },
            "frustrated", new String[] { "Frustration is essential for growth.", "Bo Bennett" },
            "stressed", new String[] { "Give your stress wings and let it fly away.", "Terri Guillemets" },
            "lonely", new String[] { "The best part about being alone is freedom.", "Justin Timberlake" },
            "helpless", new String[] { "You always have choices.", "Unknown" });

    private static final Path FAVORITES_PATH = Paths.get(
            "C:\\Users\\Hp\\OneDrive\\Desktop\\moodflix\\src\\main\\java\\com\\moodflix\\favorites.txt");

    public MoodflixGUI() {
        setTitle("Moodflix");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createWelcomePanel(), "welcome");
        mainPanel.add(createMoodPanel(), "mood");
        mainPanel.add(createQuotePanel(), "quote");

        add(mainPanel);
        cardLayout.show(mainPanel, "welcome");
    }

    // ---------------- WELCOME PANEL ----------------
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, new Color(220, 210, 255),
                        getWidth(), getHeight(), new Color(240, 230, 255));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(true);
        card.setBackground(new Color(255, 255, 255, 180));
        card.setPreferredSize(new Dimension(500, 320));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 220), 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("🎬 Welcome to Moodflix", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(90, 50, 160));

        JLabel subtitle = new JLabel("Find quotes that match your mood!", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(new Color(100, 70, 160));

        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        nameLabel.setForeground(new Color(80, 50, 130));

        JTextField nameField = new JTextField(15);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JButton getQuoteBtn = createStyledButton("Get Quote", new Color(100, 200, 100));
        JButton viewFavBtn = createStyledButton("View Favorites", new Color(255, 100, 100));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(title, gbc);

        gbc.gridy++;
        card.add(subtitle, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        card.add(nameLabel, gbc);

        gbc.gridx = 1;
        card.add(nameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(getQuoteBtn);
        buttonRow.add(viewFavBtn);
        card.add(buttonRow, gbc);

        panel.add(card);

        getQuoteBtn.addActionListener(e -> {
            userName = nameField.getText().trim();
            if (userName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name first!", "Required",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            cardLayout.show(mainPanel, "mood");
        });

        viewFavBtn.addActionListener(e -> {
            userName = nameField.getText().trim();
            if (userName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your name first to view favorites!", "Required",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String content = readFavoritesFileContents();
            JTextArea ta = new JTextArea(content.isEmpty() ? "(No favorites yet)" : content);
            ta.setEditable(false);
            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);
            JScrollPane sp = new JScrollPane(ta);
            sp.setPreferredSize(new Dimension(520, 320));
            JOptionPane.showMessageDialog(this, sp, "Favorites for " + userName, JOptionPane.INFORMATION_MESSAGE);
        });

        return panel;
    }

    // ---------------- MOOD PANEL ----------------
    private JPanel createMoodPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(235, 225, 255));

        JLabel heading = new JLabel("Select Your Mood 😊", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 22));
        heading.setForeground(new Color(80, 40, 150));

        String[][] moods = {
                { "😊 Happy", "happy" },
                { "😢 Sad", "sad" },
                { "😞 Depressed", "depressed" },
                { "😡 Frustrated", "frustrated" },
                { "😰 Stressed", "stressed" },
                { "😔 Lonely", "lonely" },
                { "😩 Helpless", "helpless" }
        };

        JPanel moodList = new JPanel(new GridLayout(moods.length, 1, 12, 12));
        moodList.setBackground(new Color(245, 240, 255));
        moodList.setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 100));

        for (String[] mood : moods) {
            JButton btn = new JButton(mood[0]);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(250, 250, 255));
            btn.addActionListener(e -> {
                selectedMood = mood[1];
                fetchAndShowQuote();
            });
            moodList.add(btn);
        }

        JButton backBtn = createStyledButton("⬅ Back", new Color(255, 120, 120));
        backBtn.setPreferredSize(new Dimension(100, 35));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        bottomPanel.setBackground(new Color(235, 225, 255));
        bottomPanel.add(backBtn);

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "welcome"));

        panel.add(heading, BorderLayout.NORTH);
        panel.add(moodList, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ---------------- QUOTE PANEL ----------------
    private JPanel createQuotePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(235, 225, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Your Quote 💭", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 22));
        heading.setForeground(new Color(90, 50, 160));

        JPanel quoteCard = new JPanel(new BorderLayout());
        quoteCard.setBackground(new Color(255, 255, 255, 230));
        quoteCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 220), 2, true),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)));
        quoteCard.setPreferredSize(new Dimension(500, 280));

        moodLabel = new JLabel("", SwingConstants.CENTER);
        moodLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        moodLabel.setForeground(new Color(100, 70, 160));

        quoteArea = new JTextArea();
        quoteArea.setFont(new Font("Serif", Font.PLAIN, 18));
        quoteArea.setForeground(Color.BLACK);
        quoteArea.setOpaque(false);
        quoteArea.setEditable(false);
        quoteArea.setLineWrap(true);
        quoteArea.setWrapStyleWord(true);
        quoteArea.setFocusable(false);
        quoteArea.setMargin(new Insets(10, 15, 10, 15));

        authorLabel = new JLabel("", SwingConstants.RIGHT);
        authorLabel.setFont(new Font("SansSerif", Font.ITALIC, 15));
        authorLabel.setForeground(Color.BLACK);

        quoteCard.add(moodLabel, BorderLayout.NORTH);
        quoteCard.add(quoteArea, BorderLayout.CENTER);
        quoteCard.add(authorLabel, BorderLayout.SOUTH);

        JButton backBtn = createStyledButton("Back", new Color(255, 120, 120));
        JButton addFavBtn = createStyledButton("Add to Favorites", new Color(100, 200, 100));

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "mood"));
        addFavBtn.addActionListener(e -> {
            boolean ok = saveFavoriteForCurrentUser();
            JOptionPane.showMessageDialog(this, ok ? "Added to favorites!" : "Failed to save favorite.");
        });

        JPanel btnRow = new JPanel(new FlowLayout());
        btnRow.add(backBtn);
        btnRow.add(addFavBtn);

        gbc.gridy = 0;
        panel.add(heading, gbc);
        gbc.gridy++;
        panel.add(quoteCard, gbc);
        gbc.gridy++;
        panel.add(btnRow, gbc);

        return panel;
    }

    // ---------------- FETCH QUOTE ----------------
    private void fetchAndShowQuote() {
        JDialog loading = new JDialog(this, "Fetching...", true);
        JLabel msg = new JLabel("Fetching quote for your mood...", SwingConstants.CENTER);
        msg.setFont(new Font("SansSerif", Font.PLAIN, 16));
        loading.add(msg);
        loading.setSize(300, 120);
        loading.setLocationRelativeTo(this);

        SwingWorker<String[], Void> worker = new SwingWorker<>() {
            @Override
            protected String[] doInBackground() {
                try {
                    return quoteService.getRandomQuote();
                } catch (Exception e) {
                    return moodQuotes.getOrDefault(selectedMood,
                            new String[] { "Keep moving forward.", "Unknown" });
                }
            }

            @Override
            protected void done() {
                loading.dispose();
                try {
                    String[] q = get();
                    moodLabel.setText("Mood: " + selectedMood.toUpperCase());
                    quoteArea.setText("“" + q[0] + "”");
                    authorLabel.setText("— " + q[1]);
                } catch (Exception e) {
                    quoteArea.setText("Error fetching quote.");
                }
                cardLayout.show(mainPanel, "quote");
            }
        };

        worker.execute();
        loading.setVisible(true);
    }

    // ---------------- UTILITIES ----------------
    private String readFavoritesFileContents() {
        try {
            if (!Files.exists(FAVORITES_PATH))
                return "";

            List<String> allLines = Files.readAllLines(FAVORITES_PATH);
            StringBuilder section = new StringBuilder();

            boolean inSection = false;
            for (String line : allLines) {
                if (line.startsWith("=== ") && line.endsWith(" ===")) {
                    inSection = line.equalsIgnoreCase("=== " + userName + " ===");
                    continue;
                }
                if (inSection)
                    section.append(line).append(System.lineSeparator());
            }

            return section.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "(Error reading favorites)";
        }
    }

    private boolean saveFavoriteForCurrentUser() {
        if (userName == null || userName.isEmpty())
            return false;

        String quote = quoteArea.getText().trim();
        String author = authorLabel.getText().replace("—", "").trim();
        String mood = selectedMood == null ? "unknown" : selectedMood;

        String entry = """
                Mood: %s
                  - %s %s

                """.formatted(mood, quote, author);

        try {
            List<String> allLines = new ArrayList<>();
            if (Files.exists(FAVORITES_PATH))
                allLines.addAll(Files.readAllLines(FAVORITES_PATH));

            List<String> newLines = new ArrayList<>();
            boolean foundUser = false;

            for (int i = 0; i < allLines.size(); i++) {
                String line = allLines.get(i);
                newLines.add(line);
                if (line.equalsIgnoreCase("=== " + userName + " ===")) {
                    foundUser = true;
                    while (i + 1 < allLines.size() && !allLines.get(i + 1).startsWith("=== ")) {
                        newLines.add(allLines.get(++i));
                    }
                    newLines.add(entry);
                }
            }

            if (!foundUser) {
                newLines.add("");
                newLines.add("=== " + userName + " ===");
                newLines.add(entry);
            }

            Files.write(FAVORITES_PATH, newLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 1, true));
        btn.setPreferredSize(new Dimension(150, 40));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MoodflixGUI().setVisible(true));
    }
}
