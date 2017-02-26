$(document).ready(function() {
  $("#version").html("v0.14");
  
  $("#searchbutton").click( function (e) {
    displayModal();
  });
  
  $("#searchfield").keydown( function (e) {
    if(e.keyCode == 13) {
      displayModal();
    }	
  });
  
  function displayModal() {
    $(  "#myModal").modal('show');
    $("#status").html("Searching...");
    $("#dialogtitle").html("Search for: "+$("#searchfield").val());
    $("#previous").hide();
    $("#next").hide();
    $.getJSON('/search/' + $("#searchfield").val() , function(data) {
      renderQueryResults(data);
    });
  }
  
  $("#next").click( function(e) {
    e.preventDefault();
//TODO: Implement 'next' button
  });
  
  $("#previous").click( function(e) {
    e.preventDefault();
//TODO: Implement 'previous' button
  });

  function renderQueryResults(data) {
    
    if (data.error != undefined) {
      $("#status").html("Error: "+data.error);
    } else {
      $("#status").html(""+data.num_results+" result(s)");
      $("#next").show();
      $("#previous").show();
      
      //TODO: Show/hide 'previous' and 'next' buttons as appropriate
      //TODO: Show the pictures in the dialog box (they should go into the 'photo0'..'photo3' cells)
     }
   }
});
