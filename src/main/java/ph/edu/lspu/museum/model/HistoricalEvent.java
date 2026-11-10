package ph.edu.lspu.museum.model;

/** One milestone on a campus timeline. */
public class HistoricalEvent extends MuseumItem {

    private final String campusSlug;
    private final int year;
    private final String dateLabel;   // e.g. "June 10, 1983" or "1952"
    private final String description;
    private final String source;      // where the fact came from
    private final boolean needsVerification;

    public HistoricalEvent(String campusSlug, int year, String dateLabel, String title,
                           String description, String source, boolean needsVerification) {
        super(campusSlug + "-" + year + "-" + Math.abs(title.hashCode()), title);
        this.campusSlug = campusSlug;
        this.year = year;
        this.dateLabel = dateLabel;
        this.description = description;
        this.source = source;
        this.needsVerification = needsVerification;
    }

    public String getCampusSlug() { return campusSlug; }
    public int getYear() { return year; }
    public String getDateLabel() { return dateLabel; }
    public String getDescription() { return description; }
    public String getSource() { return source; }
    public boolean isNeedsVerification() { return needsVerification; }

    @Override public String getCategory() { return "Milestone"; }

    @Override
    public String toSearchText() {
        return getTitle() + " " + dateLabel + " " + description;
    }
}
