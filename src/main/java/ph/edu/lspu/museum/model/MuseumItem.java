package ph.edu.lspu.museum.model;

/**
 * ABSTRACTION: MuseumItem is an abstract base class. It cannot be instantiated
 * directly; it only describes what EVERY thing in the museum must be able to do.
 *
 * INHERITANCE: Campus, HistoricalEvent and CampusImage all "extend" this class,
 * so they reuse the id/title fields and the toSearchText() helper.
 */
public abstract class MuseumItem {

    // ENCAPSULATION: fields are private; outside code uses getters only.
    private final String id;
    private final String title;

    protected MuseumItem(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }

    /** Each subclass must say what kind of museum item it is (abstract method). */
    public abstract String getCategory();

    /**
     * POLYMORPHISM: each subclass builds its own searchable text, but the search
     * service can call toSearchText() on ANY MuseumItem without knowing its type.
     */
    public abstract String toSearchText();
}
