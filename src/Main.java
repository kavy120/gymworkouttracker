import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        UserProfile profile = new UserProfile("User", "lb");
        WorkoutTracker tracker = new WorkoutTracker();

        // Auto-load if a save file exists (optional)
        tracker.tryAutoLoad();

        ConsoleUI ui = new ConsoleUI(profile, tracker, sc);
        ui.run();

        sc.close();
    }
}
