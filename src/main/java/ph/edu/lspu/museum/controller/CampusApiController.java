package ph.edu.lspu.museum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ph.edu.lspu.museum.model.Campus;
import ph.edu.lspu.museum.service.CampusManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON endpoint used ONLY by the Campus Navigator map. The data and the distance
 * calculations come from the Java backend; JavaScript just draws them.
 */
@RestController
@RequestMapping("/api")
public class CampusApiController {

    private final CampusManager manager;

    public CampusApiController(CampusManager manager) {
        this.manager = manager;
    }

    @GetMapping("/navigator")
    public Map<String, Object> navigator() {
        List<Map<String, Object>> nodes = new ArrayList<>();
        for (Campus c : manager.getAll()) {
            Map<String, Object> n = new LinkedHashMap<>();
            n.put("slug", c.getSlug());
            n.put("name", c.getName());
            n.put("location", c.getLocationLabel());
            n.put("year", c.getFoundingYear());
            n.put("flagship", c.getFlagship());
            n.put("tagline", c.getTagline());
            n.put("lat", c.getLatitude());
            n.put("lon", c.getLongitude());
            n.put("accent", c.getAccent());
            n.put("cover", c.getCoverImage() == null ? null : c.getCoverImage().getUrl());
            nodes.add(n);
        }
        List<Map<String, Object>> links = new ArrayList<>();
        List<Campus> all = manager.getAll();
        for (int i = 0; i < all.size(); i++) {
            for (int j = i + 1; j < all.size(); j++) {
                Map<String, Object> l = new LinkedHashMap<>();
                l.put("from", all.get(i).getSlug());
                l.put("to", all.get(j).getSlug());
                l.put("km", Math.round(manager.distanceKm(all.get(i), all.get(j)) * 10) / 10.0);
                links.add(l);
            }
        }
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("nodes", nodes);
        out.put("links", links);
        out.put("note", "Straight-line distances between approximate town-centre coordinates, not road distances.");
        return out;
    }
}
