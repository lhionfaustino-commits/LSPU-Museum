package ph.edu.lspu.museum.model;

/** One line in the search results page. A simple "data holder" class. */
public class SearchResult {
    private final String campusSlug;
    private final String campusName;
    private final String kind;     // Campus / Milestone
    private final String title;
    private final String snippet;
    private final int score;       // higher = better match

    public SearchResult(String campusSlug, String campusName, String kind, String title, String snippet, int score) {
        this.campusSlug = campusSlug;
        this.campusName = campusName;
        this.kind = kind;
        this.title = title;
        this.snippet = snippet;
        this.score = score;
    }

    public String getCampusSlug() { return campusSlug; }
    public String getCampusName() { return campusName; }
    public String getKind() { return kind; }
    public String getTitle() { return title; }
    public String getSnippet() { return snippet; }
    public int getScore() { return score; }
}
