const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {
		//true (hide sidebar)


		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	} else {
		//false (show sidebar)

		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
}

const search = () => {


	let query = $("#search-input").val();
	/*	console.log(query);*/

	if (query == '') {
		$(".search-result").hide();
	} else {
		// search
		console.log(query);

		//sending request to server
		let url = `http://localhost:8282/search/${query}`;
		fetch(url)
			.then((response) => {

				return response.json();
			})
			.then((data) => {
				//data
				// console.log(data);

				let text = `<div class='list-group'>`

				data.forEach((contact) => {
					text += `<a href='/user/${contact.cId}/contact' class='list-group-item list-group-item-action'> ${contact.name} </a>`
				});
				text += `</div>`;

				$(".search-result").html(text);
				$(".search-result").show();
			});
	}
}


