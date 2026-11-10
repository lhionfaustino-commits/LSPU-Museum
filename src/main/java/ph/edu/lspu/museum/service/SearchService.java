package ph.edu.lspu.museum.service;

import org.springframework.stereotype.Service;
import ph.edu.lspu.museum.model.Campus;
import ph.edu.lspu.museum.model.HistoricalEvent;
import ph.edu.lspu.museum.model.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Search using Java Regular Expressions (java.util.regex).
 *
 * 1. INPUT VALIDATION: only letters, digits, spaces and a few punctuation marks, 1-60 chars.
 * 2. TOKENISING: the query is split into words with a regex.
 * 3. MATCHING: every word becomes a case-insensitive Pattern; campuses and milestones
 *    are scored by how many words match.
 */
@Service
public class SearchService {

    /** Allowed characters in a search box. \p{L} = any letter (so "Baños" works). */
    private static final Pattern VALID_QUERY = Pattern.compile("^[\\p{L}\\p{N} .,'&\\-]{1,60}$");
    /** A four-digit year from 1900 to 2099. */
    private static final Pattern YEAR = Pattern.compile("^(19|20)\\d{2}$");
    /** Splits the query on any run of spaces/punctuation. */
    private static final Pattern SPLIT = Pattern.compile("[\\s.,&\\-]+");

    private final CampusManager manager;

    public SearchService(CampusManager manager) {
        this.manager = manager;
    }

    public boolean isValid(String query) {
        return query != null && VALID_QUERY.matcher(query.trim()).matches();
    }

    public List<SearchResult> search(String query) {
        List<SearchResult> results = new ArrayList<>();
        if (!isValid(query)) return results;

        String[] words = SPLIT.split(query.trim());
        List<Pattern> patterns = new ArrayList<>();
        for (String word : words) {
            if (word.isEmpty()) continue;
            // Pattern.quote makes sure special characters are treated as plain text.
            patterns.add(Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
        }
        if (patterns.isEmpty()) return results;

        for (Campus campus : manager.getAll()) {
            int score = score(campus.toSearchText(), patterns);
            // Founding year gets a bonus when the whole query is a year.
            if (YEAR.matcher(query.trim()).matches() && campus.getFoundingYear() == Integer.parseInt(query.trim())) {
                score += 5;
            }
            if (score > 0) {
                results.add(new SearchResult(campus.getSlug(), campus.getName(), "Campus exhibition",
                        campus.getName() + " (" + campus.getFoundingYear() + ")",
                        campus.getTagline(), score + 3));
            }
            for (HistoricalEvent event : campus.getEvents()) {
                int eventScore = score(event.toSearchText(), patterns);
                if (YEAR.matcher(query.trim()).matches() && event.getYear() == Integer.parseInt(query.trim())) {
                    eventScore += 5;
                }
                if (eventScore > 0) {
                    results.add(new SearchResult(campus.getSlug(), campus.getName(), "Milestone",
                            event.getDateLabel() + " — " + event.getTitle(),
                            event.getDescription(), eventScore));
                }
            }
        }
        sortByScoreDescending(results);
        return results;
    }

    /** Counts how many query words appear in the text. */
    private int score(String text, List<Pattern> patterns) {
        int total = 0;
        for (Pattern p : patterns) {
            Matcher m = p.matcher(text);
            while (m.find()) total++;
        }
        return total;
    }

    /** Insertion sort by score (highest first) - again our own algorithm. */
    private void sortByScoreDescending(List<SearchResult> list) {
        for (int i = 1; i < list.size(); i++) {
            SearchResult key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).getScore() < key.getScore()) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }
}
