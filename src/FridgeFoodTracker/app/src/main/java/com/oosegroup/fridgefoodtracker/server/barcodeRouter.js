module.exports = function(db, route)
{
	db.run('CREATE TABLE IF NOT EXISTS barcode(barcodeID text, item text)');

	var express = require('express')
	var router = express.Router()
	var request = require('request');

	router.use(express.urlencoded({extended: true})); 
	router.use(express.json())

	router.post('/', function(req, res)
	{
		obj = req.body

		if (!obj)
		{
			res.redirect(route);
			return
		}

		if (!obj.DESC)
		{
			console.log(`Deleting barcode ${obj.ID}!`)

			let sql = `DELETE FROM barcode WHERE barcodeID=(?)`

			console.log(`Deleting item ${req.body.ID}`)
			db.run(sql, [obj.ID], function(err)
			{
				if (err){
					console.log(err.message);
				}
			})
		}
		else if (obj.ID)
		{
			console.log(`Adding barcode ${obj.ID}`)
			
			db.run(`INSERT INTO barcode(barcodeID, item) VALUES(?,?)`, [obj.ID, obj.DESC], function(err) {
				if (err){
					console.log(err.message);
				}
			})
		}

		res.redirect(route);
	})

	router.get('/', function(req, res)
	{
		let sql = `SELECT barcodeID identifier,
	                  item description
	           FROM barcode
	           ORDER BY identifier`;
	 
		db.all(sql, [], (err, rows) => {
		  if (err) {
		    throw err;
		  }

		  var output = 
	`
	<p>Add Barcode</p>
	<form action="${route}/" method="post">
	Barcode ID:<br>
	<input type="text" name="ID"><br>
	Description:<br>
	<input type="text" name="DESC"><br>
	<input type="submit" value="Submit">
	</form>

	<p>Delete Barcode</p>
	<form action="${route}/" method="post">
	Barcode ID:<br>
	<input type="number" name="ID"><br>
	<input type="submit" value="Submit">
	</form>

	<table style="width:100%">
	  <tr>
	    <th>barcode id</th>
	    <th>item</th>
	  </tr>`

		  rows.forEach((row) => {
		  	output += '<tr>'
		  	output += '<th>'
		  	output += row.identifier
		  	output += '</th>'
		  	output += '<th>'
		  	output += row.description
		  	output += '</th>'
		    output += '</tr>'
		  });

		  output += `</table>`

		  res.send(output);
		});

		//console.log("Inbound connection");
	})

	return router;
}
