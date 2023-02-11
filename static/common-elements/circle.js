export default class Circle extends HTMLElement {

    color
    size

    constructor(color = "#ff00ff", size = 20) {
        super();
        this.color = color;
        this.size = size;
    }

    connectedCallback() {
        this.innerHTML = `
        <style>
            piekoszek-circle {
                display: block;
                border-radius: 50%;
                aspect-ratio: 1;
            }
        </style>
        `;

        this.style.width = `${this.size}px`;
        this.style.height = `${this.size}px`;
        this.style.background = this.color;
    }

    setColor(color) {
        this.color = color;
        this.style.background = this.color;
    }
}

customElements.define("piekoszek-circle", Circle);