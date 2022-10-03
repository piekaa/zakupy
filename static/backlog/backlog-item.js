import CategoryPicker from "/backlog/category-picker.js";
import Circle from "/common-elements/circle.js";

export default class BacklogItem extends HTMLElement {

    id
    name
    categories

    constructor(id, name, categories = []) {
        super();
        this.id = id;
        this.name = name;
        this.categories = categories;
    }

    connectedCallback() {
        this.innerHTML = `
        <style>
            piekoszek-backlog-item {
                display: flex;
                gap: 16px;
                justify-content: space-between;
            }
            
            .backlog-item-content {
                display: flex;
                flex: 1;
                gap: 10px;
                font-size: 1.3em;
                align-items: center;
            }
            
        </style>
        `;

        const button = document.createElement("button");
        button.onclick = () => {
            console.log("click");
            fetch(`/api/item/${this.id}`, {
                method: "delete"
            })
                .then(() => {
                    window.location.reload();
                });
        }
        button.innerText = "-";
        this.append(button);

        const itemContent = document.createElement("div");
        itemContent.classList.add("backlog-item-content");

        const nameElement = document.createElement("div");
        nameElement.innerText = this.name;
        itemContent.append(nameElement);

        const editCategoriesButton = document.createElement("button");
        editCategoriesButton.onclick = () => {
            this.append(new CategoryPicker(this.id, this.categories));
        }

        this.categories.forEach(category => {
            itemContent.append(new Circle(category.color, 26));
        })
        
        editCategoriesButton.innerText = "+";

        this.append(itemContent);

        this.append(editCategoriesButton);

    }
}

customElements.define("piekoszek-backlog-item", BacklogItem);