package ph.edu.lspu.museum.model;

import java.util.ArrayList;
import java.util.List;

/** The "wall text" of an exhibition: background paragraphs, former names, and research notes. */
public class CampusHistory {

    private final List<String> background = new ArrayList<>();       // Java Collection: ArrayList
    private final List<String> formerNames = new ArrayList<>();
    private final List<String> needsResearch = new ArrayList<>();    // honest list of gaps

    public CampusHistory addBackground(String paragraph) { background.add(paragraph); return this; }
    public CampusHistory addFormerName(String name) { formerNames.add(name); return this; }
    public CampusHistory addResearchNote(String note) { needsResearch.add(note); return this; }

    public List<String> getBackground() { return background; }
    public List<String> getFormerNames() { return formerNames; }
    public List<String> getNeedsResearch() { return needsResearch; }

    public String toSearchText() {
        return String.join(" ", background) + " " + String.join(" ", formerNames);
    }
}
