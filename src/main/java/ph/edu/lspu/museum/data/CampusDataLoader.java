package ph.edu.lspu.museum.data;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ph.edu.lspu.museum.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds all campus records in memory.
 *
 * SOURCES (researched Nov 2026):
 *  [OFFICIAL]  https://lspu.edu.ph/page/historical-development   (official "Historical Development")
 *  [SEAL]      https://lspu.edu.ph/page/university-seal         (official seal symbolism)
 *  [NEWS]      lspu.edu.ph news / procurement pages              (official site)
 *  [WIKI]      Wikipedia "Laguna State Polytechnic University"   (secondary source)
 *  [LIB]       LSPU library history page (sites.google.com/site/lspusclibrary)
 *
 * Anything that comes only from a secondary source, or that sources disagree on,
 * is flagged needsVerification = true and listed in the campus "needs research" notes.
 * No history was invented; no photograph is bundled (see CampusImage).
 */
@Component
public class CampusDataLoader {

    private static final String OFFICIAL = "LSPU official site — Historical Development (lspu.edu.ph)";
    private static final String WIKI = "Wikipedia, 'Laguna State Polytechnic University' (secondary source)";

    public List<Campus> loadCampuses() {
        List<Campus> list = new ArrayList<>();
        list.add(siniloan());
        list.add(santaCruz());
        list.add(losBanos());
        list.add(sanPablo());
        for (Campus c : list) markAvailableImages(c);
        return list;
    }

