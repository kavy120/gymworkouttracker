import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WorkoutTracker implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<WorkoutPlan> plans = new ArrayList<>();
    private List<WorkoutSession> sessions = new ArrayList<>();
    private List<Exercise> exercises = new ArrayList<>();

    private static final String DATA_FILE = "workout_data.ser";
    private static int NEXT_EXERCISE_ID = 1;

    public Exercise createExercise(String name, String muscleGroup) {
        Exercise e = new Exercise(NEXT_EXERCISE_ID++, name.trim(), (muscleGroup == null) ? "" : muscleGroup.trim());
        exercises.add(e);
        return e;
    }

    public WorkoutPlan createPlan(String name) {
        WorkoutPlan p = new WorkoutPlan(name.trim());
        plans.add(p);
        return p;
    }

    public WorkoutSession logSession(int planId, LocalDate date) {
        return new WorkoutSession(planId, date);
    }

    public void finalizeLoggedSession(WorkoutSession session) {
        sessions.add(session);
        sessions.sort(Comparator.comparing(WorkoutSession::getSessionDate).thenComparing(WorkoutSession::getSessionId));
    }

    public List<WorkoutSession> getHistory() { return sessions; }
    public List<WorkoutPlan> getPlans() { return plans; }
    public List<Exercise> getExercises() { return exercises; }

    public Double getPR(int exerciseId) {
        Double best = null;
        for (WorkoutSession s : sessions) {
            for (PerformedEntry pe : s.getPerformedEntries()) {
                if (pe.getExerciseId() == exerciseId) {
                    double w = pe.getActualWeight();
                    if (best == null || w > best) best = w;
                }
            }
        }
        return best;
    }

    public LocalDate getLastPerformed(int exerciseId) {
        LocalDate last = null;
        for (WorkoutSession s : sessions) {
            for (PerformedEntry pe : s.getPerformedEntries()) {
                if (pe.getExerciseId() == exerciseId) {
                    LocalDate d = s.getSessionDate();
                    if (last == null || d.isAfter(last)) last = d;
                }
            }
        }
        return last;
    }

    public Exercise findExerciseById(int id) {
        for (Exercise e : exercises) if (e.getExerciseId() == id) return e;
        return null;
    }

    public WorkoutPlan findPlanById(int id) {
        for (WorkoutPlan p : plans) if (p.getPlanId() == id) return p;
        return null;
    }

    public boolean deleteExercise(int id) {
        Exercise e = findExerciseById(id);
        if (e == null) return false;
        exercises.remove(e);
        return true;
    }

    public boolean isExerciseUsedInPlans(int exerciseId) {
        for (WorkoutPlan p : plans) {
            for (WorkoutEntry we : p.getEntries()) {
                if (we.getExerciseId() == exerciseId) return true;
            }
        }
        return false;
    }

    public boolean saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(this);
            out.writeInt(NEXT_EXERCISE_ID);
            out.writeInt(WorkoutPlan.getNextId());
            out.writeInt(WorkoutEntry.getNextId());
            out.writeInt(WorkoutSession.getNextId());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean loadFromFile() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return false;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            WorkoutTracker loaded = (WorkoutTracker) in.readObject();

            this.plans = loaded.plans;
            this.sessions = loaded.sessions;
            this.exercises = loaded.exercises;

            NEXT_EXERCISE_ID = in.readInt();
            WorkoutPlan.resetNextId(in.readInt());
            WorkoutEntry.resetNextId(in.readInt());
            WorkoutSession.resetNextId(in.readInt());

            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    public void tryAutoLoad() { loadFromFile(); }
}
