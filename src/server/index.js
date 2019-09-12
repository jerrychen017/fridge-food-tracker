const express = require('express');
const app = express()
const port = 3000


app.get('/', function(req, res)
{
	console.log("Inbound connection");
	res.send("Hello world!");
})

app.listen(port, function()
{
	console.log("Server started!");
})
