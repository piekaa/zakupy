import Categories from "/kategorie/categories.js";
import CategoryPickerListItem from "/backlog/category-picker-list-item.js";

export default class DefaultCategoryPicker extends HTMLElement {

    callback;

    constructor(callback = () => {
    }) {
        super();
        this.callback = callback;
    }


    connectedCallback() {

        this.innerHTML = `
        <style> 
            body {
                position: fixed;
            }
            
            piekoszek-default-category-picker {
                position:absolute;
                margin-left: -8px;
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
            Dostępne kategorie
        </div>
        <div id="allCategories" class="category-list">
            
        </div>

        <button>Ok</button>
`;

        this.getElementsByTagName("button")[0].onclick = () => {
            this.remove();
        }

        Categories.fetchAll()
            .then(categories => {
                categories.forEach(category => {
                    const categoryItem = new CategoryPickerListItem(category.name, category.color);
                    categoryItem.onclick = () => {
                        this.callback(category);
                        localStorage.lastCategory = JSON.stringify(category);
                        this.remove();
                    }
                    document.getElementById("allCategories").append(categoryItem);
                });
            })
    }
}

customElements.define("piekoszek-default-category-picker", DefaultCategoryPicker);