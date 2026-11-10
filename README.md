# LSPU Heritage Museum

An immersive digital museum for Laguna State Polytechnic University: a cinematic entrance, a main lobby with four
exhibition entrances, one gallery room per campus (history wall, interactive timeline, framed photo gallery with
lightbox, contributions, programs), a museum-wide timeline, a campus navigator map, regex-powered search, and a dedicated About Us page.

**Stack:** Java 17 · Spring Boot 3.2 · Thymeleaf · HTML/CSS/JS · GSAP + ScrollTrigger (CDN). MySQL is optional and
not needed (data lives in memory).

## Run it

```bash
mvn spring-boot:run        # then open http://localhost:8080
```

Needs internet on first load for Google Fonts and GSAP (cdnjs). Without GSAP the site still works, just without motion.

## Project structure

```
src/main/java/ph/edu/lspu/museum/
  MuseumApplication.java
  model/       MuseumItem (abstract) · Campus · CampusHistory · CampusImage · HistoricalEvent · SearchResult
  data/        CampusDataLoader        <- ALL historical content and its sources live here
  service/     CampusManager · SearchService
  sorting/     CampusSorter            <- manual Insertion / Bubble / Selection sort
  controller/  MuseumController (pages) · CampusApiController (/api/navigator JSON)
src/main/resources/
  templates/   index · exhibition · timeline · navigator · about · search · fragments/layout
  static/css/museum.css   static/js/museum.js   static/images/campuses/<slug>/
```

## Where each Java requirement is shown

| Requirement | Where |
|---|---|
| Abstraction | `MuseumItem` (abstract class, abstract `getCategory()` / `toSearchText()`) |
| Inheritance | `Campus`, `HistoricalEvent`, `CampusImage` extend `MuseumItem` |
| Polymorphism | each subclass overrides `toSearchText()` / `getCategory()` |
| Encapsulation | private fields + getters in every model class |
| Collections | `ArrayList` for campuses, events, images, contributions, programs |
| Regex | `SearchService` (input validation `VALID_QUERY`, year pattern, tokeniser, case-insensitive matching) |
| Manual sorting | `CampusSorter`: insertion sort (name / year / location), selection sort (events by year), bubble sort; `SearchService` ranks by insertion sort |
| Spring Boot + Thymeleaf | controllers prepare a `Model`; templates render it |

Sort the lobby entrances from the dropdown (`/?sort=name|year|location`). Search at `/search?q=1983`.

## Photographs — approval and local files needed

**No photographs are bundled yet**, because the project archive did not include source image files and reuse permission could not be verified. The galleries are wired to local files and show an honest pending state rather than fabricated imagery. Each campus has five frames. Add real,
credited images with these exact names and they appear automatically (restart or refresh):

```
static/images/campuses/<siniloan|santa-cruz|los-banos|san-pablo>/
  main-gate.jpg · administration-building.jpg · campus-grounds.jpg · archive-photo.jpg · flagship-facility.jpg
```

Use official LSPU photos with permission or your own, resize to about 1600 px wide, and record each credit
(`CampusImage.setCredit(...)` in `CampusDataLoader`). See [IMAGE_SOURCES.md](IMAGE_SOURCES.md) for official starting points and attribution guidance. Do not use UP Los Baños photos for the Los Baños campus — it is a different school.

## Verified vs. still needs research

Verified on the official LSPU "Historical Development" page: 1952 Baybay Provincial High School; 1957 RA 1807 (BNAVS);
1971 RA 6321 (BNCAT); June 10, 1983 BP 482 (LSPC); 1986 first college president; 1993 PTIA; October 1999 integration of the
Laguna College of Arts and Trades and Los Baños College of Fisheries; December 11, 2000 turn-over of San Pablo City NSAT.
Seal symbolism, 2018–19 graduate counts and board-exam results come from lspu.edu.ph pages.

Flagged in the app with “Needs verification” (secondary sources only):

- Founding year **1957** and operations year (1960 / 1959 / 1960) for Santa Cruz, Los Baños and San Pablo (Wikipedia).
- **2007** RA 9402 university conversion and Santa Cruz becoming Main Campus.
- 1983 college status of Los Baños (school directory); 2016 CHED Center of Development memo; year Dr. Briones became president.
- Land areas, current program lists per campus, barangay naming for Los Baños (Malinta vs. Mayondon–Malinta).
- Map coordinates are approximate town centres; replace with exact campus coordinates in `CampusDataLoader`.

## Demo script (3 minutes)

1. Open `/` → cinematic entrance → *Enter the Museum* (or *Skip*).
2. Hover the exhibition entrances, then change the sort dropdown (Java insertion sort).
3. Enter Siniloan → step through the timeline → open the gallery lightbox (arrow keys, Esc).
4. Use *Next room* to walk to Santa Cruz (door transition), then *Return to the Lobby*.
5. Open *Campus Navigator*, click campuses, show the Java-computed distances.
6. Search “1983”, then try `<script>` to show regex validation.


## About Us — developer portraits

The `/about` page includes a responsive six-member developer section. Add the team's approved portrait photos to
`src/main/resources/static/images/developers/` with these exact filenames:

- `developer-1.jpg`
- `developer-2.jpg`
- `developer-3.jpg`
- `developer-4.jpg`
- `developer-5.jpg`
- `developer-6.jpg`

Then replace “Team Member 1” through “Team Member 6” in
`src/main/resources/templates/about.html` with the developers' names. Portraits use a 4:5 crop; a neutral
background remains visible until each photo is added.
