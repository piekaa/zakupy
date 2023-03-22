export default class YesNoNumberInputPopup extends HTMLElement {

    onYes
    onNo

    constructor(message, inputText, onYes, onNo) {
        super();
        this.onYes = onYes;
        this.onNo = onNo;
        this.innerHTML = `
        <style>
            piekoszek-yes-no-popup {
                position:fixed;
                top: 0;
                width: 100vw;
                height: 100vh;
                background: #022e; 
                display: flex;
                justify-content: center;
                align-items: center;                    
            }
           
            .popup-content {
                color: white;
                font-size: 1.3em;
            
                gap: 16px;
                align-items: center;       
                display: grid;
                grid-template: 
                            "a a"
                            "d e"
                            "b c";
            }
            
           .popup-content button {
                font-size: 1em;
           }
           
           piekoszek-yes-no-popup input {
                font-size: 1em;
                padding: 4px;
           }
            
        </style>
        `;

        const contentDiv = document.createElement("div");

        const messageDiv = document.createElement("div");
        messageDiv.innerText = message;
        messageDiv.style.gridArea = "a";

        const inputDiv = document.createElement("div");
        inputDiv.innerText = inputText;
        inputDiv.style.gridArea = "d";

        const input = document.createElement("input");
        input.type = "number";
        input.style.gridArea = "e";

        const yesButton = document.createElement("button");
        yesButton.style.gridArea = "b";
        const noButton = document.createElement("button");
        noButton.style.gridArea = "c";


        yesButton.innerText = "Tak";

        yesButton.onclick = () => {
            this.remove();
            this.onYes?.(input.value);
        }

        noButton.onclick = () => {
            this.remove();
            this.onNo?.();
        }

        noButton.innerText = "Nie";

        contentDiv.append(messageDiv);

        contentDiv.append(inputText);

        contentDiv.append(input);

        contentDiv.append(yesButton)
        contentDiv.append(noButton);

        contentDiv.classList.add("popup-content")

        this.append(contentDiv);

        document.body.append(this);
    }

}

customElements.define("piekoszek-yes-no-popup", YesNoNumberInputPopup);