const userId = localStorage.getItem("userId");

fetch(`http://localhost:8080/api/users/${userId}/dashboard`)
    .then(res => res.json())
    .then(data => {
        document.getElementById("userName").innerText = `Welcome, ${data.name}`;
        document.getElementById("eventsAttended").innerText = `Events Attended: ${data.eventsAttended}`;

        let eventsList = document.getElementById("availableEvents");
        eventsList.innerHTML = "";
        data.availableEvents.forEach(event => {
            let li = document.createElement("li");
            li.innerHTML = `${event.title} - ${event.date}
                <button onclick="registerEvent(${event.id})">Register</button>`;
            eventsList.appendChild(li);
        });
    });

function registerEvent(eventId) {
    fetch(`http://localhost:8080/api/users/${userId}/registerEvent/${eventId}`, { method: "POST" })
        .then(res => res.text())
        .then(msg => alert(msg));
}