    // ------------------------------------------------------------------ SINILOAN
    private Campus siniloan() {
        Campus c = new Campus("siniloan", "LSPU Siniloan", "Siniloan",
                "Brgy. Wawa, Siniloan, Laguna (Host Campus)", 1952,
                "1952: Baybay Provincial High School, the root of the whole university [OFFICIAL].",
                14.4167, 121.4500, "#4F7A3A",
                "Agriculture", "The Host Campus — where the university began",
                "Beside Laguna de Bay in the shoreline (baybay) district, the Siniloan campus is where LSPU "
              + "traces its roots. It began in 1952 as a provincial high school and grew, step by step, into "
              + "the university's Host Campus.");
        c.getHistory()
         .addBackground("The university began in 1952 as Baybay Provincial High School — the first public high school "
                      + "in the shoreline (baybay) district of Laguna de Bay and the second in the whole province of Laguna.")
         .addBackground("Through the decades the school was converted several times by law: into a national agricultural and "
                      + "vocational school (1957), a college of agriculture and technology (1971), and a state college (1983). "
                      + "Siniloan remained the only campus of the Laguna State Polytechnic College until 1999.")
         .addBackground("Agriculture is the campus's banner program; in the official university seal, the green colour and "
                      + "rice stalk stand for Siniloan.")
         .addFormerName("Baybay Provincial High School (1952)")
         .addFormerName("Baybay National Agricultural and Vocational School — BNAVS (1957)")
         .addFormerName("Baybay National College of Agriculture and Technology — BNCAT (1971)")
         .addFormerName("Laguna State Polytechnic College (1983)")
         .addResearchNote("Exact land area: sources disagree (a 13.37 ha campus plus laboratory/research areas in one source; "
                        + "other sources give different totals). Confirm with the campus.")
         .addResearchNote("Date the Host Campus label was formally adopted — not found in the sources reviewed.")
         .addResearchNote("Photographs: none bundled. Request official photos from the LSPU Siniloan campus or University Library.");

        e(c, 1952, "1952", "Baybay Provincial High School opens",
          "Established as the first public high school in the shoreline (baybay) district of Laguna de Bay and the second in the province of Laguna.", OFFICIAL, false);
        e(c, 1957, "June 1957", "Becomes BNAVS",
          "By Republic Act No. 1807 the high school was converted into the Baybay National Agricultural and Vocational School.", OFFICIAL, false);
        e(c, 1968, "School year 1968–1969", "Manpower Training Center starts",
          "The Manpower Training Center began operating. BNAVS also served as a PACD-BVE Training Center in rice production for American Peace Corps Volunteers, producing 682 graduates.", OFFICIAL, false);
        e(c, 1969, "December 1969", "Pilot school for the Associate in Agriculture",
          "BNAVS became the pilot school for the 2-year Agricultural Technician Curriculum, Associate in Agriculture, starting with 8 enrollees.", OFFICIAL, false);
        e(c, 1971, "June 1971", "Becomes BNCAT",
          "Republic Act No. 6321 (from House Bill 269) converted the school into the Baybay National College of Agriculture and Technology.", OFFICIAL, false);
        e(c, 1975, "2nd Semester 1975–1976", "First degree course: BS Agriculture",
          "BNCAT began offering the Bachelor of Science in Agriculture; the Bachelor of Science in Agricultural Education followed in the first semester of 1977–1978.", OFFICIAL, false);
        e(c, 1980, "1980–1983", "USAID Agricultural Education Outreach Project",
          "The institution was a grantee of the Agricultural Education Outreach Project (AEOP), a research project of the United States Agency for International Development.", OFFICIAL, false);
        e(c, 1983, "June 10, 1983", "Laguna State Polytechnic College is born",
          "Batas Pambansa No. 482 converted BNCAT into a state college: the Laguna State Polytechnic College (LSPC).", OFFICIAL, false);
        e(c, 1986, "1986", "Dr. Ricardo A. Wagan, first College President",
          "Dr. Wagan became the first College President; many projects and infrastructure developments followed.", OFFICIAL, false);
        e(c, 1993, "1993", "Provincial Technical Institute of Agriculture",
          "DECS designated the institution one of seven Provincial Technical Institutes of Agriculture in Region IV under the Aus-AID-AGRITECH Project.", OFFICIAL, false);
        e(c, 1999, "1999", "From one campus to a multi-campus college",
          "Siniloan had been the sole campus of LSPC until the Laguna College of Arts and Trades and the Los Baños College of Fisheries were integrated in 1999.", "LSPU Library history (sites.google.com/site/lspusclibrary) and " + OFFICIAL, false);
        e(c, 2018, "December 2018", "ISO 9001:2015 certification",
          "The university received its ISO 9001:2015 certificate (a university-wide milestone shared by all four campuses).", "LSPU official site (lspu.edu.ph)", false);

        c.getContributions().add("Birthplace of the university: the 1952 high school is the root of every LSPU campus.");
        c.getContributions().add("Pilot school for the 2-year Associate in Agriculture (from December 1969).");
        c.getContributions().add("Served as a rice-production training center for American Peace Corps Volunteers (682 graduates).");
        c.getContributions().add("Designated a Provincial Technical Institute of Agriculture in Region IV (1993).");
        c.getContributions().add("Hosts the university's two-storey Research Center, open to faculty, students, personnel and permitted visitors.");

        c.getPrograms().add("College of Agriculture — includes a Doctor of Philosophy in Agriculture [WIKI]");
        c.getPrograms().add("College of Arts and Sciences (science laboratory equipment procurement listed on lspu.edu.ph)");
        c.getPrograms().add("Agriculture and Computer Studies are named as the campus's concentrations [WIKI]");
        c.getPrograms().add("Full current program list: needs research — confirm on lspu.edu.ph/academics");

        photos(c, "siniloan");
        return c;
    }

