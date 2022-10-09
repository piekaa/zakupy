import Header from "/structure/header.js";
import ShoppingFilters from "/zakupy/shopping-filters.js";
import ShoppingItem from "/zakupy/shopping-item.js";
import YesNoPopup from "/common-elements/yes-no-popup.js";

export default class ShoppingPage extends HTMLElement {

    allItems = []
    currentCategories = [];

    constructor() {
        super();
    }

    connectedCallback() {

        this.innerHTML = `
            <style>
            
                piekoszek-shopping-page {
                    display: flex;
                    padding: 8px;
                    flex-direction: column;
                }
            
                .items {
                    display: flex;
                    flex-direction: column;
                    gap: 8px;
                    font-size: 1.3em;
                }
                
                .buy-button {
                    align-self: center;
                    margin-top: 16px;
                }
                
            </style>
        `;

        const itemsDiv = document.createElement("div");
        itemsDiv.classList.add("items")


        fetch("/api/item")
            .then(res => res.json())
            .then(items => {
                this.allItems = items;
                this.append(new Header("Zakupy"));
                this.append(new ShoppingFilters(categories => {
                    this.currentCategories = categories;
                    itemsDiv.innerHTML = "";
                    console.log(categories);
                    this.allItems
                        .sort((a, b) => a.inCart - b.inCart)
                        .forEach(item => {
                            if (categories.some(c => item.categories.map(ic => ic._id).includes(c))) {
                                itemsDiv.append(new ShoppingItem(item))
                            }
                        })
                }));

                this.append(itemsDiv);

                const buyButton = document.createElement("button");
                buyButton.innerText = "Zakończ zakupy";
                buyButton.classList.add("buy-button");

                buyButton.onclick = () => {
                    new YesNoPopup("Na pewno zakończyć zakupy?", () => {
                        fetch("/api/cart/finish",
                            {
                                method: "POST",
                                body: JSON.stringify({
                                    categories: this.currentCategories
                                })
                            })
                            .then(res => {
                                if (res.ok) {
                                    window.location.reload();
                                } else {
                                    console.trace(res.status)
                                }
                            })
                    });
                }
                this.append(buyButton);
            });
    }

}

customElements.define("piekoszek-shopping-page", ShoppingPage);