import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// ============================================================
//  QuizLogic.java — Contains Question class + all quiz logic
// ============================================================

// ----------------------------------------------------------
// Question class: holds one quiz question and its data
// ----------------------------------------------------------
class Question {
    String question;      // The question text
    String[] options;     // The 4 answer choices
    int correctAnswer;    // Index (0–3) of the correct option

    // Constructor to set up a question
    public Question(String question, String[] options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}

// ----------------------------------------------------------
// QuizLogic class: handles GUI + quiz logic + timer
// ----------------------------------------------------------
public class QuizLogic extends JFrame implements ActionListener {

    // ---- Quiz Data ----
    Question[] questions = {
            new Question(
                    "Q1. What is the full form of JVM?",
                    new String[]{"Java Virtual Machine", "Java Variable Method", "Java Verified Module", "Java Volatile Memory"},
                    0
            ),
            new Question(
                    "Q2. Which keyword is used to create an object in Java?",
                    new String[]{"create", "object", "new", "make"},
                    2
            ),
            new Question(
                    "Q3. What is the default value of an int variable in Java?",
                    new String[]{"null", "1", "undefined", "0"},
                    3
            ),
            new Question(
                    "Q4. Which of these is NOT a primitive data type in Java?",
                    new String[]{"int", "String", "char", "boolean"},
                    1
            ),
            new Question(
                    "Q5. Which method is the entry point of every Java program?",
                    new String[]{"start()", "run()", "main()", "init()"},
                    2
            ),
            new Question(
                    "Q6. What does OOP stand for?",
                    new String[]{"Object-Oriented Programming", "Open-Order Processing", "Object-Optimized Program", "Ordered Object Protocol"},
                    0
            ),
            new Question(
                    "Q7. Which symbol is used for single-line comments in Java?",
                    new String[]{"/* */", "<!--", "//", "#"},
                    2
            )
    };

    // ---- State Variables ----
    int currentQuestion = 0;   // Index of the question being shown
    int score = 0;             // User's current score
    int timeLeft = 10;         // Seconds remaining for current question

    // ---- GUI Components ----
    JLabel questionLabel;
    JLabel timerLabel;
    JLabel scoreLabel;
    JRadioButton[] options;
    ButtonGroup buttonGroup;
    JButton nextButton;

    // ---- Timer ----
    Timer timer;

    // ----------------------------------------------------------
    // Constructor: builds the entire GUI window
    // ----------------------------------------------------------
    public QuizLogic() {
        setTitle("Online Quiz Application");
        setSize(620, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // ---- Background panel with gradient ----
        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 40, 60),
                        0, getHeight(), new Color(15, 22, 40));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setBounds(0, 0, 620, 400);
        background.setLayout(null);
        add(background);

        // ---- Header bar ----
        JLabel header = new JLabel("  ★  JAVA QUIZ CHALLENGE", SwingConstants.LEFT);
        header.setBounds(0, 0, 620, 45);
        header.setFont(new Font("SansSerif", Font.BOLD, 17));
        header.setForeground(new Color(255, 215, 0));
        header.setOpaque(true);
        header.setBackground(new Color(20, 28, 50));
        background.add(header);

        // ---- Progress label ----
        scoreLabel = new JLabel("Question 1 of " + questions.length, SwingConstants.RIGHT);
        scoreLabel.setBounds(420, 8, 185, 28);
        scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        scoreLabel.setForeground(new Color(170, 200, 255));
        background.add(scoreLabel);

        // ---- Timer label ----
        timerLabel = new JLabel("⏱  10s", SwingConstants.CENTER);
        timerLabel.setBounds(460, 55, 140, 36);
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        timerLabel.setForeground(new Color(100, 230, 130));
        timerLabel.setOpaque(true);
        timerLabel.setBackground(new Color(25, 35, 55));
        timerLabel.setBorder(BorderFactory.createLineBorder(new Color(60, 80, 120), 1));
        background.add(timerLabel);

        // ---- Question label ----
        questionLabel = new JLabel("<html><body style='width:340px'></body></html>");
        questionLabel.setBounds(20, 55, 430, 70);
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        questionLabel.setForeground(Color.WHITE);
        background.add(questionLabel);

        // ---- Divider ----
        JSeparator sep = new JSeparator();
        sep.setBounds(20, 130, 580, 2);
        sep.setForeground(new Color(60, 80, 120));
        background.add(sep);

        // ---- Radio buttons ----
        options = new JRadioButton[4];
        buttonGroup = new ButtonGroup();

        int[] optionY = {145, 195, 245, 295};
        Color[] badgeColors = {
                new Color(70, 130, 200),
                new Color(70, 170, 130),
                new Color(200, 120, 60),
                new Color(180, 80, 140)
        };
        String[] letters = {"A", "B", "C", "D"};

