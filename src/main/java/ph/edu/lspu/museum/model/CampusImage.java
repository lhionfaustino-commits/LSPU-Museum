package ph.edu.lspu.museum.model;

/**
 * A framed photograph in a campus gallery.
 *
 * IMPORTANT: no fake photographs are bundled. The "available" flag is set at startup
 * by checking whether the real image file exists in static/images/campuses/<slug>/.
 * If it does not, the page shows an honest "photograph pending" placard instead.
 */
public class CampusImage extends MuseumItem {

    private final String fileName;     // e.g. main-gate.jpg
    private final String kind;         // Current / Landmark / Historical / Institutional
    private final String description;
    private String credit = "To be provided by the project team";
    private boolean available;

    public CampusImage(String campusSlug, String fileName, String title, String kind, String description) {
        super(campusSlug + "/" + fileName, title);
        this.fileName = fileName;
        this.kind = kind;
        this.description = description;
    }

    public String getFileName() { return fileName; }
    public String getKind() { return kind; }
    public String getDescription() { return description; }
    public String getCredit() { return credit; }
    public void setCredit(String credit) { this.credit = credit; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    /** Web path used by the template, e.g. /images/campuses/siniloan/main-gate.jpg */
    public String getUrl() { return "/images/campuses/" + getId(); }

    @Override public String getCategory() { return "Photograph"; }

    @Override
    public String toSearchText() { return getTitle() + " " + kind + " " + description; }
}
