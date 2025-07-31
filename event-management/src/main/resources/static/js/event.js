document.getElementById("eventForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const user = JSON.parse(localStorage.getItem("user"));
  const event = {
    title: document.getElementById("title").value,
    description: document.getElementById("description").value,
    date: document.getElementById("date").value,
    time: document.getElementById("time").value,
    location: document.getElementById("location").value,
    ticketPrice: document.getElementById("ticketPrice").value,
    userId: user.id,
  };
  const res = await fetch("/api/events", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(event),
  });
  if (res.ok) {
    alert("Event created!");
    window.location.href = "dashboard.html";
  } else {
    alert(await res.text());
  }
});
