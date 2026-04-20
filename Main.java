import javax.swing.SwingUtilities;

// ============================================================
//  Main.java — Entry point of the Online Quiz Application
//  Run this file to start the quiz.
// ============================================================

public class Main {

    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (Swing best-practice)
        // This creates the QuizLogic window which handles everything
        SwingUtilities.invokeLater(() -> new QuizLogic());
    }
}