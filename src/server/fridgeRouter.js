module.exports = function(db)
{
	db.run('CREATE TABLE IF NOT EXISTS fridges(item text, fridgeID text)');

	var express = require('express')
	var router = express.Router()
	var request = require('request');

	router.use(express.urlencoded({extended: true})); 
	router.use(express.json())

	router.get('/:id/', function(req, res){			//Get all items from a fridge
		let sql = `SELECT item thing, fridgeID id
		FROM fridges
		WHERE fridgeID = \'${req.params.id}\'
		ORDER BY thing`

		db.all(sql, [], (err, rows) => {
			if (err) { throw err; }

			resp = {}
			resp.items = []
			rows.forEach((row) => {
				resp.items.push(row.thing);
			})

			res.send(resp)
		})

		//res.send(req.params.id)
	})

	router.post('/:id', function(req, res) {	// Add an item to the fridge.
		if (!req.body || !req.body.item)
		{
			console.log("Missing body")
			// Missing req body
			res.status(400)
			res.send("Malformed request, need item to add")
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
		}
		else
		{
			db.run(`INSERT INTO fridges(item, fridgeID) VALUES(?,?)`, [req.body.item, req.params.id], function(err) {
				if (err){
					return console.log(err.message);
				}

				console.log(`A row has been inserted with rowid ${this.lastID}`)
			})
		}

		res.send("Good!")
	})

	router.post('/', function(req, res)
	{
		var obj = req.body;

		console.log(obj);

		request.post(`http://localhost:3000/fridge/${obj.ID}`, {json: {item: obj.DESC}}, function(err, res, body) {
			console.log(err)
			console.log(res)
			console.log(body)
		})

		res.redirect('/fridge');
	})

	router.get('/', function(req, res)
	{
		let sql = `SELECT fridgeID identifier,
	                  item description
	           FROM fridges
	           ORDER BY identifier`;
	 
		db.all(sql, [], (err, rows) => {
		  if (err) {
		    throw err;
		  }

		  var output = 
	`<form action="/fridge" method="post">
	ID:<br>
	<input type="text" name="ID"><br>
	Description:<br>
	<input type="text" name="DESC"><br>
	<input type="submit" value="Submit">
	</form>

	<table style="width:100%">
	  <tr>
	    <th>id</th>
	    <th>desc</th>
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

		console.log("Inbound connection");
	})

	return router;
}
