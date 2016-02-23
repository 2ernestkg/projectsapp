$().ready(function() {
	$("#fName").autocomplete({
		source: function(request, response){
			$.ajax({
				url: "/employee/suggest",
				dataType: "json",
				data: {text: request.term},
				success: function(data) {
					response($.map(data.suggestions, function(item){
						return {
							label: item.fName + " " + item.lName,
							value: item.fName,
							id: item.id,
							fName: item.fName,
							lName: item.lName
						}
					}));
				}
			});
		},
		minLength: 2,
		select: function(event, data) {
			$("#id").val(data.item.id);
			$("#fName").val(data.item.fName);
			$("#lName").val(data.item.lName);
		},

		html: true, // optional (jquery.ui.autocomplete.html.js required)

		// optional (if other layers overlap autocomplete list)
		open: function(event, ui) {
			$(".ui-autocomplete").css("z-index", 1000);
		}
	});

});
$().ready(function() {
	$( "#projectForm #beginDate" ).datepicker({
		dateFormat: "yy-mm-dd"
	});
	$( "#projectForm #endDate" ).datepicker({
		dateFormat: "yy-mm-dd"
	});
});