package ph.edu.lspu.museum.model;

import java.util.ArrayList;
import java.util.List;

/**
 * One LSPU campus = one museum exhibition room.
 * Extends MuseumItem (INHERITANCE) and implements the abstract methods (POLYMORPHISM).
 */
public class Campus extends MuseumItem {

    private final String name;            // "LSPU Siniloan"
    private final String slug;            // used in URLs: /exhibition/siniloan
    private final String locationLabel;   // "Brgy. Wawa, Siniloan, Laguna"
    private final String town;            // used for sorting by location
    private final int foundingYear;
    private final String foundingNote;    // explains how the year is counted
    private final double latitude;        // approximate town-centre coordinates
    private final double longitude;
    private final String accent;          // colour taken from the official university seal symbolism
    private final String flagship;
    private final String tagline;
    private final String intro;

    private final CampusHistory history = new CampusHistory();
    private final List<HistoricalEvent> events = new ArrayList<>();
    private final List<CampusImage> images = new ArrayList<>();
    private final List<String> contributions = new ArrayList<>();
    private final List<String> programs = new ArrayList<>();

    public Campus(String slug, String name, String town, String locationLabel, int foundingYear,
                  String foundingNote, double latitude, double longitude, String accent,
                  String flagship, String tagline, String intro) {
        super(slug, name);
        this.slug = slug;
        this.name = name;
        this.town = town;
        this.locationLabel = locationLabel;
        this.foundingYear = foundingYear;
        this.foundingNote = foundingNote;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accent = accent;
        this.flagship = flagship;
        this.tagline = tagline;
        this.intro = intro;
    }

    public String getSlug() { return slug; }
    public String getName() { return name; }
    public String getTown() { return town; }
    public String getLocationLabel() { return locationLabel; }
    public int getFoundingYear() { return foundingYear; }
    public String getFoundingNote() { return foundingNote; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getAccent() { return accent; }
    public String getFlagship() { return flagship; }
    public String getTagline() { return tagline; }
    public String getIntro() { return intro; }
    public CampusHistory getHistory() { return history; }
    public List<HistoricalEvent> getEvents() { return events; }
    public List<CampusImage> getImages() { return images; }
    public List<String> getContributions() { return contributions; }
    public List<String> getPrograms() { return programs; }

    /** Number of gallery photographs that really exist on disk. */
    public int getAvailableImageCount() {
        int count = 0;
        for (CampusImage image : images) {
            if (image.isAvailable()) count++;
        }
        return count;
    }

    /** First real photograph, or null if the team has not added any yet. */
    public CampusImage getCoverImage() {
        for (CampusImage image : images) {
            if (image.isAvailable()) return image;
        }
        return null;
    }

    @Override public String getCategory() { return "Campus"; }

    @Override
    public String toSearchText() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(' ').append(town).append(' ').append(locationLabel).append(' ')
          .append(foundingYear).append(' ').append(flagship).append(' ')
          .append(tagline).append(' ').append(intro).append(' ')
          .append(history.toSearchText()).append(' ')
          .append(String.join(" ", contributions)).append(' ')
          .append(String.join(" ", programs));
        return sb.toString();
    }
}
