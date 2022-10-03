export default class CategoryInput extends HTMLElement {

    pickedFunction = undefined;

    constructor(onPick) {
        super();
        this.pickedFunction = onPick;
    }

    connectedCallback() {
        this.innerHTML = `
        <style>
            piekoszek-category-input {
                margin-top: 16px;
                display: flex;
                gap: 16px;
            }
            
            piekoszek-category-input input {
                flex: 5;
                font-size: 1.3em;
            }
            
            piekoszek-category-input input[type="color" i] {
                flex: 1;
                height: auto;
                background: white;
            }
        </style>
        `;

        const textInput = document.createElement("input")
        const colorInput = document.createElement("input")
        colorInput.type = "color";

        const button = document.createElement("button");
        button.innerText = "ok";
        button.onclick = () => {
            this.pickedFunction?.(textInput.value, colorInput.value);
        };

        this.append(textInput, colorInput, button);

    }
}

customElements.define("piekoszek-category-input", CategoryInput);