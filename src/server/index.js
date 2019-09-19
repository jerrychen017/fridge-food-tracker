const express = require('express');
const app = express()
const port = 3000

app.use(express.urlencoded({extended: true})); 
app.use(express.json())

var sqlite3 = require('sqlite3').verbose();
var db = new sqlite3.Database(':memory:');//new sqlite3.Database('./data/test.db');

db.run('CREATE TABLE IF NOT EXISTS test(id text, desc text)');

app.post('/', function(req, res)
{
	var obj = req.body;

	console.log(obj);

	db.run(`INSERT INTO test(id, desc) VALUES(?, ?)`, [obj.ID, obj.DESC], function(err) {
    if (err) {
      return console.log(err.message);
    }
    // get the last insert id
    console.log(`A row has been inserted with rowid ${this.lastID}`);
  	});

	res.redirect('/');
})

app.get('/', function(req, res)
{
	let sql = `SELECT id identifier,
                  desc description
           FROM test
           ORDER BY identifier`;
 
	db.all(sql, [], (err, rows) => {
	  if (err) {
	    throw err;
	  }

	  var output = 
`<form action="/" method="post">
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

app.listen(port, function()
{
	console.log("Server started!");
})