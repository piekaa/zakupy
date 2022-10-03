import Circle from "/common-elements/circle.js";
import Categories from "/kategorie/categories.js";
import CategoryPickerListItem from "/backlog/category-picker-list-item.js";

export default class CategoryPicker extends HTMLElement {

    itemId
    currentCategories

    constructor(itemId, currentCategories) {
        super();
        this.itemId = itemId;
        this.currentCategories = currentCategories;
    }

    connectedCallback() {

        this.innerHTML = `
        <style> 
            body {
                position: absolute;
            }
            
            piekoszek-category-picker {
                position:absolute;
                margin: -8px;
                top: 0;
                width: 100vw;
                height: 100vh;
                background: rgb(0 0 0 / 0.9);
                display: flex;
                justify-content: center;
                align-items: center;
                flex-direction: column;
                gap: 20px;
                color: white;
            }
            
            .category-list {
                display: flex;
                flex-direction: column;
                gap: 10px;
                
            }
            
        </style>
        
        <div>
            Obecne kategorie
        </div>
        <div id="currentCategories" class="category-list">
        
        </div>
        
        <div>
            Dostępne kategorie
        </div>
        <div id="allCategories" class="category-list">
            
        </div>

        <button>Ok</button>
`;

        this.getElementsByTagName("button")[0].onclick = () => {
            this.remove();
        }

        this.currentCategories.forEach(category => {
            const categoryItem = new CategoryPickerListItem(category.name, category.color);
            categoryItem.onclick = () => {
                fetch("/api/item/category", {
                    method: "PUT",
                    body: JSON.stringify({
                        _id: this.itemId,
                        categories: this.currentCategories.filter(c => c._id !== category._id)
                    })
                })
                    .then(() => {
                        this.remove();
                        window.location.reload();
                    });
            }
            document.getElementById("currentCategories")
                .append(categoryItem);
        });

        Categories.fetchAll()
            .then(categories => {

                categories = categories.filter(category => !this.currentCategories.some(c => c._id === category._id))

                categories.forEach(category => {
                    const categoryItem = new CategoryPickerListItem(category.name, category.color);
                    categoryItem.onclick = () => {
                        fetch("/api/item/category", {
                            method: "PUT",
                            body: JSON.stringify({
                                _id: this.itemId,
                                categories: [
                                    category, ...this.currentCategories
                                ]
                            })
                        })
                            .then(() => {
                                this.remove();
                                window.location.reload();
                            })
                    }
                    document.getElementById("allCategories").append(categoryItem);
                });
            })
    }
}

customElements.define("piekoszek-category-picker", CategoryPicker);