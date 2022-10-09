export default class Line extends HTMLElement {

    connectedCallback() {

        this.innerHTML = `
            <style>
                .line {
                    display: block;
                    width: 100%;
                    height: 2px;
                    background: #00000044;
                }
            </style>
        `;

        this.classList.add("line");
    }

}

customElements.define("piekoszek-line", Line);