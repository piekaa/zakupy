import Circle from "/common-elements/circle.js";

export default class CategoryPickerListItem extends HTMLElement {

    name
    color

    constructor(name, color) {
        super();

        this.name = name;
        this.color = color;
    }

    connectedCallback() {

        this.innerHTML = `
        
            <style>
                piekoszek-category-picker-list-item {
                    display: flex;
                    flex-direction: row;
                    gap: 10px;
                }
            </style>
        `;

        this.append(new Circle(this.color, 30));

        const nameDiv = document.createElement("div");
        nameDiv.innerText = this.name;

        this.append(nameDiv);
    }


}

customElements.define("piekoszek-category-picker-list-item", CategoryPickerListItem);