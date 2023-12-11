import Circle from "/common-elements/circle.js";
import Authorization from "/auth/Authorization.js";

export default class CategoryItem extends HTMLElement {

    id
    name
    color

    constructor(id, name = "unknown", color = "#ff00ff") {
        super();
        this.name = name;
        this.color = color;
        this.id = id;
    }

    connectedCallback() {
        this.innerHTML = `
        <style>
            piekoszek-category-item {
                display: flex;
                gap: 16px;
                align-items: center;
            }
            
            .category-item-content {
                display: flex;
                gap: 8px;
                font-size: 1.3em;
            }
            
        </style>
        `;

        const itemContent = document.createElement("div");
        itemContent.classList.add("category-item-content");

        const nameDiv = document.createElement("div");
        nameDiv.innerText = this.name;

        itemContent.append(nameDiv);
        itemContent.append(new Circle(this.color, 28));
        const button = document.createElement("button");
        button.onclick = () => {
            fetch(`/api/category/${this.id}`, {
                headers: Authorization.header(),
                method: "delete"
            })
                .then(() => {
                    window.location.reload();
                });
        }
        button.innerText = "-";
        this.append(button);
        this.append(itemContent);

    }
}

customElements.define("piekoszek-category-item", CategoryItem);