module.exports = function(db)
{
	db.run('CREATE TABLE IF NOT EXISTS fridges(item text, fridgeID text)');

	var express = require('express')
	var router = express.Router()

	router.use(express.urlencoded({extended: true})); 
	router.use(express.json())

	router.get('/:id/', function(req, res){
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

	router.post('/', function(req, res)
	{
		var obj = req.body;

		console.log(obj);

		db.run(`INSERT INTO fridges(item, fridgeID) VALUES(?, ?)`, [obj.DESC, obj.ID], function(err) {
	    if (err) {
	      return console.log(err.message);
	    }
	    // get the last insert id
	    console.log(`A row has been inserted with rowid ${this.lastID}`);
	  	});

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
