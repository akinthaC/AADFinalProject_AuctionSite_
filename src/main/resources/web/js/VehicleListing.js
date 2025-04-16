$(document).ready(function () {
    $.ajax({
        url: "http://localhost:8080/api/listings/Vehicle/active", // Adjust URL if needed
        method: "GET",
        dataType: "json",
        success: function (response) {
            console.log(response);

            const auctionContainer = $("#active-auctions");
            auctionContainer.empty(); // Clear previous listings

            if (Array.isArray(response.data)) {
                // Limit to 10 items
                response.data.slice(0, 8).forEach(listing => {
                    let auctionCard = `
                    <div class="auction-card">
                        <img src="/static/uploads/${listing.mainImage}" alt="${listing.vehicleType}">
                        <h3>${listing.vehicleType}</h3>
                        <p>Starting at $${listing.startingBidPrice}</p>`;

                    if (listing.sellingOption === "bidding") {
                        // Calculate auction end time
                        const endTime = new Date(new Date(listing.bidStartedDate).getTime() + listing.bidDuration * 86400000);

                        auctionCard += `
                        <p class="timer" data-endtime="${endTime.toISOString()}">00:00:00</p>
                        <button class="bid-now">Bid Now</button>`;
                    } else if (listing.sellingOption === "fixed") {
                        auctionCard += `
                        <p class="buy-now">Fixed Price: $${listing.price}</p>
                        <button class="buy-now-btn">Buy Now</button>`;
                    }

                    auctionCard += `</div>`; // Close auction-card div
                    auctionContainer.append(auctionCard);
                });

                startTimers(); // Start countdown timers
            } else {
                console.error("Invalid data format, expected an array of listings.");
            }
        },
        error: function (xhr, status, error) {
            console.error("Error loading auction data:", error);
        }
    });


    // Load Upcoming (Pending) Auctions
    $.ajax({
        url: "http://localhost:8080/api/listings/Vehicle/pending",
        method: "GET",
        dataType: "json",
        success: function (response) {
            console.log(response); // Debugging

            const pendingAuctionContainer = $("#pending-auctions");
            pendingAuctionContainer.empty(); // Clear previous listings

            if (Array.isArray(response.data)) {
                response.data.slice(0, 8).forEach(listing => {
                    const today = new Date();
                    const bidStartDate = new Date(listing.bidStartedDate); // Convert start date to Date object
                    console.log(bidStartDate);

                    const timeLeftToStart = bidStartDate - today; // Time until auction starts
                    console.log(timeLeftToStart)

                    let timerText;
                    if (timeLeftToStart > 0) {
                        // Auction hasn't started yet, show countdown to start
                        timerText = `<p style="color: #ff6600" class="start-timer" data-starttime="${bidStartDate.toISOString()}">Starts in 00:00:00</p>`;
                    } else {
                        // Auction has started
                        timerText = `<p style="color: #0a1c93">Auction Started</p>`;
                    }

                    let auctionCard = `
                    <div class="auction-card">
                        <img src="/static/uploads/${listing.mainImage}" alt="${listing.vehicleType}">
                        <h3>${listing.vehicleType}</h3>
                        <p>Starting at $${listing.startingBidPrice}</p>
                        ${timerText}
                    </div>`;
                    pendingAuctionContainer.append(auctionCard);
                });

                // Start countdown timers
                startCountdownToStart();
            } else {
                console.error("Invalid pending auctions data.");
            }
        },
        error: function (xhr, status, error) {
            console.error("Error loading pending auctions:", error);
        }
    });

    function startTimers() {
        setInterval(() => {
            $(".timer").each(function () {
                const endTime = new Date($(this).data("endtime"));
                const now = new Date();
                let timeLeft = endTime - now;

                if (timeLeft <= 0) {
                    $(this).text("00:00:00"); // Auction ended
                    return;
                }

                const hours = String(Math.floor(timeLeft / (1000 * 60 * 60))).padStart(2, '0');
                const minutes = String(Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60))).padStart(2, '0');
                const seconds = String(Math.floor((timeLeft % (1000 * 60)) / 1000)).padStart(2, '0');

                $(this).text(`${hours}:${minutes}:${seconds}`);
            });
        }, 1000);
    }

    function startCountdownToStart() {
        setInterval(() => {
            $(".start-timer").each(function () {
                const startTime = new Date($(this).data("starttime"));
                const now = new Date();
                let timeLeft = startTime - now;

                if (timeLeft <= 0) {
                    $(this).text("Auction Started").css("color", "#0a1c93");
                    return;
                }

                const hours = String(Math.floor(timeLeft / (1000 * 60 * 60))).padStart(2, '0');
                const minutes = String(Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60))).padStart(2, '0');
                const seconds = String(Math.floor((timeLeft % (1000 * 60)) / 1000)).padStart(2, '0');

                $(this).text(`Starts in ${hours}:${minutes}:${seconds}`);
            });
        }, 1000);
    }
});