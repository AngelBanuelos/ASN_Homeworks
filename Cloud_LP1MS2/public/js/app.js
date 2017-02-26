$(document).ready(function () {

    var imagesArray = null;
    var currentPage = 0;
    var pageSize = 0;
    var imagePerPage = 4;
    var num_results = 0;

    $("#version").html("v0.14");

    $("#searchbutton").click(function (e) {
        displayModal();
    });

    $("#searchfield").keydown(function (e) {
        if (e.keyCode == 13) {
            displayModal();
        }
    });

    function displayModal() {
        $("#myModal").modal('show');
        $("#status").html("Searching...");
        $("#dialogtitle").html("Search for: " + $("#searchfield").val());
        $("#previous").hide();
        $("#next").hide();
        $.getJSON('/search/' + $("#searchfield").val(), function (data) {
            renderQueryResults(data);
        });
    }

    $("#next").click(function (e) {
        e.preventDefault();
        $("#previous").show();
        currentPage++;
        if (pageSize == currentPage) {
            $("#next").hide();
        }
        renderImages();
    });

    $("#previous").click(function (e) {
        e.preventDefault();
        $("#next").show();
        currentPage--;
        if (currentPage <= 1) {
            $("#previous").hide();
        }
        renderImages();
    });

    function renderQueryResults(data) {

        if (data.error != undefined) {
            $("#status").html("Error: " + data.error);
        } else {
            $("#status").html("" + data.num_results + " result(s)");
            num_results = data.num_results;
            if (data.num_results > 0) {
                currentPage = 1;
                pageSize = data.num_results / imagePerPage;

                if (data.num_results > 4) {
                    $("#next").show();
                }
                imagesArray = data.results;
                renderImages();
            } else {
                clearImages();
            }
        }
    }

    function renderImages() {
        clearImages();
        var scalar = currentPage * imagePerPage;
        var j = 0;
        for (var i = scalar - imagePerPage; i < scalar && i < num_results; i++) {
            var photo = "#photo" + j;
            $(photo).html("<img src='" + imagesArray[i] + "' height='250' width='250'  >");
            j++;
        }
    }

    function clearImages() {
        for (var i = 0; i < 4; i++) {
            var photo = "#photo" + i;
            $(photo).html(i);
        }
    }

});
