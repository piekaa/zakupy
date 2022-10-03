export default class ShoppingItem extends HTMLElement {

    id
    name
    inCart

    constructor(item) {
        super();
        this.name = item.name;
        this.id = item._id;
        this.inCart = item.inCart;
    }

    connectedCallback() {
        this.innerHTML = `
        <style>
            piekoszek-shopping-item {
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 8px;
            }

            .inCart {
                background: #92f989;
            }
        
        </style>
        
        <div>${this.name}</div>
        
        `;

        const button = document.createElement("button");

        button.onclick = () => {
            fetch(`/api/cart/${this.id}`,
                {
                    method: this.inCart ? "DELETE" : "PUT"
                })
                .then(() => window.location.reload())
        }

        button.innerText = this.inCart ? "z koszyka" : "do koszyka";
        this.append(button);

        if (this.inCart) {
            this.classList.add("inCart");
        }
    }
}

customElements.define("piekoszek-shopping-item", ShoppingItem);