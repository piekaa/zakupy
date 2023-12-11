import Header from "/structure/header.js";
import ShoppingFilters from "/zakupy/shopping-filters.js";
import ShoppingItem from "/zakupy/shopping-item.js";
import YesNoNumberInputPopup from "/common-elements/yes-no-number-input-popup.js";
import Authorization from "/auth/Authorization.js";

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
                    gap: 2px;
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


        fetch("/api/item",
            {
                headers: Authorization.header(),
            })
            .then(res => res.json())
            .then(items => {
                this.allItems = items;
                this.append(new Header("Zakupy"));
                this.append(new ShoppingFilters(categories => {
                    this.currentCategories = categories;
                    itemsDiv.innerHTML = "";
                    let previousId = undefined;
                    this.allItems
                        .sort((a, b) => a.inCart - b.inCart)
                        .sort((a, b) => a.missing - b.missing)
                        .forEach(item => {
                            if (categories.some(c => item.categories.map(ic => ic._id).includes(c))) {
                                itemsDiv.append(new ShoppingItem(item, previousId));
                                previousId = item._id;
                            }
                        })
                    window.scrollTo(0, parseInt(sessionStorage.getItem("scrollTo") ?? "0"));
                    document.getElementById(new URLSearchParams(window.location.search).get("scrollToId"))?.scrollIntoView();
                }));

                this.append(itemsDiv);

                const buyButton = document.createElement("button");
                buyButton.innerText = "Zakończ zakupy";
                buyButton.classList.add("buy-button");

                buyButton.onclick = () => {
                    new YesNoNumberInputPopup("Na pewno zakończyć zakupy?",
                        "Koszt", (price) => {
                            fetch("/api/cart/finish",
                                {
                                    method: "POST",
                                    headers: Authorization.header(),
                                    body: JSON.stringify({
                                        categories: this.currentCategories,
                                        price: Math.round(parseFloat(price || "0") * 100)
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