    // ------------------------------------------------------------------ SANTA CRUZ
    private Campus santaCruz() {
        Campus c = new Campus("santa-cruz", "LSPU Santa Cruz", "Santa Cruz",
                "Brgy. Bubukal, Santa Cruz, Laguna (Main Campus)", 1957,
                "1957 per Wikipedia (operations began 1960). Not stated on the official history page — verify.",
                14.2800, 121.4160, "#8B2E2E",
                "Engineering", "The Main Campus in the provincial capital",
                "Located in Santa Cruz, the capital town of Laguna, this campus grew from the Laguna College of Arts and "
              + "Trades and became the university's Main Campus.");
        c.getHistory()
         .addBackground("The campus began as the Laguna College of Arts and Trades (LCAT), a CHED-supervised institution. "
                      + "It was integrated into the Laguna State Polytechnic College in October 1999 and became LSPU–Sta. Cruz Campus.")
         .addBackground("When the college became a university, the Santa Cruz campus in Barangay Bubukal became the Main Campus.")
         .addBackground("Engineering and industrial technology are its banner programs; in the official seal, the maroon colour and gear "
                      + "represent Santa Cruz and San Pablo City together.")
         .addFormerName("Laguna College of Arts and Trades — LCAT")
         .addFormerName("Laguna School of Arts and Trade (name used in a university presentation)")
         .addResearchNote("Founding year 1957 and start of operations in 1960 come from Wikipedia only. Find the original law or order creating the school.")
         .addResearchNote("Exact date of the Main Campus designation (the 2007 conversion to a university is the likely context). Confirm.")
         .addResearchNote("Photographs: none bundled. Request official photos from the campus.");

        e(c, 1957, "1957", "Campus founded (per secondary source)",
          "Wikipedia records the Santa Cruz campus as founded in 1957, with initial operations three years later.", WIKI, true);
        e(c, 1960, "1960", "Initial operations begin (per secondary source)",
          "Classes/operations began about three years after founding, according to Wikipedia.", WIKI, true);
        e(c, 1999, "October 1999", "Integrated into the college",
          "As a CHED-supervised institution, the Laguna College of Arts and Trades was integrated into LSPC under CHED Memorandum No. 18, s. 1999 (formal integration on October 5 and 25, 1999 for the two schools).", OFFICIAL, false);
        e(c, 2007, "2007", "Becomes the Main Campus of a university",
          "Republic Act No. 9402 converted the Laguna State Polytechnic College into a university; LSPU–Sta. Cruz in Barangay Bubukal became the Main Campus.", WIKI + " and Scribd 'LSPU Background' — official source still to be confirmed", true);
        e(c, 2018, "School year 2018–2019", "Largest graduating class among the campuses",
          "Of 7,949 university graduates certified for SY 2018–2019, Santa Cruz had the most: 2,878.", "LSPU official news (lspu.edu.ph)", false);
        e(c, 2023, "November 2023", "96.51% passing rate in the Nursing Licensure Exam",
          "83 of 86 LSPU–SCC examinees passed the November 2023 Nursing Licensure Examination.", "LSPU official news (lspu.edu.ph)", false);

        c.getContributions().add("Main Campus of the university and home of several university-level offices.");
        c.getContributions().add("Engineering and industrial technology training — the campus's banner programs.");
        c.getContributions().add("Graduate-level partnership: a group of Vietnamese students completed a Master of Arts in Teaching (English) here.");
        c.getContributions().add("November 2023: 83 of 86 examinees passed the Nursing Licensure Examination (96.51%).");

        c.getPrograms().add("Engineering");
        c.getPrograms().add("Nursing and Allied Health");
        c.getPrograms().add("Hospitality Management and Tourism");
        c.getPrograms().add("Arts and Sciences");
        c.getPrograms().add("Business Management and Accountancy");
        c.getPrograms().add("Criminal Justice Education");
        c.getPrograms().add("Law  (all listed per Wikipedia — confirm current offerings)");

        photos(c, "santa-cruz");
        return c;
    }

