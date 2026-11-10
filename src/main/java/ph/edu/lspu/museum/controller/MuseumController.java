package ph.edu.lspu.museum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import ph.edu.lspu.museum.model.Campus;
import ph.edu.lspu.museum.service.CampusManager;
import ph.edu.lspu.museum.service.SearchService;
import ph.edu.lspu.museum.sorting.CampusSorter.SortField;

/** Controller = receives a web request, prepares data in the Model, returns a template name. */
@Controller
public class MuseumController {

    private final CampusManager manager;
    private final SearchService searchService;

    public MuseumController(CampusManager manager, SearchService searchService) {
        this.manager = manager;
        this.searchService = searchService;
    }

    /** Entrance + lobby. Optional ?sort=name|year|location sorts the four exhibition entrances. */
    @GetMapping("/")
    public String lobby(@RequestParam(defaultValue = "") String sort, Model model) {
        SortField field = SortField.fromText(sort);
        model.addAttribute("campuses", sort.isEmpty() ? manager.getAll() : manager.getSorted(field));
        model.addAttribute("sort", sort.isEmpty() ? "" : field.name().toLowerCase());
        model.addAttribute("page", "lobby");
        return "index";
    }

    @GetMapping("/exhibition/{slug}")
    public String exhibition(@PathVariable String slug, Model model) {
        Campus campus = manager.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such exhibition room"));
        int index = manager.getAll().indexOf(campus);
        model.addAttribute("campus", campus);
        model.addAttribute("events", manager.getEventsSorted(campus));
        model.addAttribute("prev", manager.previousRoom(campus));
        model.addAttribute("next", manager.nextRoom(campus));
        model.addAttribute("roomNumber", index + 1);
        model.addAttribute("campuses", manager.getAll());
        model.addAttribute("page", "exhibitions");
        return "exhibition";
    }

    @GetMapping("/timeline")
    public String timeline(Model model) {
        model.addAttribute("events", manager.getAllEventsChronological());
        model.addAttribute("campuses", manager.getAll());
        model.addAttribute("page", "timeline");
        return "timeline";
    }

    @GetMapping("/navigator")
    public String navigator(Model model) {
        model.addAttribute("campuses", manager.getAll());
        model.addAttribute("page", "navigator");
        return "navigator";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("campuses", manager.getAll());
        model.addAttribute("page", "about");
        return "about";
    }

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "") String q, Model model) {
        String query = q.trim();
        boolean valid = query.isEmpty() || searchService.isValid(query);
        model.addAttribute("q", query);
        model.addAttribute("valid", valid);
        model.addAttribute("results", valid && !query.isEmpty() ? searchService.search(query) : java.util.List.of());
        model.addAttribute("campuses", manager.getAll());
        model.addAttribute("page", "search");
        return "search";
    }
}
