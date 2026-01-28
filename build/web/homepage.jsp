<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.drugstore.model.Medicine" %>
<%
    List<Medicine> featuredMedicines = (List<Medicine>) request.getAttribute("featuredMedicines");
    if (featuredMedicines == null) {
        featuredMedicines = java.util.Collections.emptyList();
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SkPharma | Home</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
    <style>
        .hero-wrapper {
            position: relative;
            width: 100vw;
            left: 50%;
            transform: translateX(-50%);
            margin-top: clamp(1.25rem, 3vw, 2.75rem);
        }
        .hero-slider {
            position: relative;
            overflow: hidden;
            width: 100%;
            height: clamp(420px, 55vh, 620px);
            border-radius: 0 0 28px 28px;
            box-shadow: 0 32px 60px rgba(15, 23, 42, 0.16);
        }
        .hero-slider::after {
            content: "";
            position: absolute;
            inset: 0;
            background: linear-gradient(180deg, rgba(255,255,255,0) 40%, rgba(255,255,255,0.45) 100%);
            pointer-events: none;
        }
        .slider-track {
            display: flex;
            transition: transform 0.8s ease;
            height: 100%;
        }
        .hero-slide {
            flex: 0 0 100%;
            height: 100%;
            position: relative;
        }
        .hero-slide picture,
        .hero-slide img {
            width: 100%;
            height: 100%;
        }

        .hero-slide img {
            object-fit: contain;
            object-position: center;
            transition: filter 0.4s ease, transform 0.4s ease;
        }

        .hero-slider .is-blurred .hero-slide img {
            filter: blur(2px);
            transform: scale(1.04);
        }
        .slider-content {
            position: absolute;
            inset: 0;
            display: grid;
            align-content: center;
            z-index: 2;
            padding: clamp(2rem, 8vw, 6rem);
            color: #0f172a;
            gap: 1rem;
            max-width: min(580px, 92%);
        }

        .slider-content > * {
            margin: 0;
        }

        .slider-content .pill {
            font-size: 0.8rem;
            text-transform: uppercase;
        }
        .slider-content h1 {
            margin: 0;
            font-size: clamp(2.2rem, 4vw, 3.5rem);
            letter-spacing: -0.02em;
        }
        .slider-content p {
            margin: 0;
            font-size: clamp(1.1rem, 2vw, 1.35rem);
            line-height: 1.6;
            opacity: 0.9;
        }
        .slider-controls {
            position: absolute;
            inset: auto clamp(1.5rem, 4vw, 3rem) clamp(1.4rem, 3vw, 2.6rem) auto;
            display: flex;
            gap: 0.75rem;
            z-index: 3;
        }
        .slider-controls button,
        [data-slider-indicators] button {
            background: rgba(15, 23, 42, 0.2);
            border: 1px solid rgba(15, 23, 42, 0.35);
            color: #111827;
            width: 44px;
            height: 44px;
            border-radius: 999px;
            display: grid;
            place-items: center;
            cursor: pointer;
            transition: background 0.2s ease, transform 0.2s ease;
        }
        .slider-controls button:hover,
        [data-slider-indicators] button:hover {
            background: rgba(59, 130, 246, 0.2);
            transform: translateY(-2px);
        }
        .slider-controls button svg {
            width: 18px;
            height: 18px;
        }
        [data-slider-indicators] {
            position: absolute;
            left: 50%;
            bottom: clamp(1.5rem, 3vw, 2.5rem);
            transform: translateX(-50%);
            display: flex;
            gap: 0.6rem;
            z-index: 3;
        }
        [data-slider-indicators] button {
            width: 14px;
            height: 14px;
            border-radius: 50%;
            background: rgba(15, 23, 42, 0.35);
        }
        [data-slider-indicators] button.is-active {
            background: #2563eb;
            transform: scale(1.2);
        }
        @media (max-width: 768px) {
            .hero-wrapper {
                margin-top: 0.5rem;
            }
            .hero-slider {
                height: 55vh;
                border-radius: 0 0 20px 20px;
            }
            .slider-controls {
                inset: auto 1rem 1.2rem auto;
            }
        }
    </style>
</head>
<body>
<jsp:include page="includes/userHeader.jsp"/>
<jsp:include page="includes/alerts.jsp"/>
<h1>Testing Live Website</h1>
<main class="container" style="padding-bottom:3rem;">
    <section class="hero-wrapper">
        <div class="hero-slider" data-homepage-slider>
            <div class="slider-track" data-slider-track>
                <article class="hero-slide" data-slide>
                    <img src="<%= request.getContextPath() %>/images/banner1.jpg" alt="Trusted medicines">
                    <div class="slider-content">
                        <p class="pill" style="width:max-content;background:rgba(59,130,246,0.18);padding:0.4rem 1rem;border-radius:999px;font-weight:600;font-size:0.85rem;">New Arrivals</p>
                        <h1>SkPharma brings premium pharmaceuticals to your doorstep</h1>
                        <p>Discover a curated range of essentials from trusted manufacturers, handpicked for better patient outcomes.</p>
                    </div>
                </article>
                <article class="hero-slide" data-slide>
                    <img src="<%= request.getContextPath() %>/images/banner2.png" alt="Healthcare experts">
                    <div class="slider-content">
                        <p class="pill" style="width:max-content;background:rgba(16,185,129,0.2);padding:0.4rem 1rem;border-radius:999px;font-weight:600;font-size:0.85rem;">Trusted Vendors</p>
                        <h1>Partnering with licensed professionals across the country</h1>
                        <p>We onboard certified vendors to ensure every product reaches you with complete authenticity.</p>
                    </div>
                </article>
                <article class="hero-slide" data-slide>
                    <img src="<%= request.getContextPath() %>/images/banner3.png" alt="Customer support">
                    <div class="slider-content">
                        <p class="pill" style="width:max-content;background:rgba(234,179,8,0.25);padding:0.4rem 1rem;border-radius:999px;font-weight:600;font-size:0.85rem;">24/7 Support</p>
                        <h1>Your wellness journey is our prime focus</h1>
                        <p>From guidance on medicines to quick order assistance, our support specialists are here round the clock.</p>
                    </div>
                </article>
            </div>
            <div class="slider-controls">
                <button type="button" data-slider-prev aria-label="Previous slide">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M15 6l-6 6 6 6" stroke-linecap="round" stroke-linejoin="round"/></svg>
                </button>
                <button type="button" data-slider-next aria-label="Next slide">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M9 6l6 6-6 6" stroke-linecap="round" stroke-linejoin="round"/></svg>
                </button>
            </div>
            <div data-slider-indicators></div>
        </div>
    </section>

    <section class="search-bar">
        <div class="search-card">
            <input type="text" id="searchInput" placeholder="Search by medicine name">
            <button class="btn btn-primary" id="searchBtn">Search</button>
        </div>
    </section>

    <section>
        <h2 class="section-title">Top Medicines</h2>
        <div class="grid" style="grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));" id="medicineGrid">
            <%
                if (featuredMedicines.isEmpty()) {
            %>
            <div class="empty-state">No medicines available yet. Please check back soon.</div>
            <%
                } else {
                    for (Medicine medicine : featuredMedicines) {
            %>
            <form action="<%= request.getContextPath() %>/cart/add" method="post" class="medicine-card purchase-form" data-name="<%= medicine.getName().toLowerCase() %>">
                <input type="hidden" name="medicineId" value="<%= medicine.getId() %>">
                <img src="<%= (medicine.getImageUrl() != null && !medicine.getImageUrl().trim().isEmpty()) ? (medicine.getImageUrl().startsWith("/") ? request.getContextPath() + medicine.getImageUrl() : medicine.getImageUrl()) : "https://images.unsplash.com/photo-1580281658629-acf6f5f1df59" %>" alt="<%= medicine.getName() %>" class="medicine-image">
                <div>
                    <h3><%= medicine.getName() %></h3>
                    <p style="color:var(--muted);"><%= medicine.getDescription() != null ? medicine.getDescription() : "Medicine description coming soon." %></p>
                </div>
                <div class="price-row">
                    <span class="price">₹<%= medicine.getPricePerUnit() %></span>
                    <span class="discount"><%= medicine.getDiscountPercentage() %>% off</span>
                </div>
                <div class="quantity-picker">
                    <label for="home-qty-<%= medicine.getId() %>">Quantity</label>
                    <input type="number" id="home-qty-<%= medicine.getId() %>" name="quantity" min="1" value="1">
                </div>
                <div class="card-actions">
                    <button type="submit" class="btn btn-outline" style="flex:1;">Add to Cart</button>
                    <button type="submit" formaction="<%= request.getContextPath() %>/order/buy-now" class="btn btn-primary" style="flex:1;">Buy</button>
                </div>
            </form>
            <%
                    }
                }
            %>
        </div>
    </section>
</main>

<script src="<%= request.getContextPath() %>/assets/js/homepage-slider.js"></script>
<script>
    const searchInput = document.getElementById('searchInput');
    const searchBtn = document.getElementById('searchBtn');
    const grid = document.getElementById('medicineGrid');

    async function performSearch() {
        const query = searchInput.value.trim();
        const response = await fetch(`<%= request.getContextPath() %>/medicines/search?q=` + encodeURIComponent(query));
        if (response.ok) {
            const html = await response.text();
            grid.innerHTML = html;
        }
    }

    searchBtn.addEventListener('click', performSearch);
    searchInput.addEventListener('input', () => {
        clearTimeout(window.searchTimeout);
        window.searchTimeout = setTimeout(performSearch, 400);
    });
</script>
</body>
</html>
