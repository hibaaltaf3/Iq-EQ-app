import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class QuizAppGUI extends JFrame implements ActionListener {
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int iqScore = 0;
    private int eqScore = 0;
    private int totalQuestions = 10; // Number of questions per round
    private String userName;
    private int userAge;
    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionGroup;
    private JButton nextButton;
    private JPanel inputPanel;
    private JTextField nameField;
    private JTextField ageField;

    public QuizAppGUI() {
        questions = loadQuestions();

        setTitle("Professional Quiz Application");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input panel for name and age
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);
        nextButton = new JButton("Start Quiz");
        nextButton.setBackground(Color.BLUE);
        nextButton.setForeground(Color.WHITE);
        nextButton.addActionListener(e -> startQuiz());
        inputPanel.add(nextButton);
        add(inputPanel, BorderLayout.CENTER);

        // Style the input panel
        inputPanel.setBackground(new Color(230, 230, 250)); // Light lavender background
        setVisible(true);
    }

private List<Question> loadQuestions() {
    List<Question> questionList = new ArrayList<>();

    // IQ Questions
    questionList.add(new Question(
            "If the sum of three consecutive integers is 33, what is the largest number?",
            new String[]{"10", "11", "12", "13"}, "12", "IQ"));
    questionList.add(new Question(
            "Which number does not belong in the series: 2, 6, 12, 20, 30, 42?",
            new String[]{"6", "20", "30", "42"}, "20", "IQ"));
    questionList.add(new Question(
            "A clock strikes once at 1 o'clock, twice at 2 o'clock, and so on. How many times does it strike in a day?",
            new String[]{"78", "156", "144", "120"}, "156", "IQ"));
    questionList.add(new Question(
            "Solve: 3x + 5 = 20. What is x?",
            new String[]{"5", "10", "15", "20"}, "5", "IQ"));
    questionList.add(new Question(
            "What is the square root of 144?",
            new String[]{"10", "11", "12", "13"}, "12", "IQ"));

    // EQ Questions
    questionList.add(new Question(
            "You and a team member disagree on a strategy. How do you handle it?",
            new String[]{"Avoid them", "Discuss calmly", "Report them", "Ignore it"}, "Discuss calmly", "EQ"));
    questionList.add(new Question(
            "A friend cancels plans at the last minute. What is your response?",
            new String[]{"Confront them angrily", "Understand their situation", "Avoid them", "Complain to others"}, "Understand their situation", "EQ"));
    questionList.add(new Question(
            "You see someone being treated unfairly. What do you do?",
            new String[]{"Ignore it", "Stand up for them", "Join the bully", "Record it"}, "Stand up for them", "EQ"));
    questionList.add(new Question(
            "Your team has failed a project. How do you react?",
            new String[]{"Blame others", "Take responsibility", "Give up", "Argue"}, "Take responsibility", "EQ"));
    questionList.add(new Question(
            "How would you handle criticism from a superior?",
            new String[]{"Get defensive", "Analyze their feedback", "Ignore it", "Blame others"}, "Analyze their feedback", "EQ"));

    Collections.shuffle(questionList);
    return questionList.subList(0, totalQuestions);
}


    private void startQuiz() {
        try {
            userName = nameField.getText();
            userAge = Integer.parseInt(ageField.getText());

            if (userName.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty.");
            }

            // Remove input panel and set up quiz components
            remove(inputPanel);
            setupQuizUI();
            displayQuestion();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupQuizUI() {
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel questionPanel = new JPanel();
        questionPanel.add(questionLabel);
        add(questionPanel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1));
        optionButtons = new JRadioButton[4];
        optionGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setBackground(new Color(230, 230, 250)); // Light lavender background for options
            optionGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        add(optionsPanel, BorderLayout.CENTER);

        nextButton = new JButton("Next");
        nextButton.setBackground(Color.BLUE);
        nextButton.setForeground(Color.WHITE);
        nextButton.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Style the main frame
        getContentPane().setBackground(new Color(230, 230, 250)); // Light lavender background
        setVisible(true);
    }

    private void displayQuestion() {
        if (currentQuestionIndex < totalQuestions) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionLabel.setText((currentQuestionIndex + 1) + ". " + currentQuestion.getQuestionText());

            String[] options = currentQuestion.getOptions();
            for (int i = 0; i < optionButtons.length; i++) {
                optionButtons[i].setText(options[i]);
                optionButtons[i].setActionCommand(options[i]);
            }
        } else {
            showResults();
        }
    }

    private void showResults() {
        int iqPercentage = (iqScore * 100) / totalQuestions;
        int eqPercentage = (eqScore * 100) / totalQuestions;

        String personalityType = "";
        String message = "";
        String careerRecommendation = "";

        // Determine personality type, message, and career based on scores
        if (iqPercentage > eqPercentage) {
            personalityType = "Logical Thinker";
            message = "You have a strong analytical mind. Continue honing your problem-solving skills!";
            careerRecommendation = "Recommended Careers: Software Developer, Data Analyst, Engineer, Research Scientist, Mathematician.";
        } else if (eqPercentage > iqPercentage) {
            personalityType = "Emotionally Intelligent";
            message = "You have a great understanding of emotions. You are compassionate and empathetic!";
            careerRecommendation = "Recommended Careers: Psychologist, Social Worker, Human Resources, Teacher, Customer Relations.";
        } else {
            personalityType = "Balanced Thinker";
            message = "You possess a good balance of logic and empathy. Both skills are valuable in various situations!";
            careerRecommendation = "Recommended Careers: Project Manager, Consultant, Mediator, Marketing Specialist, Teacher.";
        }

        String resultText = "Name: " + userName + "\n" +
                "Age: " + userAge + "\n" +
                "IQ Score: " + iqPercentage + "%\n" +
                "EQ Score: " + eqPercentage + "%\n" +
                "Personality Type: " + personalityType + "\n" +
                "Message: " + message + "\n" +
                careerRecommendation;

        JOptionPane.showMessageDialog(this, resultText, "Quiz Results", JOptionPane.INFORMATION_MESSAGE);

        try (FileWriter writer = new FileWriter("Quiz_Results.txt")) {
            writer.write(resultText);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedOption = optionGroup.getSelection().getActionCommand();
        Question currentQuestion = questions.get(currentQuestionIndex);

        if (selectedOption.equals(currentQuestion.getCorrectAnswer())) {
            if (currentQuestion.getType().equals("IQ")) iqScore++;
            else eqScore++;
        }
        currentQuestionIndex++;
        optionGroup.clearSelection();
        displayQuestion();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizAppGUI::new);
    }

    private static class Question {
        private final String questionText;
        private final String[] options;
        private final String correctAnswer;
        private final String type;

        public Question(String questionText, String[] options, String correctAnswer, String type) {
            this.questionText = questionText;
            this.options = options;
            this.correctAnswer = correctAnswer;
            this.type = type;
        }

        public String getQuestionText() {
            return questionText;
        }

        public String[] getOptions() {
            return options;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public String getType() {
            return type;
        }
    }
}