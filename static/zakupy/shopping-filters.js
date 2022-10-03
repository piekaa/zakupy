import Circle from "/common-elements/circle.js";
import Categories from "/kategorie/categories.js";

export default class ShoppingFilters extends HTMLElement {

    categoriesChangeCallback
    circles = []

    constructor(onCategoriesChange) {
        super();
        this.categoriesChangeCallback = onCategoriesChange;
    }

    connectedCallback() {
        this.innerHTML = `
        <style>
            
            piekoszek-shoppig-filters {
                display: block;
                margin: 16px 0;
                padding-left: 16px;
            }
            
            .filters {
                display: flex;
                gap: 16px;
            }
            
            .filters piekoszek-circle {
            border: 3px solid transparent;
            }
            
            .filters piekoszek-circle.selected {
                border: 3px solid black;
            }
            
        </style>
         
        `;

        const filtersDiv = document.createElement("div")
        filtersDiv.classList.add("filters");

        Categories.fetchAll()
            .then(categories => {

                const loadedSelectedIds = JSON.parse(localStorage.getItem("selected-filters") || "[]");

                this.categoriesChangeCallback?.(loadedSelectedIds);

                categories.forEach(category => {
                    const circle = new Circle(category.color, 30);
                    this.circles.push(circle);
                    circle._id = category._id;

                    if (loadedSelectedIds.includes(category._id)) {
                        circle.classList.add("selected");
                    }

                    circle.onclick = () => {
                        if (circle.classList.contains("selected")) {
                            circle.classList.remove("selected");
                        } else {
                            circle.classList.add("selected");
                        }

                        const selectedIds = this.circles
                            .filter(c => c.classList.contains("selected"))
                            .map(c => c._id);

                        this.categoriesChangeCallback?.(selectedIds);

                        localStorage.setItem("selected-filters", JSON.stringify(selectedIds));

                    }
                    filtersDiv.append(circle)
                });
            });

        this.append(filtersDiv);
    }
}

customElements.define("piekoszek-shoppig-filters", ShoppingFilters);