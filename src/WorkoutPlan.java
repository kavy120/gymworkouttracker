import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorkoutPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int NEXT_ID = 1;

    private int planId;
    private String name;
    private List<WorkoutEntry> entries;

    public WorkoutPlan(String name) {
        this.planId = NEXT_ID++;
        this.name = name;
        this.entries = new ArrayList<>();
    }

    public void addEntry(WorkoutEntry entry) {
        if (entry != null) entries.add(entry);
    }

    public boolean removeEntry(int entryId) {
        Iterator<WorkoutEntry> it = entries.iterator();
        while (it.hasNext()) {
            WorkoutEntry we = it.next();
            if (we.getEntryId() == entryId) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public List<WorkoutEntry> getEntries() { return entries; }
    public int getPlanId() { return planId; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "planId=" + planId + "  " + name + "  (entries=" + entries.size() + ")";
    }

    public static void resetNextId(int next) { if (next > 0) NEXT_ID = next; }
    public static int getNextId() { return NEXT_ID; }
}
