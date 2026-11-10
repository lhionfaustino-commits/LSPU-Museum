/* ==========================================================
   LSPU HERITAGE MUSEUM — front-end motion and interaction
   All DATA (campuses, sorting, search, distances) comes from the Java backend.
   This file only handles animation and user interface behaviour.
   Uses GSAP + ScrollTrigger; works (without animation) if they fail to load.
   ========================================================== */
(function () {
  'use strict';

  var root = document.documentElement;
  var reduce = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  var hasGsap = typeof window.gsap !== 'undefined';
  // Base path of the app ("/" normally, "/museum/" if deployed under a context path) so /api and /images URLs never break.
  var metaCtx = document.querySelector('meta[name="ctx"]');
  var CTX = ((metaCtx && metaCtx.content) || '/').replace(/\/$/, '');
  var page = document.body.getAttribute('data-page');

  // If GSAP could not load (offline, blocked CDN) show everything normally.
  if (!hasGsap) { root.classList.remove('js'); }
  var animate = hasGsap && !reduce;
  if (hasGsap && window.ScrollTrigger) { gsap.registerPlugin(ScrollTrigger); }

  var $ = function (sel, ctx) { return (ctx || document).querySelector(sel); };
  var $$ = function (sel, ctx) { return Array.prototype.slice.call((ctx || document).querySelectorAll(sel)); };

  /* ------------------------------------------------------------------
     1. Doors: the page-transition between rooms
     ------------------------------------------------------------------ */
  var doors = $('#doors');
  var panels = doors ? $$('.doors__panel', doors) : [];
  var doorsLabel = $('#doorsLabel');

  function closeDoors(label, done) {
    if (!animate || !doors) { done(); return; }
    doors.classList.add('is-closed');
    if (doorsLabel) doorsLabel.textContent = label || '';
    gsap.timeline({ onComplete: done })
      .to(panels, { scaleX: 1, duration: 0.55, ease: 'power3.inOut' })
      .to(doorsLabel, { opacity: 1, duration: 0.25 }, '-=0.15');
  }

  function openDoorsOnArrival() {
    var label = null;
    try { label = sessionStorage.getItem('lspuArrive'); sessionStorage.removeItem('lspuArrive'); } catch (e) {}
    if (!animate || !doors || label === null) { return Promise.resolve(); }
    return new Promise(function (resolve) {
      doors.classList.add('is-closed');
      doorsLabel.textContent = label;
      gsap.set(panels, { scaleX: 1 });
      gsap.set(doorsLabel, { opacity: 1 });
      gsap.timeline({ onComplete: function () { doors.classList.remove('is-closed'); resolve(); } })
        .to(doorsLabel, { opacity: 0, duration: 0.3, delay: 0.25 })
        .to(panels, { scaleX: 0, duration: 0.7, ease: 'power3.inOut' }, '-=0.05');
    });
  }

  document.addEventListener('click', function (e) {
    var a = e.target.closest ? e.target.closest('a[data-room]') : null;
    if (!a || e.defaultPrevented || e.metaKey || e.ctrlKey || e.shiftKey || e.button !== 0) return;
    var url = new URL(a.href, location.href);
    if (url.origin !== location.origin) return;
    // Links to a section on the SAME page just scroll.
    if (url.pathname === location.pathname && url.hash) return;
    if (url.pathname === location.pathname && url.search === location.search) return;
    e.preventDefault();
    var label = a.getAttribute('data-label') || a.textContent.trim();
    try { sessionStorage.setItem('lspuArrive', label); } catch (err) {}
    closeDoors(label, function () { location.href = a.href; });
  });

  // If the browser restores the page from cache (Back button), reset the doors.
  window.addEventListener('pageshow', function (ev) {
    if (ev.persisted && doors && hasGsap) {
      gsap.set(panels, { scaleX: 0 }); gsap.set(doorsLabel, { opacity: 0 }); doors.classList.remove('is-closed');
    }
  });

  /* ------------------------------------------------------------------
     2. Navigation: solid bar on scroll, mobile menu, active indicator
     ------------------------------------------------------------------ */
  var nav = $('#nav');
  var toggle = $('#navToggle');
  var menu = $('#navMenu');
  var indicator = $('#navIndicator');

  function onScrollNav() { if (nav) nav.classList.toggle('is-solid', window.scrollY > 40); }
  window.addEventListener('scroll', onScrollNav, { passive: true }); onScrollNav();

  if (toggle && menu) {
    toggle.addEventListener('click', function () {
      var open = toggle.getAttribute('aria-expanded') !== 'true';
      toggle.setAttribute('aria-expanded', String(open));
      menu.classList.toggle('is-open', open);
    });
    document.addEventListener('keydown', function (e) {
      if (e.key === 'Escape' && menu.classList.contains('is-open')) { toggle.click(); toggle.focus(); }
    });
  }

  function moveIndicator(el) {
    if (!indicator || !el || !nav) return;
    var navBox = nav.getBoundingClientRect(), box = el.getBoundingClientRect();
    indicator.style.left = (box.left - navBox.left) + 'px';
    indicator.style.width = box.width + 'px';
  }
  var activeLink = $('.nav__menu a.is-active');
  if (activeLink) {
    moveIndicator(activeLink);
    $$('.nav__menu a').forEach(function (a) {
      a.addEventListener('mouseenter', function () { moveIndicator(a); });
      a.addEventListener('focus', function () { moveIndicator(a); });
    });
    $('.nav__menu').addEventListener('mouseleave', function () { moveIndicator(activeLink); });
    window.addEventListener('resize', function () { moveIndicator(activeLink); });
  }

  /* ------------------------------------------------------------------
     3. Text splitting for staggered title reveals
     ------------------------------------------------------------------ */
  function splitLines(el) {
    var lines = el.innerHTML.split(/<br\s*\/?>/i);
    el.innerHTML = lines.map(function (l) {
      return '<span class="split-line"><span>' + l + '</span></span>';
    }).join('');
    return $$('.split-line > span', el);
  }

  /* ------------------------------------------------------------------
     4. Page intro animations + scroll effects
     ------------------------------------------------------------------ */
  function playPageIntro() {
    if (!animate) return;
    var splits = $$('[data-split]');
    var inner = [];
    splits.forEach(function (el) { inner = inner.concat(splitLines(el)); });
    if (inner.length) {
      gsap.set(inner, { yPercent: 110 });
      gsap.to(inner, { yPercent: 0, duration: 1.1, ease: 'power4.out', stagger: 0.12, delay: 0.1 });
    }
    if (window.ScrollTrigger) {
      // Reveal elements as the visitor scrolls
      ScrollTrigger.batch('.reveal', {
        start: 'top 88%', once: true,
        onEnter: function (batch) {
          gsap.to(batch, { opacity: 1, y: 0, duration: 0.9, ease: 'power3.out', stagger: 0.12, overwrite: true });
        }
      });
      // Parallax for large backgrounds
      $$('[data-parallax], .room-hero__bg').forEach(function (el) {
        gsap.to(el, { yPercent: 14, ease: 'none',
          scrollTrigger: { trigger: el.parentElement, start: 'top top', end: 'bottom top', scrub: true } });
      });
      // Animated progress line on the long timeline
      var vtl = $('.vtl');
      if (vtl) {
        gsap.fromTo(vtl, { '--tl-progress': 0 }, { '--tl-progress': 1, ease: 'none',
          scrollTrigger: { trigger: vtl, start: 'top 70%', end: 'bottom 70%', scrub: true } });
      }
    }
  }

  /* ------------------------------------------------------------------
     5. Cinematic entrance (lobby page only)
     ------------------------------------------------------------------ */
  var entrance = $('#entrance');

  function finishEntrance(instant) {
    try { sessionStorage.setItem('lspuEntered', '1'); } catch (e) {}
    document.body.style.overflow = '';
    function done() { entrance.classList.add('is-gone'); playPageIntro(); }
    if (instant || !animate) { done(); return; }
    gsap.to(entrance, { opacity: 0, duration: 0.8, ease: 'power2.inOut', onComplete: done });
  }

  function runEntrance() {
    document.body.style.overflow = 'hidden';
    var enterBtn = $('#enterBtn'), skipBtn = $('#skipBtn');
    skipBtn.addEventListener('click', function () { tl && tl.kill(); finishEntrance(true); });
    var tl = null;
    var titleParts = $$('.entrance__title span');
    var items = [$('.entrance__eyebrow'), $('.entrance__sub'), enterBtn, skipBtn];

    if (animate) {
      gsap.set(titleParts, { yPercent: 60, opacity: 0 });
      gsap.set(items, { opacity: 0, y: 14 });
      gsap.set('.entrance__glow', { opacity: 0 });
      // Slow reveal: light grows, title rises, button appears (~3 s in total)
      tl = gsap.timeline({ delay: 0.3 })
        .to('.entrance__light', { opacity: 1, duration: 1.4 }, 0)
        .to('.entrance__glow', { opacity: 1, duration: 1.6 }, 0.2)
        .to(items[0], { opacity: 1, y: 0, duration: 0.8 }, 0.6)
        .to(titleParts, { yPercent: 0, opacity: 1, duration: 1, stagger: 0.18, ease: 'power3.out' }, 0.8)
        .to([items[1], enterBtn, skipBtn], { opacity: 1, y: 0, duration: 0.8, stagger: 0.15 }, 1.9);
    }

    enterBtn.addEventListener('click', function () {
      if (!animate) { finishEntrance(true); return; }
      tl && tl.kill();
      // The museum doors swing open, light floods the screen, the hall is revealed.
      gsap.timeline({ onComplete: function () { finishEntrance(false); } })
        .to('.entrance__content', { opacity: 0, scale: 0.96, duration: 0.5, ease: 'power2.in' })
        .to('.entrance__door--left', { xPercent: -100, duration: 1.3, ease: 'power3.inOut' }, 0.1)
        .to('.entrance__door--right', { xPercent: 100, duration: 1.3, ease: 'power3.inOut' }, 0.1)
        .to('.entrance__glow', { scale: 3, opacity: 1, duration: 1.3, ease: 'power2.in' }, 0.1)
        .to('.entrance__light', { background: 'radial-gradient(ellipse at 50% 60%, rgba(245,240,230,.95), rgba(198,161,91,.8))', duration: 0.9 }, 0.5);
    });
  }

  /* ------------------------------------------------------------------
     6. Interactive timeline widget (exhibition rooms)
     ------------------------------------------------------------------ */
  function setupTimeline() {
    var widget = $('#timelineWidget');
    if (!widget) return;
    var dots = $$('.tl__dot', widget), cards = $$('.tl__card', widget);
    var count = $('#tlCount'), progress = $('#tlProgress');
    var current = 0;

    function show(i) {
      if (i < 0 || i >= cards.length) return;
      current = i;
      dots.forEach(function (d, k) {
        d.classList.toggle('is-active', k === i);
        d.classList.toggle('is-past', k < i);
        d.setAttribute('aria-selected', String(k === i));
      });
      cards.forEach(function (c, k) { c.classList.toggle('is-active', k === i); });
      count.textContent = (i + 1) + ' / ' + cards.length;
      var d = dots[i];
      progress.style.width = (d.offsetLeft + d.offsetWidth / 2) + 'px';
      if (d.scrollIntoView) d.scrollIntoView({ block: 'nearest', inline: 'center', behavior: reduce ? 'auto' : 'smooth' });
    }
    dots.forEach(function (d, k) { d.addEventListener('click', function () { show(k); }); });
    $('#tlPrev').addEventListener('click', function () { show(current - 1); });
    $('#tlNext').addEventListener('click', function () { show(current + 1); });
    widget.addEventListener('keydown', function (e) {
      if (e.key === 'ArrowRight') { show(current + 1); }
      if (e.key === 'ArrowLeft') { show(current - 1); }
    });
    window.addEventListener('resize', function () { show(current); });
    show(0);
  }

  /* ------------------------------------------------------------------
     7. Cinematic photograph lightbox
     ------------------------------------------------------------------ */
  function setupLightbox() {
    var grid = $('#gallery-grid'), box = $('#lightbox');
    if (!grid || !box) return;
    var items = $$('.frame__btn', grid);
    var wrap = $('#lbImgWrap'), spin = $('#lbSpin'), fig = $('.lightbox__fig', box);
    var index = 0, lastFocus = null;

    function fill(i) {
      var b = items[i], d = b.dataset;
      $('#lbTitle').textContent = d.title;
      $('#lbKind').textContent = d.kind + ' · photograph ' + (i + 1) + ' of ' + items.length;
      $('#lbDesc').textContent = d.desc;
      $('#lbCredit').textContent = d.src ? 'Credit: ' + d.credit : 'No photograph has been added for this frame yet.';
      Array.prototype.slice.call(wrap.children).forEach(function (n) { if (n !== spin) wrap.removeChild(n); });
      if (d.src) {
        spin.hidden = false;           // loading indicator
        var img = new Image();
        img.alt = d.title;
        img.onload = function () { spin.hidden = true; wrap.appendChild(img); if (animate) gsap.from(img, { opacity: 0, scale: 1.04, duration: 0.7, ease: 'power2.out' }); };
        img.onerror = function () { spin.hidden = true; };
        img.src = d.src;
      } else {
        spin.hidden = true;
        var empty = document.createElement('div');
        empty.className = 'lightbox__empty';
        empty.textContent = 'Photograph pending';
        wrap.appendChild(empty);
      }
    }

    function open(i) {
      lastFocus = document.activeElement; index = i; fill(i);
      box.hidden = false; document.body.style.overflow = 'hidden';
      if (animate) {
        gsap.fromTo(box, { opacity: 0 }, { opacity: 1, duration: 0.45, ease: 'power2.out' });
        gsap.fromTo(fig, { y: 40, scale: 0.94, opacity: 0 }, { y: 0, scale: 1, opacity: 1, duration: 0.7, ease: 'power3.out', delay: 0.1 });
      } else { box.style.opacity = 1; }
      $('#lbClose').focus();
    }
    function close() {
      function end() { box.hidden = true; document.body.style.overflow = ''; if (lastFocus) lastFocus.focus(); }
      if (animate) { gsap.to(box, { opacity: 0, duration: 0.35, ease: 'power2.in', onComplete: end }); } else { end(); }
    }
    function go(step) {
      index = (index + step + items.length) % items.length;
      if (animate) {
        gsap.to(fig, { opacity: 0, x: -30 * step, duration: 0.25, onComplete: function () {
          fill(index); gsap.fromTo(fig, { opacity: 0, x: 30 * step }, { opacity: 1, x: 0, duration: 0.45, ease: 'power2.out' });
        } });
      } else { fill(index); }
    }
    items.forEach(function (b, i) { b.addEventListener('click', function () { open(i); }); });
    $('#lbClose').addEventListener('click', close);
    $('#lbPrev').addEventListener('click', function () { go(-1); });
    $('#lbNext').addEventListener('click', function () { go(1); });
    box.addEventListener('click', function (e) { if (e.target === box) close(); });
    document.addEventListener('keydown', function (e) {
      if (box.hidden) return;
      if (e.key === 'Escape') close();
      if (e.key === 'ArrowLeft') go(-1);
      if (e.key === 'ArrowRight') go(1);
      if (e.key === 'Tab') {                      // keep keyboard focus inside the viewer
        var f = $$('button', box);
        var first = f[0], last = f[f.length - 1];
        if (e.shiftKey && document.activeElement === first) { e.preventDefault(); last.focus(); }
        else if (!e.shiftKey && document.activeElement === last) { e.preventDefault(); first.focus(); }
      }
    });
  }

  /* ------------------------------------------------------------------
     8. Timeline page: campus filter
     ------------------------------------------------------------------ */
  function setupTimelineFilter() {
    var bar = $('#tlFilters');
    if (!bar) return;
    var items = $$('.vtl__item');
    bar.addEventListener('click', function (e) {
      var chip = e.target.closest('.chip');
      if (!chip) return;
      $$('.chip', bar).forEach(function (c) { c.classList.toggle('is-active', c === chip); });
      var f = chip.dataset.filter;
      items.forEach(function (it) {
        it.hidden = !(f === 'all' || it.dataset.campus === f);
        it.style.opacity = 1; it.style.transform = 'none';
        if (!it.hidden && animate) gsap.from(it, { opacity: 0, y: 20, duration: 0.5 });
      });
      if (window.ScrollTrigger) ScrollTrigger.refresh();
    });
  }

  /* ------------------------------------------------------------------
     9. Campus Navigator (data from Java: /api/navigator)
     ------------------------------------------------------------------ */
  function setupNavigator() {
    var svg = $('#mapSvg');
    if (!svg) return;
    var NS = 'http://www.w3.org/2000/svg';
    var W = 700, H = 520, PAD = 90;

    fetch(CTX + '/api/navigator').then(function (r) { return r.json(); }).then(function (data) {
      var nodes = data.nodes, links = data.links, byslug = {};
      var minLat = Math.min.apply(null, nodes.map(function (n) { return n.lat; }));
      var maxLat = Math.max.apply(null, nodes.map(function (n) { return n.lat; }));
      var minLon = Math.min.apply(null, nodes.map(function (n) { return n.lon; }));
      var maxLon = Math.max.apply(null, nodes.map(function (n) { return n.lon; }));
      var kx = Math.cos(14.2 * Math.PI / 180);                 // longitude degrees are shorter than latitude degrees
      var scale = Math.min((W - 2 * PAD) / ((maxLon - minLon) * kx), (H - 2 * PAD) / (maxLat - minLat));
      var offX = (W - (maxLon - minLon) * kx * scale) / 2, offY = (H - (maxLat - minLat) * scale) / 2;
      nodes.forEach(function (n) {
        n.x = offX + (n.lon - minLon) * kx * scale;
        n.y = offY + (maxLat - n.lat) * scale;                  // north is up
        byslug[n.slug] = n;
      });

      function el(name, attrs, parent) {
        var e = document.createElementNS(NS, name);
        Object.keys(attrs).forEach(function (k) { e.setAttribute(k, attrs[k]); });
        if (parent) parent.appendChild(e);
        return e;
      }
      var gLinks = $('#mapLinks'), gNodes = $('#mapNodes'), lineEls = [];

      links.forEach(function (l) {
        var a = byslug[l.from], b = byslug[l.to];
        var line = el('line', { x1: a.x, y1: a.y, x2: b.x, y2: b.y, 'class': 'link-line' }, gLinks);
        var t = el('text', { x: (a.x + b.x) / 2, y: (a.y + b.y) / 2 - 5, 'class': 'link-label' }, gLinks);
        t.textContent = l.km + ' km';
        lineEls.push({ line: line, l: l });
      });

      var nodeEls = {};
      nodes.forEach(function (n) {
        var g = el('g', { 'class': 'node', tabindex: 0, role: 'button', 'aria-label': n.name + ', ' + n.location }, gNodes);
        el('circle', { 'class': 'ring', cx: n.x, cy: n.y, r: 18 }, g);
        el('circle', { 'class': 'dot', cx: n.x, cy: n.y, r: 9, fill: n.accent }, g);
        var label = el('text', { x: n.x, y: n.y + 38 }, g);
        label.textContent = n.name;
        nodeEls[n.slug] = g;
        g.addEventListener('click', function () { select(n); });
        g.addEventListener('keydown', function (e) { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); select(n); } });
        if (animate) gsap.from(g, { opacity: 0, scale: 0.4, transformOrigin: n.x + 'px ' + n.y + 'px', duration: 0.8, delay: 0.2 + nodes.indexOf(n) * 0.15, ease: 'back.out(1.4)' });
      });
      if (animate) gsap.from('.link-line', { opacity: 0, duration: 1, delay: 0.9 });

      function select(n) {
        Object.keys(nodeEls).forEach(function (s) { nodeEls[s].classList.toggle('is-selected', s === n.slug); });
        lineEls.forEach(function (o) { o.line.classList.toggle('is-hot', o.l.from === n.slug || o.l.to === n.slug); });
        $('#infoKind').textContent = 'Est. ' + n.year + ' · ' + n.flagship;
        $('#infoName').textContent = n.name;
        $('#infoMeta').textContent = n.location + '. ' + n.tagline + '.';
        var photo = $('#infoPhoto');
        photo.innerHTML = '';
        photo.classList.toggle('has', !!n.cover);
        if (n.cover) { var im = new Image(); im.src = CTX + n.cover; im.alt = n.name; photo.appendChild(im); }
        var ul = $('#infoLinks'); ul.innerHTML = '';
        links.forEach(function (l) {
          if (l.from !== n.slug && l.to !== n.slug) return;
          var other = byslug[l.from === n.slug ? l.to : l.from];
          var li = document.createElement('li');
          var s1 = document.createElement('span'); s1.textContent = 'to ' + other.name;
          var s2 = document.createElement('b'); s2.textContent = l.km + ' km';
          li.appendChild(s1); li.appendChild(s2); ul.appendChild(li);
        });
        var go = $('#infoGo'); go.hidden = false; go.href = '/exhibition/' + n.slug; go.setAttribute('data-label', n.name);
        $('#infoPanel').style.setProperty('--accent', n.accent);
        if (animate) gsap.from('#infoPanel > *', { opacity: 0, y: 12, duration: 0.5, stagger: 0.05 });
      }
    }).catch(function () {
      $('#infoMeta').textContent = 'The map data could not be loaded. Please start the Spring Boot server and refresh.';
    });
  }

  /* ------------------------------------------------------------------
     Boot
     ------------------------------------------------------------------ */
  var showEntrance = false;
  if (entrance) {
    var seen = false;
    try { seen = sessionStorage.getItem('lspuEntered') === '1'; } catch (e) {}
    showEntrance = !seen && !location.search && !location.hash;
  }

  openDoorsOnArrival().then(function () {
    if (entrance && showEntrance) { runEntrance(); }
    else {
      if (entrance) entrance.classList.add('is-gone');
      playPageIntro();
    }
  });

  setupTimeline();
  setupLightbox();
  setupTimelineFilter();
  setupNavigator();
})();
