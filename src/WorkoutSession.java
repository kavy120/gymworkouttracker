import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkoutSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int NEXT_ID = 1;

    private int sessionId;
    private LocalDate sessionDate;
    private int planId;
    private List<PerformedEntry> performedEntries;

    public WorkoutSession(int planId, LocalDate sessionDate) {
        this.sessionId = NEXT_ID++;
        this.planId = planId;
        this.sessionDate = sessionDate;
        this.performedEntries = new ArrayList<>();
    }

    public void addPerformedEntry(PerformedEntry pe) {
        if (pe != null) performedEntries.add(pe);
    }

    public int getSessionId() { return sessionId; }
    public LocalDate getSessionDate() { return sessionDate; }
    public int getPlanId() { return planId; }
    public List<PerformedEntry> getPerformedEntries() { return performedEntries; }

    public static void resetNextId(int next) { if (next > 0) NEXT_ID = next; }
    public static int getNextId() { return NEXT_ID; }
}
