
let socket;

let messageLog,contactList,messageForm,contactNameElement,userNameElement,backButton,messageWrapper,
	noContactElement,contactSelectedElement;

let userName,contactName,lastMessageDate;

window.addEventListener("DOMContentLoaded",() => {

    initializeElements();

    initializeWebSocket();

    fetchUsername();

    fetchContacts();

})

window.onclose = () => {

    socket.close();
}


let initializeElements = () => {

    messageLog = document.querySelector("#messageLog");

    userNameElement = document.querySelector("#userName");

    contactList = document.querySelector("#contactList");

    messageForm = document.querySelector("form[name=messageForm]");

    contactNameElement = document.querySelector("#contactName");

    backButton = document.querySelector("#backButton");

    messageWrapper = document.querySelector(".messages-wrapper");
    
    noContactElement = document.querySelector(".no-contact");
    
    contactSelectedElement = document.querySelector(".contact-selected");

    messageForm.addEventListener("submit",sendMessage );
    
    backButton.addEventListener("click",onBackButtonPressed)

}

let initializeWebSocket  = () => {

    socket = new WebSocket("ws://localhost:8080/chat")

    socket.onopen = onOpen;
    socket.onmessage = onMessage;
    socket.onclose = onClose;
    socket.onerror = onError;

}

let onMessage = (event) => {
	
	console.log(event)

    let message =  JSON.parse(event.data);

    if(message.fromUser == contactName) {

        addMessageToLog(message,"recieved",true)
    }

}

let onClose = (event) => {

    console.log("Socket Clossed");

}

let onOpen = (event) => {

    console.log("Socket Opened");
}

let onError = (event) => {

    console.log("Error Occoured");

    console.log(event);
}

let sendMessage = (event) => {

    event.preventDefault();

    let input = messageForm.querySelector("input[name=message]");

    if(input.value){
    	
    	let message = {
	        fromUser : userName,
	        toUser : contactName,
	        content : input.value,
	        timestamp : new Date().getTime()
	    }

	    socket.send(JSON.stringify(message));

	    addMessageToLog(message,"send",true);
    }
    
    input.value =  "";

}



let addMessageToLog = (message,direction,isLast) => {

    let messageElement = document.createElement("p");

    messageElement.textContent = message.content;

    messageElement.classList.add(direction);
    
    let timeElement = document.createElement("span");
    
    let date = new Date(message.timestamp);
    
    timeElement.textContent = date.toLocaleTimeString("en-Us",{ hour: '2-digit', minute: '2-digit' });
    
    messageElement.appendChild(timeElement);
    
    date = date.toLocaleDateString().replace(/\//g,"-");;
    
    if(lastMessageDate != date){
    	
    	let dateElement = document.createElement("span");
    	
    	dateElement.textContent = date;
    	
    	dateElement.classList.add("date");
    	
    	messageLog.appendChild(dateElement)
    }

    messageLog.appendChild(messageElement);
        
    lastMessageDate = date;
    
    if(isLast){
    	messageElement.scrollIntoView();
    }

}

let emptyMessageLog = () => {

    while(messageLog.firstChild){
        messageLog.removeChild(messageLog.firstChild)
    }

}

let addContactToList = (contact) => {

    var contactElement = document.createElement("li");

    contactElement.textContent = contact.username;

    contactElement.addEventListener("click",onContactSelected)

    contactList.appendChild(contactElement);

}

let fetchContacts = () => {

    fetch("/contacts")
    .then((data) => data.json())
    .then((data) => {

        data.forEach( (contact) => {
            addContactToList(contact)
        })

    })
}

let fetchPreviousMessages = () => {

    fetch("/messages/" + contactName)
    .then((data) => data.json())
    .then((data) => {

        let direction,isLast,length = data.length;
     

        data.forEach( (message,index) => {

            direction = message.fromUser == userName ? "send" : "recieved";

            isLast = length -1 == index;
            
            addMessageToLog(message,direction,isLast);
            
        })
    })

}

let fetchUsername = () => {

    fetch("/user/username")
	.then((data) => data.text())
	.then((data) => {

        userName = data;
        
        userNameElement.textContent = data;
	})
}

let onContactSelected = (event) => {

	emptyMessageLog();
	
    let target = event.currentTarget;

    contactName = target.textContent;

    contactNameElement.textContent = contactName;
    
    fetchPreviousMessages();

    messageWrapper.classList.add("active");
    
    noContactElement.classList.add("hide");
    
    contactSelectedElement.classList.remove("hide");

}

let onBackButtonPressed = (event) => {

    contactName = null;

    messageWrapper.classList.remove("active");
    
    noContactElement.classList.remove("hide");
    
    contactSelectedElement.classList.add("hide");


}


