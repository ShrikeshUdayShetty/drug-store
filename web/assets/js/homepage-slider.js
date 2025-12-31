class HomepageSlider {
    constructor(root) {
        this.root = root;
        this.track = root.querySelector('[data-slider-track]');
        this.track.classList.add('is-blurred');
        this.slides = Array.from(root.querySelectorAll('[data-slide]'));
        this.prevBtn = root.querySelector('[data-slider-prev]');
        this.nextBtn = root.querySelector('[data-slider-next]');
        this.indicators = root.querySelector('[data-slider-indicators]');
        this.currentIndex = 0;
        this.autoPlayInterval = null;
        this.init();
    }

    init() {
        if (!this.slides.length) return;
        this.renderIndicators();
        this.bindEvents();
        this.goToSlide(0);
        this.startAutoplay();
        window.addEventListener('visibilitychange', () => {
            if (document.hidden) {
                this.stopAutoplay();
            } else {
                this.startAutoplay(true);
            }
        });
    }

    renderIndicators() {
        if (!this.indicators) return;
        this.indicators.innerHTML = this.slides
            .map((_, index) => `<button type="button" data-indicator="${index}" aria-label="Go to slide ${index + 1}"></button>`)
            .join('');
        this.indicatorButtons = Array.from(this.indicators.querySelectorAll('button'));
    }

    bindEvents() {
        this.prevBtn?.addEventListener('click', () => this.goToSlide(this.currentIndex - 1));
        this.nextBtn?.addEventListener('click', () => this.goToSlide(this.currentIndex + 1));
        this.indicatorButtons?.forEach((btn) => {
            btn.addEventListener('click', () => {
                const target = Number(btn.dataset.indicator);
                this.goToSlide(target);
            });
        });
        this.root.addEventListener('mouseenter', () => this.stopAutoplay());
        this.root.addEventListener('mouseleave', () => this.startAutoplay(true));
        window.addEventListener('resize', () => this.updateSlidePositions());
    }

    goToSlide(index) {
        if (!this.slides.length) return;
        if (index < 0) index = this.slides.length - 1;
        if (index >= this.slides.length) index = 0;
        this.currentIndex = index;
        const offset = this.slides[index].offsetLeft;
        this.track.style.transform = `translateX(-${offset}px)`;
        this.slides.forEach((slide, idx) => slide.classList.toggle('is-active', idx === index));
        this.indicatorButtons?.forEach((btn, idx) => btn.classList.toggle('is-active', idx === index));
    }

    updateSlidePositions() {
        this.goToSlide(this.currentIndex);
    }

    startAutoplay(reset = false) {
        if (reset) this.stopAutoplay();
        if (this.autoPlayInterval) return;
        this.autoPlayInterval = setInterval(() => {
            this.goToSlide(this.currentIndex + 1);
        }, 5000);
    }

    stopAutoplay() {
        if (this.autoPlayInterval) {
            clearInterval(this.autoPlayInterval);
            this.autoPlayInterval = null;
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const slider = document.querySelector('[data-homepage-slider]');
    if (slider) {
        new HomepageSlider(slider);
    }
});
