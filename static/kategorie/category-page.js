import Header from "/structure/header.js";
import CategoryInput from "/kategorie/category-input.js";
import CategoryItem from "/kategorie/category-item.js";
import Authorization from "/auth/Authorization.js";

export default class CategoryPage extends HTMLElement {

    constructor() {
        super();
    }

    connectedCallback() {

        this.innerHTML = `
            <style>
            
                .items-container {
                    margin-top: 16px;
                    display: flex;
                    flex-direction: column;
                    gap: 8px;
                }
            </style>
        `;

        fetch("/api/category", {
            headers: Authorization.header(),
        })
            .then(res => res.json())
            .then(categories => {
                categories.forEach(category => {
                    itemsContainer.append(new CategoryItem(category._id, category.name, category.color));
                });
            });

        const itemsContainer = document.createElement("div");
        itemsContainer.classList.add("items-container");


        this.append(new Header("Kategorie"));
        this.append(new CategoryInput((text, color) => {
            console.log(text);
            console.log(color);

            fetch("/api/category",
                {
                    headers: Authorization.header(),
                    method: "POST",
                    body: JSON.stringify({
                        name: text,
                        color: color
                    })

                })
                .then(res => res.json())
                .then(json => {
                    window.location.reload();
                })
        }))
        this.append(itemsContainer);

        this.classList.add("piekoszek-page");
    }

}

customElements.define("piekoszek-category-page", CategoryPage);