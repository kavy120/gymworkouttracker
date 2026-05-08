import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final UserProfile profile;
    private final WorkoutTracker tracker;
    private final Scanner sc;

    public ConsoleUI(UserProfile profile, WorkoutTracker tracker, Scanner sc) {
        this.profile = profile;
        this.tracker = tracker;
        this.sc = sc;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printHeader();
            System.out.println("1) Manage Exercises");
            System.out.println("2) Manage Workout Plans");
            System.out.println("3) Log Workout Session");
            System.out.println("4) View Workout History");
            System.out.println("5) View Exercise Progress");
            System.out.println("6) Save");
            System.out.println("7) Load");
            System.out.println("0) Exit");
            int choice = promptInt("Choose an option: ", 0, 7);

            switch (choice) {
                case 1: manageExercises();
                    break;
                case 2: managePlans();
                    break;
                case 3: logSession();
                    break;
                case 4: viewHistory();
                    break;
                case 5: viewProgress();
                    break;
                case 6: doSave();
                    break;
                case 7: doLoad();
                    break;
                case 0: running = !exitFlow();
                    break;
            }
        }
    }

    private void printHeader() {
        System.out.println("\n==========================================");
        System.out.println(" Gym Workout Planner & Progress Logger");
        System.out.println(" Units: " + profile.getUnitPreference());
        System.out.println("==========================================\n");
    }

    private void manageExercises() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Manage Exercises ---");
            System.out.println("1) Add Exercise");
            System.out.println("2) Edit Exercise");
            System.out.println("3) Delete Exercise");
            System.out.println("4) List Exercises");
            System.out.println("0) Back");
            int c = promptInt("Choose: ", 0, 4);

            switch (c) {
                case 1: addExercise();
                    break;
                case 2: editExercise();
                    break;
                case 3: deleteExercise();
                    break;
                case 4: listExercises();
                    break;
                case 0: back = true;
                    break;
            }
        }
    }

    private void addExercise() {
        System.out.println("\nAdd Exercise");
        String name = promptNonEmpty("Name: ");
        String mg = promptOptional("Muscle Group (optional): ");
        Exercise e = tracker.createExercise(name, mg);
        System.out.println("Added: " + e);
    }

    private void editExercise() {
        if (tracker.getExercises().isEmpty()) {
            System.out.println("No exercises found.");
            return;
        }
        listExercises();
        int id = promptInt("Enter exerciseId to edit: ", 1, Integer.MAX_VALUE);
        Exercise e = tracker.findExerciseById(id);
        if (e == null) {
            System.out.println("Exercise not found.");
            return;
        }
        String newName = promptNonEmpty("New name: ");
        String newMG = promptOptional("New muscle group (optional): ");
        e.updateInfo(newName, newMG);
        System.out.println("Updated: " + e);
    }

    private void deleteExercise() {
        if (tracker.getExercises().isEmpty()) {
            System.out.println("No exercises found.");
            return;
        }
        listExercises();
        int id = promptInt("Enter exerciseId to delete: ", 1, Integer.MAX_VALUE);

        if (tracker.isExerciseUsedInPlans(id)) {
            System.out.println("This exercise is used in a workout plan. Remove it from plans first.");
            return;
        }

        boolean ok = tracker.deleteExercise(id);
        System.out.println(ok ? "Deleted." : "Exercise not found.");
    }

    private void listExercises() {
        System.out.println("\nExercises:");
        if (tracker.getExercises().isEmpty()) {
            System.out.println("(none)");
            return;
        }
        for (Exercise e : tracker.getExercises()) {
            System.out.println("  " + e);
        }
    }

    private void managePlans() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Manage Workout Plans ---");
            System.out.println("1) Create Plan");
            System.out.println("2) Add Entry to Plan");
            System.out.println("3) Remove Entry from Plan");
            System.out.println("4) List Plans");
            System.out.println("0) Back");
            int c = promptInt("Choose: ", 0, 4);
            switch (c) {
                case 1: createPlan();
                    break;
                case 2: addEntryToPlan();
                    break;
                case 3: removeEntryFromPlan();
                    break;
                case 4: listPlans();
                    break;
                case 0: back = true;
                    break;
            }
        }
    }

    private void createPlan() {
        System.out.println("\nCreate Workout Plan");
        String name = promptNonEmpty("Plan name: ");
        WorkoutPlan plan = tracker.createPlan(name);
        System.out.println("Created: " + plan);
    }

    private void addEntryToPlan() {
        if (tracker.getPlans().isEmpty()) {
            System.out.println("No plans found. Create one first.");
            return;
        }
        if (tracker.getExercises().isEmpty()) {
            System.out.println("No exercises found. Add exercises first.");
            return;
        }

        listPlans();
        int planId = promptInt("Enter planId: ", 1, Integer.MAX_VALUE);
        WorkoutPlan plan = tracker.findPlanById(planId);
        if (plan == null) {
            System.out.println("Plan not found.");
            return;
        }

        listExercises();
        int exId = promptInt("Enter exerciseId to add: ", 1, Integer.MAX_VALUE);
        Exercise ex = tracker.findExerciseById(exId);
        if (ex == null) {
            System.out.println("Exercise not found.");
            return;
        }

        int sets = promptInt("Planned sets (>=1): ", 1, 100);
        int reps = promptInt("Planned reps (>=1): ", 1, 500);
        double weight = promptDouble("Planned weight (>=0): ", 0, 10000);

        WorkoutEntry entry = new WorkoutEntry(sets, reps, weight, exId);
        plan.addEntry(entry);
        System.out.println("Added entry to " + plan.getName() + ": " + entry + " -> " + ex.getName());
    }

    private void removeEntryFromPlan() {
        if (tracker.getPlans().isEmpty()) {
            System.out.println("No plans found.");
            return;
        }
        listPlans();
        int planId = promptInt("Enter planId: ", 1, Integer.MAX_VALUE);
        WorkoutPlan plan = tracker.findPlanById(planId);
        if (plan == null) {
            System.out.println("Plan not found.");
            return;
        }
        if (plan.getEntries().isEmpty()) {
            System.out.println("This plan has no entries.");
            return;
        }
        System.out.println("\nEntries in " + plan.getName() + ":");
        for (WorkoutEntry we : plan.getEntries()) {
            Exercise ex = tracker.findExerciseById(we.getExerciseId());
            String exName = (ex == null) ? "(deleted exercise)" : ex.getName();
            System.out.println("  entryId=" + we.getEntryId() + "  " + exName + "  " + we.plannedSummary());
        }
        int entryId = promptInt("Enter entryId to remove: ", 1, Integer.MAX_VALUE);
        boolean ok = plan.removeEntry(entryId);
        System.out.println(ok ? "Removed entry." : "Entry not found.");
    }

    private void listPlans() {
        System.out.println("\nWorkout Plans:");
        if (tracker.getPlans().isEmpty()) {
            System.out.println("(none)");
            return;
        }
        for (WorkoutPlan p : tracker.getPlans()) {
            System.out.println("  " + p);
        }
    }

    private void logSession() {
        if (tracker.getPlans().isEmpty()) {
            System.out.println("No workout plans found.");
            return;
        }
        listPlans();
        int planId = promptInt("Select planId to log: ", 1, Integer.MAX_VALUE);
        WorkoutPlan plan = tracker.findPlanById(planId);
        if (plan == null) {
            System.out.println("Plan not found.");
            return;
        }
        if (plan.getEntries().isEmpty()) {
            System.out.println("Selected plan has no entries. Add exercises first.");
            return;
        }

        LocalDate date = promptDate("Session date (YYYY-MM-DD) or blank for today: ");
        WorkoutSession session = tracker.logSession(planId, date);

        System.out.println("\nLogging session for: " + plan.getName() + " on " + date);
        for (WorkoutEntry we : plan.getEntries()) {
            Exercise ex = tracker.findExerciseById(we.getExerciseId());
            String exName = (ex == null) ? "(unknown)" : ex.getName();

            System.out.println("\nExercise: " + exName);
            System.out.println("Planned: " + we.plannedSummary());

            boolean confirm = promptYesNo("Use planned numbers? (y/n): ");
            int sets;
            int reps;
            double weight;

            if (confirm) {
                sets = we.getPlannedSets();
                reps = we.getPlannedReps();
                weight = we.getPlannedWeight();
            } else {
                sets = promptInt("Actual sets (>=0): ", 0, 100);
                reps = promptInt("Actual reps (>=0): ", 0, 500);
                weight = promptDouble("Actual weight (>=0): ", 0, 10000);
            }

            PerformedEntry pe = new PerformedEntry(
                    we.getPlannedSets(), we.getPlannedReps(), we.getPlannedWeight(),
                    we.getExerciseId(), sets, reps, weight
            );
            session.addPerformedEntry(pe);
        }

        tracker.finalizeLoggedSession(session);
        System.out.println("\nWorkout logged successfully.");
    }

    private void viewHistory() {
        List<WorkoutSession> history = tracker.getHistory();
        System.out.println("\nWorkout History:");
        if (history.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        for (WorkoutSession s : history) {
            WorkoutPlan plan = tracker.findPlanById(s.getPlanId());
            String planName = (plan == null) ? "(unknown plan)" : plan.getName();
            System.out.println("\nSessionId=" + s.getSessionId() + "  Date=" + s.getSessionDate() + "  Plan=" + planName);
            for (PerformedEntry pe : s.getPerformedEntries()) {
                Exercise ex = tracker.findExerciseById(pe.getExerciseId());
                String exName = (ex == null) ? "(unknown)" : ex.getName();
                System.out.println("  - " + exName + "  " + pe.actualSummary());
            }
        }
    }

    private void viewProgress() {
        if (tracker.getExercises().isEmpty()) {
            System.out.println("No exercises found.");
            return;
        }
        listExercises();
        int exId = promptInt("Select exerciseId: ", 1, Integer.MAX_VALUE);
        Exercise ex = tracker.findExerciseById(exId);
        if (ex == null) {
            System.out.println("Exercise not found.");
            return;
        }

        Double pr = tracker.getPR(exId);
        LocalDate last = tracker.getLastPerformed(exId);

        System.out.println("\nProgress for: " + ex.getName());
        System.out.println("Last performed: " + (last == null ? "(no history)" : last.toString()));
        System.out.println("Personal record (max weight): " + (pr == null ? "(no history)" : (pr + " " + profile.getUnitPreference())));
    }

    private void doSave() {
        boolean ok = tracker.saveToFile();
        System.out.println(ok ? "Saved." : "Save failed.");
    }

    private void doLoad() {
        boolean ok = tracker.loadFromFile();
        System.out.println(ok ? "Loaded." : "Load failed (file may not exist).");
    }

    private boolean exitFlow() {
        boolean save = promptYesNo("Save before exiting? (y/n): ");
        if (save) doSave();
        System.out.println("Goodbye!");
        return true;
    }

    private int promptInt(String msg, int min, int max) {
        while (true) {
            System.out.print(msg);
            String line = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < min || v > max) {
                    System.out.println("Enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return v;
            } catch (NumberFormatException ex) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    private double promptDouble(String msg, double min, double max) {
        while (true) {
            System.out.print(msg);
            String line = sc.nextLine().trim();
            try {
                double v = Double.parseDouble(line);
                if (v < min || v > max) {
                    System.out.println("Enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return v;
            } catch (NumberFormatException ex) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private String promptNonEmpty(String msg) {
        while (true) {
            System.out.print(msg);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Value cannot be empty.");
        }
    }

    private String promptOptional(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    private boolean promptYesNo(String msg) {
        while (true) {
            System.out.print(msg);
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("y") || s.equals("yes")) return true;
            if (s.equals("n") || s.equals("no")) return false;
            System.out.println("Please enter y/n.");
        }
    }

    private LocalDate promptDate(String msg) {
        while (true) {
            System.out.print(msg);
            String s = sc.nextLine().trim();
            if (s.isEmpty()) return LocalDate.now();
            try {
                return LocalDate.parse(s);
            } catch (Exception e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }
}