    // ------------------------------------------------------------------ LOS BAÑOS
    private Campus losBanos() {
        Campus c = new Campus("los-banos", "LSPU Los Baños", "Los Baños",
                "Brgy. Malinta (Mayondon–Malinta), Los Baños, Laguna", 1957,
                "1957 per Wikipedia (operations began 1959). Not stated on the official history page — verify.",
                14.1700, 121.2400, "#2C7A8C",
                "Fisheries", "The fisheries campus on Laguna de Bay",
                "The Los Baños campus grew from the Los Baños College of Fisheries and keeps fisheries as its banner program. "
              + "It is a different institution from UP Los Baños, which shares the town.");
        c.getHistory()
         .addBackground("The campus began as the Los Baños College of Fisheries (LBCF), a CHED-supervised institution. "
                      + "It was integrated into the Laguna State Polytechnic College in October 1999.")
         .addBackground("In the official university seal, the aqua blue colour and fish stand for fisheries, the banner program of Los Baños.")
         .addBackground("A school directory notes the school was formerly the Los Baños School of Fisheries and became a College in 1983 (secondary source).")
         .addFormerName("Los Baños College of Fisheries — LBCF")
         .addFormerName("Los Baños School of Fisheries — LBSF (per a school directory; verify)")
         .addResearchNote("Barangay name: the official page says Barangays Malinta; Wikipedia says Mayondon–Malinta. Confirm the correct form.")
         .addResearchNote("Founding 1957 / operations 1959 and the 1983 college status come from secondary sources. Verify with the campus or the original law.")
         .addResearchNote("Photographs: none bundled. Do NOT use UPLB photos — different institution.");

        e(c, 1957, "1957", "Campus founded (per secondary source)",
          "Wikipedia records the Los Baños campus as founded in 1957, starting operations in 1959.", WIKI, true);
        e(c, 1959, "1959", "Operations begin (per secondary source)",
          "The campus started operations in 1959, per Wikipedia.", WIKI, true);
        e(c, 1983, "1983", "Los Baños College of Fisheries (per directory)",
          "A school directory states the Los Baños School of Fisheries became the Los Baños College of Fisheries in 1983.", "edukasyon.ph school profile (secondary source)", true);
        e(c, 1999, "October 1999", "Integrated into the college",
          "The Los Baños College of Fisheries was integrated into LSPC under CHED Memorandum No. 18, s. 1999 and became LSPU–Los Baños Campus.", OFFICIAL, false);
        e(c, 2019, "School year 2018–2019", "1,222 graduates",
          "Los Baños contributed 1,222 of the university's 7,949 graduates for SY 2018–2019.", "LSPU official news (lspu.edu.ph)", false);

        c.getContributions().add("Banner campus for Fisheries in the university system.");
        c.getContributions().add("Food, Nutrition and Dietetics is a concentration of the campus [WIKI].");
        c.getContributions().add("Its library collection focuses on fisheries, as recorded in the LSPU library history.");

        c.getPrograms().add("Fisheries");
        c.getPrograms().add("Food Nutrition and Dietetics");
        c.getPrograms().add("College of Arts and Sciences — BS Psychology [WIKI]");
        c.getPrograms().add("College of Business Management and Accountancy — BS Accountancy [WIKI]");
        c.getPrograms().add("Graduate programs in Education and Fisheries [directory — verify]");

        photos(c, "los-banos");
        return c;
    }

