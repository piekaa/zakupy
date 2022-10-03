export default class YesNoPopup extends HTMLElement {

    onYes
    onNo

    constructor(message, onYes, onNo) {
        super();
        this.onYes = onYes;
        this.onNo = onNo;
        this.innerHTML = `
        <style>
            piekoszek-yes-no-popup {
                position:absolute;
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
            
                display: grid;
                grid-template: 
                            "a a"
                            "b c";
            }
            
           .popup-content button {
                font-size: 1em;
           }
            
        </style>
        `;

        const contentDiv = document.createElement("div");

        const messageDiv = document.createElement("div");
        messageDiv.innerText = message;

        messageDiv.style.gridArea = "a";

        const yesButton = document.createElement("button");
        yesButton.style.gridArea = "b";
        const noButton = document.createElement("button");
        noButton.style.gridArea = "c";


        yesButton.innerText = "Tak";

        yesButton.onclick = () => {
            this.remove();
            this.onYes?.();
        }

        noButton.onclick = () => {
            this.remove();
            this.onNo?.();
        }

        noButton.innerText = "Nie";

        contentDiv.append(messageDiv);

        contentDiv.append(yesButton)
        contentDiv.append(noButton);

        contentDiv.classList.add("popup-content")

        this.append(contentDiv);

        document.body.append(this);
    }

}

customElements.define("piekoszek-yes-no-popup", YesNoPopup);