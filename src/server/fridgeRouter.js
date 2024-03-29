module.exports = function(db,route)
{
	db.run('CREATE TABLE IF NOT EXISTS fridges(item text, itemID integer primary key, fridgeID text)');

	var express = require('express')
	var router = express.Router()
	var request = require('request');

	router.use(express.urlencoded({extended: true})); 
	router.use(express.json())

	router.delete('', function(req, res)		// Delete an item
	{
		if(!req.body || !req.body.item)
		{
			console.log("Missing body")
			res.status(400)
			res.send("Malformed request, need item id to delete")
			return
		}

		let sql = `DELETE FROM fridges WHERE itemID=${req.body.item}`

		console.log(`Deleting item ${req.body.item}`)
		db.run(sql, function(err)
		{
			if(!err)
				res.send("Item deleted")
			else
			{
				res.status(400)
				res.send(err)
				console.log(err)
			}
		})
	})

	router.put('', function(req,res)		// Modify an item
	{
		if(!req.body)
		{
			console.log("Missing body")
			res.status(400)
			res.send("Malformed request, need request body")
			return
		}

		if (!req.body.id)
		{
			res.status(400)
			res.send("Malformed request, need item id in json to modify")
			return
		}

		if (!req.body.item)
		{
			res.status(400)
			res.send("Malformed request, need new item description to change item to")
			return
		}

		let sql = `UPDATE fridges SET item=(?) WHERE itemID=(?)`
		db.run(sql, [req.body.item, req.body.id], function(err)
		{
			if(!err)
				res.send("Item modified")
			else
			{
				res.status(400)
				res.send(err)
				console.log(err)
			}
		})
	})

	router.get('/:id/', function(req, res){			//Get all items from a fridge
		let sql = `SELECT item thing, fridgeID id, itemID itId
		FROM fridges
		WHERE fridgeID = \'${req.params.id}\'
		ORDER BY itID`

		db.all(sql, [], (err, rows) => {
			if (err) { throw err; }

			resp = {}
			resp.items = []
			rows.forEach((row) => {
				resp.items.push({id: row.itId, item: row.thing});
			})

			res.send(resp)
		})
	})

	router.post('/:id', function(req, res) {	// Add an item to the fridge.
		if (!req.body || !req.body.item)
		{
			console.log("Missing body")
			// Missing req body
			res.status(400)
			res.send("Malformed request, need item to add")

			return;
		}

		if (Array.isArray(req.body.item))
		{
			req.body.item.forEach((item) => {
				db.run(`INSERT INTO fridges(item, fridgeID) VALUES(?,?)`, [item, req.params.id], function(err) {
					if (err){
						return console.log(err.message);
					}

					console.log(`A row has been inserted with rowid ${this.lastID}`)
				})
			})
			res.status(201)
			res.send({id : -1})
		}
		else
		{
			db.run(`INSERT INTO fridges(item, fridgeID) VALUES(?,?)`, [req.body.item, req.params.id], function(err) {
				if (err){
					return console.log(err.message);
				}

				console.log(`A row has been inserted with rowid ${this.lastID}`)
				res.status(201)
				res.send({id:this.lastID})
			})
		}
	})

	router.post('/', function(req, res)
	{
		var obj = req.body;

		if (!obj)
		{
			res.redirect(route);
			return
		}

		if (!obj.DESC)
		{
			console.log("Test Delete!")

			request.delete(`http://localhost:3000` + route + `/`, {json: {item: obj.ID}}, function(err, res, body) {
			})
		}
		else if (obj.IID)
		{
			console.log("Test Modify!")
			request.put(`http://localhost:3000` + route + `/`, {json: {id: obj.IID, item: obj.DESC}}, function(err, res, body) {
			})
		}
		else
		{
			console.log("Test Add!")

			request.post(`http://localhost:3000` + route + `/${obj.ID}`, {json: {item: obj.DESC}}, function(err, res, body) {
			})
		}
		

		res.redirect(route);
	})

	router.get('/', function(req, res)
	{
		let sql = `SELECT fridgeID identifier,
					  itemID itId,
	                  item description
	           FROM fridges
	           ORDER BY identifier`;
	 
		db.all(sql, [], (err, rows) => {
		  if (err) {
		    throw err;
		  }

		  var output = 
	`
	<p>Add Item</p>
	<form action="${route}/" method="post">
	Fridge ID:<br>
	<input type="text" name="ID"><br>
	Description:<br>
	<input type="text" name="DESC"><br>
	<input type="submit" value="Submit">
	</form>

	<p>Modify Item</p>
	<form action="${route}/" method="post">
	Item ID:<br>
	<input type="text" name="IID"><br>
	Description:<br>
	<input type="text" name="DESC"><br>
	<input type="submit" value="Submit">
	</form>

	<p>Delete Item</p>
	<form action="${route}/" method="post">
	Item ID:<br>
	<input type="number" name="ID"><br>
	<input type="submit" value="Submit">
	</form>

	<table style="width:100%">
	  <tr>
	    <th>fridge id</th>
	    <th>item id</th>
	    <th>item desc</th>
	  </tr>`

		  rows.forEach((row) => {
		  	output += '<tr>'
		  	output += '<th>'
		  	output += row.identifier
		  	output += '</th>'
		  	output += '<th>'
		  	output += row.itId
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
