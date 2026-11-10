package ph.edu.lspu.museum.service;

import org.springframework.stereotype.Service;
import ph.edu.lspu.museum.data.CampusDataLoader;
import ph.edu.lspu.museum.model.Campus;
import ph.edu.lspu.museum.model.HistoricalEvent;
import ph.edu.lspu.museum.sorting.CampusSorter;
import ph.edu.lspu.museum.sorting.CampusSorter.SortField;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CampusManager owns the list of campuses and offers the operations the controllers need.
 * It is a Spring "service": Spring creates ONE instance and injects it where needed
 * (DEPENDENCY INJECTION through the constructor).
 */
@Service
public class CampusManager {

    private final List<Campus> campuses;          // Java Collection: ArrayList
    private final CampusSorter sorter = new CampusSorter();

    public CampusManager(CampusDataLoader loader) {
        this.campuses = loader.loadCampuses();
    }

    /** Museum room order (the "hallway"): Siniloan -> Santa Cruz -> Los Baños -> San Pablo. */
    public List<Campus> getAll() { return campuses; }

    public List<Campus> getSorted(SortField field) {
        return sorter.insertionSort(campuses, field);
    }

    public Optional<Campus> findBySlug(String slug) {
        for (Campus campus : campuses) {
            if (campus.getSlug().equals(slug)) return Optional.of(campus);
        }
        return Optional.empty();
    }

    public Campus previousRoom(Campus current) {
        int i = campuses.indexOf(current);
        return campuses.get((i - 1 + campuses.size()) % campuses.size());
    }

    public Campus nextRoom(Campus current) {
        int i = campuses.indexOf(current);
        return campuses.get((i + 1) % campuses.size());
    }

    /** Every milestone of every campus, sorted by year with our own Selection Sort. */
    public List<HistoricalEvent> getAllEventsChronological() {
        List<HistoricalEvent> all = new ArrayList<>();
        for (Campus campus : campuses) all.addAll(campus.getEvents());
        return sorter.selectionSortEvents(all);
    }

    public List<HistoricalEvent> getEventsSorted(Campus campus) {
        return sorter.selectionSortEvents(campus.getEvents());
    }

    /** Great-circle distance between two campuses in kilometres (Haversine formula). */
    public double distanceKm(Campus a, Campus b) {
        final double earthRadiusKm = 6371.0;
        double dLat = Math.toRadians(b.getLatitude() - a.getLatitude());
        double dLon = Math.toRadians(b.getLongitude() - a.getLongitude());
        double h = Math.pow(Math.sin(dLat / 2), 2)
                 + Math.cos(Math.toRadians(a.getLatitude())) * Math.cos(Math.toRadians(b.getLatitude()))
                 * Math.pow(Math.sin(dLon / 2), 2);
        return earthRadiusKm * 2 * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h));
    }
}
