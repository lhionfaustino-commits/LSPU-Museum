package ph.edu.lspu.museum.sorting;

import ph.edu.lspu.museum.model.Campus;
import ph.edu.lspu.museum.model.HistoricalEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * MANUALLY IMPLEMENTED SORTING ALGORITHMS (no Collections.sort / List.sort used).
 *
 *  - Insertion Sort  -> sorts campuses by name / founding year / location
 *  - Selection Sort  -> sorts historical events by year
 *  - Bubble Sort     -> available as a third demonstration algorithm
 *
 * Each method works on a COPY of the list, so the original list is never changed.
 */
public class CampusSorter {

    /** What the visitor can sort by. */
    public enum SortField {
        NAME, YEAR, LOCATION;

        /** Converts text from the URL (?sort=year) into an enum value; defaults to NAME. */
        public static SortField fromText(String text) {
            if (text == null) return NAME;
            switch (text.toLowerCase()) {
                case "year":     return YEAR;
                case "location": return LOCATION;
                default:         return NAME;
            }
        }
    }

    /** Compares two campuses on the chosen field. Negative = a comes first. */
    private int compare(Campus a, Campus b, SortField field) {
        switch (field) {
            case YEAR:
                if (a.getFoundingYear() != b.getFoundingYear()) {
                    return Integer.compare(a.getFoundingYear(), b.getFoundingYear());
                }
                return a.getName().compareToIgnoreCase(b.getName()); // tie-breaker
            case LOCATION:
                return a.getTown().compareToIgnoreCase(b.getTown());
            default:
                return a.getName().compareToIgnoreCase(b.getName());
        }
    }

    // ---------------------------------------------------------------- INSERTION SORT
    /** Insertion sort: take each item and slide it left until it sits in the right place. */
    public List<Campus> insertionSort(List<Campus> source, SortField field) {
        List<Campus> list = new ArrayList<>(source);
        for (int i = 1; i < list.size(); i++) {
            Campus key = list.get(i);
            int j = i - 1;
            while (j >= 0 && compare(list.get(j), key, field) > 0) {
                list.set(j + 1, list.get(j));   // shift bigger item to the right
                j--;
            }
            list.set(j + 1, key);
        }
        return list;
    }

    // ---------------------------------------------------------------- BUBBLE SORT
    /** Bubble sort: repeatedly swap neighbours that are in the wrong order. */
    public List<Campus> bubbleSort(List<Campus> source, SortField field) {
        List<Campus> list = new ArrayList<>(source);
        boolean swapped = true;
        for (int pass = 0; pass < list.size() - 1 && swapped; pass++) {
            swapped = false;
            for (int i = 0; i < list.size() - 1 - pass; i++) {
                if (compare(list.get(i), list.get(i + 1), field) > 0) {
                    Campus temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                    swapped = true;
                }
            }
        }
        return list;
    }

    // ---------------------------------------------------------------- SELECTION SORT
    /** Selection sort: find the smallest remaining event and move it to the front. */
    public List<HistoricalEvent> selectionSortEvents(List<HistoricalEvent> source) {
        List<HistoricalEvent> list = new ArrayList<>(source);
        for (int i = 0; i < list.size() - 1; i++) {
            int min = i;
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j).getYear() < list.get(min).getYear()) {
                    min = j;
                }
            }
            if (min != i) {
                HistoricalEvent temp = list.get(i);
                list.set(i, list.get(min));
                list.set(min, temp);
            }
        }
        return list;
    }
}