    // ------------------------------------------------------------------ SAN PABLO
    private Campus sanPablo() {
        Campus c = new Campus("san-pablo", "LSPU San Pablo", "San Pablo City",
                "Brgy. Del Remedio, San Pablo City, Laguna", 1957,
                "1957 per Wikipedia (operations began 1960). Not stated on the official history page — verify.",
                14.0690, 121.3250, "#8B2E2E",
                "Teacher Education & Technology", "A Center of Development in Teacher Education",
                "The San Pablo City campus grew from the San Pablo City National School of Arts and Trades and is recognized "
              + "by CHED as a Center of Development in Teacher Education.");
        c.getHistory()
         .addBackground("The campus began as the San Pablo City National School of Arts and Trades (SPCNSAT). The formal turn-over "
                      + "to the Laguna State Polytechnic College took place on December 11, 2000.")
         .addBackground("Its College of Teacher Education is recognized by CHED as a Center of Development in Teacher Education "
                      + "(CHED Memorandum Order 17, series of 2016); Industrial Technology is another concentration.")
         .addBackground("Dr. Mario R. Briones, a former Campus Director of San Pablo City, was elected the university's third president.")
         .addFormerName("San Pablo City National School of Arts and Trades — SPCNSAT")
         .addResearchNote("Founding 1957 / operations 1960 come from Wikipedia only. Find the original law that created the school.")
         .addResearchNote("Wikipedia gives the campus as 6 hectares; confirm the current land area.")
         .addResearchNote("Photographs: none bundled. Request official photos from the campus.");

        e(c, 1957, "1957", "Campus founded (per secondary source)",
          "Wikipedia records the San Pablo City campus (Barangay Del Remedio) as founded in 1957, beginning operations in 1960.", WIKI, true);
        e(c, 1960, "1960", "Operations begin (per secondary source)",
          "The campus began operations in 1960, per Wikipedia.", WIKI, true);
        e(c, 2000, "December 11, 2000", "Formal turn-over to LSPC",
          "The San Pablo City National School of Arts and Trades was formally turned over and became LSPU–San Pablo City Campus.", OFFICIAL, false);
        e(c, 2016, "2016", "Center of Development in Teacher Education",
          "CHED Memorandum Order 17, series of 2016, recognized the College of Teacher Education as a Center of Development.", WIKI + " (cites CMO 17, s. 2016)", true);
        e(c, 2019, "2019", "Alumnus becomes the third University President",
          "Dr. Mario R. Briones, previously Campus Director of San Pablo City, was elected by the Board of Regents and took his oath the same day. (Year per Wikipedia.)", WIKI, true);
        e(c, 2024, "February 2024", "9th top-performing school in the Criminologist Licensure Exam",
          "LSPU-SPCC placed 9th among top-performing schools in the February 2024 Licensure Examination for Criminologists.", "LSPU official news (lspu.edu.ph)", false);

        c.getContributions().add("CHED Center of Development in Teacher Education.");
        c.getContributions().add("Industrial Technology and teacher-education training for Laguna.");
        c.getContributions().add("Criminology: ranked 9th in the February 2024 licensure exam; topnotchers recognized in December 2022.");
        c.getContributions().add("Hosts the university's Audio-Visual Education Center and Conference Hall used for university events.");

        c.getPrograms().add("College of Teacher Education (Center of Development)");
        c.getPrograms().add("Industrial Technology");
        c.getPrograms().add("College of Arts and Sciences — BS Biology, BS Psychology [WIKI]");
        c.getPrograms().add("Hotel and Restaurant Management / Tourism Management (laboratory procurement on lspu.edu.ph)");
        c.getPrograms().add("Criminal Justice Education; Electronics/Engineering programs (board-exam results on lspu.edu.ph)");

        photos(c, "san-pablo");
        return c;
    }

    // ------------------------------------------------------------------ helpers
    private void e(Campus c, int year, String label, String title, String desc, String source, boolean verify) {
        c.getEvents().add(new HistoricalEvent(c.getSlug(), year, label, title, desc, source, verify));
    }

    /** Five photo "slots". The real files are added by the team (see README). */
    private void photos(Campus c, String slug) {
        c.getImages().add(new CampusImage(slug, "main-gate.jpg", "Campus Main Gate", "Landmark",
                "The entrance to the campus. Description to be written from the photograph and its source."));
        c.getImages().add(new CampusImage(slug, "administration-building.jpg", "Administration Building", "Current",
                "A current photograph of the campus administration building."));
        c.getImages().add(new CampusImage(slug, "campus-grounds.jpg", "Campus Grounds", "Current",
                "A view of the campus grounds."));
        c.getImages().add(new CampusImage(slug, "archive-photo.jpg", "From the Archive", "Historical",
                "A historical photograph from the university archive or library, with date and credit."));
        c.getImages().add(new CampusImage(slug, "flagship-facility.jpg", "Flagship Facility", "Institutional",
                "A facility connected to the campus's flagship program (" + c.getFlagship() + ")."));
    }

    /** Checks whether each photo file really exists in src/main/resources/static/images/campuses/. */
    private void markAvailableImages(Campus c) {
        for (CampusImage image : c.getImages()) {
            image.setAvailable(new ClassPathResource("static" + image.getUrl()).exists());
        }
    }
}
