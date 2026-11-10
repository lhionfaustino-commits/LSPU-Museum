# LSPU Heritage Museum — photograph sourcing notes

The application intentionally does not fabricate historical photographs. The campus gallery checks for local files and displays a clear “Photograph pending” label when a file has not yet been supplied.

## Official starting points

1. **LSPU official website / campus information** — https://lspu.edu.ph/campus/
2. **LSPU official historical development** — https://lspu.edu.ph/page/historical-development
3. **LSPU official news and photo news** — https://lspu.edu.ph/index.php
4. **Official LSPU Siniloan campus entrance evidence (PDF)** — https://lspu.edu.ph/green/assets/evidences/2024/si/1.3/1C_3_lspu.edu.ph_Number_of_campus_site.pdf
5. **Official LSPU campus-building photo evidence (PDF)** — https://lspu.edu.ph/green/assets/evidences/2024/si/1.6/1G_7_lspu.edu.ph_Total_campus_buildings_area_%28m2%29.pdf

These sources identify relevant official material, but do not by themselves grant permission to republish every photograph. Confirm reuse rights and record the photographer/source before placing any image in the public website. For historical photos, request scans from the LSPU University Library or the relevant campus office and retain their date/context where known.

## Local image folders

Place approved image files in the matching folder. The Java data loader detects the expected names automatically:

```text
src/main/resources/static/images/campuses/
  siniloan/    main-gate.jpg  administration-building.jpg  campus-grounds.jpg  archive-photo.jpg  flagship-facility.jpg
  santa-cruz/  main-gate.jpg  administration-building.jpg  campus-grounds.jpg  archive-photo.jpg  flagship-facility.jpg
  los-banos/   main-gate.jpg  administration-building.jpg  campus-grounds.jpg  archive-photo.jpg  flagship-facility.jpg
  san-pablo/   main-gate.jpg  administration-building.jpg  campus-grounds.jpg  archive-photo.jpg  flagship-facility.jpg
```

Use descriptive captions and credits in `CampusDataLoader.java` (`CampusImage` title, description, kind, and credit). Do not label a current campus photograph as a historical photograph. Do not use UP Los Baños photographs to represent LSPU Los Baños.
