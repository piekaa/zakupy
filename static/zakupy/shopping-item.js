export default class ShoppingItem extends HTMLElement {

    id
    name
    inCart
    missing

    constructor(item) {
        super();
        this.name = item.name;
        this.id = item._id;
        this.inCart = item.inCart;
        this.missing = item.missing || false;
    }

    connectedCallback() {
        this.innerHTML = `
        <style>
            piekoszek-shopping-item {
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding-top: 0;
                padding-bottom: 0;
            }


            .missing {
                background: #f99a89;
                padding: 4px;
            }

            .inCart {
                background: #92f989;
                padding: 4px;
            }
            
            .buttons {
                display: flex;
                flex-direction: row;
                align-items: center;
                gap: 8px;
            }
            
            .cart-icon {
                height: 40px;
                overflow: hidden;
            }
            
            .missing-icon {
                border: 2px solid black;
                width: 30px;
                text-align: center;
                padding-bottom: 4px;
            }
        
        </style>
        
        <div>${this.name}</div>
        `;

        const buttons = document.createElement("div");
        buttons.classList.add("buttons");


        if (!this.inCart) {
            const missing = document.createElement("span");
            missing.innerText = "?";
            missing.classList.add("missing-icon");

            missing.onclick = () => {
                fetch(`/api/missing/${this.id}`,
                    {
                        method: this.missing ? "DELETE" : "PUT"
                    })
                    .then(() => window.location.reload())
            };
            buttons.append(missing);
        }


        const cart = document.createElement("img");
        cart.src = "/img/cart.svg";
        cart.classList.add("cart-icon");

        cart.onclick = () => {
            fetch(`/api/cart/${this.id}`,
                {
                    method: this.inCart ? "DELETE" : "PUT"
                })
                .then(() => window.location.reload())
        }

        buttons.append(cart);


        this.append(buttons);

        if (this.inCart) {
            this.classList.add("inCart");
        }

        if (this.missing) {
            this.classList.add("missing");
        }
    }
}

customElements.define("piekoszek-shopping-item", ShoppingItem);