$(document).ready(function() {
	$('a.remove').click(function(){
		  event.preventDefault();
		  $( this ).parent().parent().parent().hide( 400 );
		 
		})

		// Just for testing, show all items
		  $('a.btn.continue').click(function(){
		    $('li.items').show(400);
		  })

		  $("#cart").find(".qty").each(function(l) {
			  console.log("Attached");
			  var _this = this;
		     $(this).on("input",function(){
					 var amt = parseFloat($(this).val());
					  var regex = /[x\s*\$]/g;
					 var cost = $(this).parent().text();
					 cost = cost.replace(regex,"");
					 
					 cost = parseFloat(cost);
					 cost*=amt;
		    	 $(this).parent().parent().next().empty().append($("<p>").text("$"+cost+".00"))
		     });
		});
});
