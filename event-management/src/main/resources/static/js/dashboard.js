document.addEventListener("DOMContentLoaded", () => {
    const registeredEventsContainer = document.getElementById("registered-events");
    const availableEventsContainer = document.getElementById("available-events");
    const eventsAttendedCounter = document.getElementById("events-attended");
    const logoutBtn = document.getElementById("logout-btn");

    // ✅ Fetch Current User (OAuth2 or Normal Login)
    fetch("/api/users/me", {
        credentials: "include"  // Include session cookies
    })
    .then(res => {
        if (!res.ok) throw new Error("Not logged in");
        return res.json();
    })
    .then(user => {
        if (!user.email) {
            alert("Please login first.");
            window.location.href = "/login.html";
            return;
        }

        // ✅ Display Welcome Name
        document.getElementById("user-name").innerText = `Welcome, ${user.name}`;

        // ✅ Load Events for This User
        loadRegisteredEvents(user.email);
        loadAvailableEvents(user.email);
    })
    .catch(err => {
        console.error("Login check failed:", err);
        window.location.href = "/login.html";
    });

    // ✅ Load Registered Events
    function loadRegisteredEvents(email) {
        fetch(`/api/users/eventsAttended?email=${email}`, { credentials: "include" })
        .then(res => res.json())
        .then(data => {
            if (data.eventsAttended !== undefined) {
                eventsAttendedCounter.innerText = data.eventsAttended;
            }
        });

        fetch(`/api/events/registered?email=${email}`, { credentials: "include" })
        .then(res => res.json())
        .then(events => {
            registeredEventsContainer.innerHTML = "";
            events.forEach(event => {
                const div = document.createElement("div");
                div.classList.add("event-card");
                div.innerHTML = `
                    <h4>${event.title}</h4>
                    <p>${event.description}</p>
                    <p><b>Date:</b> ${event.date}</p>
                    <p><b>Location:</b> ${event.location}</p>
                `;
                registeredEventsContainer.appendChild(div);
            });
        });
    }

    // ✅ Load Available Events
    function loadAvailableEvents(email) {
        fetch("/api/events", { credentials: "include" })
        .then(res => res.json())
        .then(events => {
            availableEventsContainer.innerHTML = "";
            events.forEach(event => {
                const div = document.createElement("div");
                div.classList.add("event-card");
                div.innerHTML = `
                    <h4>${event.title}</h4>
                    <p>${event.description}</p>
                    <button class="register-btn" data-id="${event.id}">Register</button>
                `;
                availableEventsContainer.appendChild(div);
            });

            // Add event listeners for registration
            document.querySelectorAll(".register-btn").forEach(btn => {
                btn.addEventListener("click", () => registerForEvent(btn.dataset.id, email));
            });
        });
    }

    // ✅ Register for Event
    function registerForEvent(eventId, email) {
        fetch(`/api/users/registerEvent/${eventId}?email=${email}`, {
            method: "POST",
            credentials: "include"
        })
        .then(res => res.json())
        .then(data => {
            alert(data.message);
            if (data.status === "success") {
                eventsAttendedCounter.innerText = data.eventsAttended;
                loadRegisteredEvents(email);
            }
        });
    }

    // ✅ Logout
    logoutBtn.addEventListener("click", () => {
        fetch("/logout", { credentials: "include" })
            .then(() => window.location.href = "/login.html");
    });

});
