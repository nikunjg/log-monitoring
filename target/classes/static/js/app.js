function connect() {
	//connect to stomp where stomp endpoint is exposed
	var socket = new SockJS("/log-monitoring-websocket");
	var ws = Stomp.over(socket);

	ws.connect({}, function(frame) {
		ws.subscribe("/log-monitoring-broker/log", function(message) {
		    var span = $('<span />').css("display", "block").html(message.body);
		    $("#main-content").append(span);
		});

	}, function(error) {
	    console.log("STOMP error " + error);
	});
	
    
    	$.ajax({
        url : "/tail/log",
        type : 'GET',
        error : function(xhr, ajaxOptions, thrownError) {
            console.log(xhr.status);
            console.log(thrownError);
        }
    });
}

$(function() {
    connect();
});
