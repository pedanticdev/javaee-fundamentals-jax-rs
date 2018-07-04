if (typeof (EventSource) !== "undefined") {
    var source = new EventSource("http://localhost:8080/javaee-fundamentals-jax-rs/api/v1/sse");

    source.onmessage = function (evt) {
        console.log(evt);
        document.getElementById("message").innerText += evt.data + "\n"
    };

    source.addEventListener("employee", function (event) {
        console.log(event);
        document.getElementById("employee").innerText += event.data + "\n"

    });
} else {
    document.getElementById("message").innerText = "Sorry! Your browser doesn't support SSE";

}