        for (int i = 0; i < 4; i++) {
            // Badge (A/B/C/D)
            JLabel badge = new JLabel(letters[i], SwingConstants.CENTER);
            badge.setBounds(20, optionY[i], 28, 28);
            badge.setFont(new Font("SansSerif", Font.BOLD, 12));
            badge.setForeground(Color.WHITE);
            badge.setOpaque(true);
            badge.setBackground(badgeColors[i]);
            background.add(badge);

            // Radio button
            options[i] = new JRadioButton();
            options[i].setBounds(55, optionY[i], 530, 28);
            options[i].setFont(new Font("SansSerif", Font.PLAIN, 13));
            options[i].setForeground(new Color(220, 230, 255));
            options[i].setBackground(new Color(30, 40, 60));
            options[i].setFocusPainted(false);
            options[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            buttonGroup.add(options[i]);
            background.add(options[i]);
        }

        // ---- Next button ----
        nextButton = new JButton("Next  ›");
        nextButton.setBounds(460, 328, 135, 38);
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        nextButton.setForeground(Color.WHITE);
        nextButton.setBackground(new Color(50, 110, 200));
        nextButton.setFocusPainted(false);
        nextButton.setBorderPainted(false);
        nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextButton.addActionListener(this);
        background.add(nextButton);

        // ---- Tip label ----
        JLabel tip = new JLabel("Select an option then click Next (or wait for timer)");
        tip.setBounds(18, 340, 420, 18);
        tip.setFont(new Font("SansSerif", Font.ITALIC, 11));
        tip.setForeground(new Color(120, 140, 180));
        background.add(tip);

        // ---- Load first question ----
        loadQuestion(currentQuestion);

        // ---- Countdown timer (fires every 1 second) ----
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                updateTimerLabel();
                if (timeLeft <= 0) {
                    handleNext(false);   // Auto-advance when time runs out
                }
            }
        });
        timer.start();

        setVisible(true);
    }

    // ----------------------------------------------------------
    // updateTimerLabel: refreshes timer display + changes colour
    // ----------------------------------------------------------
    public void updateTimerLabel() {
        timerLabel.setText("⏱  " + timeLeft + "s");
        if (timeLeft <= 3) {
            timerLabel.setForeground(new Color(255, 80, 80));    // Red
        } else if (timeLeft <= 6) {
            timerLabel.setForeground(new Color(255, 190, 60));   // Orange
        } else {
            timerLabel.setForeground(new Color(100, 230, 130));  // Green
        }
    }

    // ----------------------------------------------------------
    // loadQuestion: populates GUI for question at given index
    // ----------------------------------------------------------
    public void loadQuestion(int index) {
        Question q = questions[index];
        questionLabel.setText("<html><body style='width:380px'>" + q.question + "</body></html>");
        for (int i = 0; i < 4; i++) {
            options[i].setText(q.options[i]);
            options[i].setSelected(false);
        }
        buttonGroup.clearSelection();
        scoreLabel.setText("Question " + (index + 1) + " of " + questions.length);
        timeLeft = 10;
        updateTimerLabel();
    }

    // ----------------------------------------------------------
    // handleNext: processes answer and advances to next question
    //   userClicked = true  → user pressed Next button
    //   userClicked = false → timer expired automatically
    // ----------------------------------------------------------
    public void handleNext(boolean userClicked) {
        timer.stop();

        // Find selected option (-1 if none selected)
        int selected = -1;
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                selected = i;
                break;
            }
        }

        // Warn user if they clicked Next without selecting anything
        if (userClicked && selected == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an answer before clicking Next!",
                    "No Answer Selected",
                    JOptionPane.WARNING_MESSAGE);
            timer.start();
            return;
        }

        // Award point if answer is correct
        if (selected == questions[currentQuestion].correctAnswer) {
            score++;
        }

        currentQuestion++;

        if (currentQuestion < questions.length) {
            loadQuestion(currentQuestion);
            timer.start();
        } else {
            showResult();
        }
    }

    // ----------------------------------------------------------
    // showResult: shows final score popup at end of quiz
    // ----------------------------------------------------------
    public void showResult() {
        int percent = (score * 100) / questions.length;

        String message;
        if (percent == 100) {
            message = "🏆 Perfect Score! Outstanding!";
        } else if (percent >= 70) {
            message = "🎉 Great job! Well done!";
        } else if (percent >= 40) {
            message = "👍 Good effort! Keep practising!";
        } else {
            message = "📚 Keep studying — you'll get there!";
        }

        String resultText = "Quiz Finished!\n\n"
                + "Your Score: " + score + " / " + questions.length + "\n"
                + "Percentage: " + percent + "%\n\n"
                + message;

        JOptionPane.showMessageDialog(this, resultText, "Quiz Result", JOptionPane.INFORMATION_MESSAGE);

        int restart = JOptionPane.showConfirmDialog(this,
                "Would you like to try again?",
                "Play Again?",
                JOptionPane.YES_NO_OPTION);

        if (restart == JOptionPane.YES_OPTION) {
            restartQuiz();
        } else {
            System.exit(0);
        }
    }

    // ----------------------------------------------------------
    // restartQuiz: resets everything and starts from question 1
    // ----------------------------------------------------------
    public void restartQuiz() {
        currentQuestion = 0;
        score = 0;
        loadQuestion(0);
        timer.start();
    }

    // ----------------------------------------------------------
    // actionPerformed: called when Next button is clicked
    // ----------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            handleNext(true);
        }
    }
}