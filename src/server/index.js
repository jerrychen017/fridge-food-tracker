const express = require('express');
const app = express()
const port = 3000

app.use(express.urlencoded({extended: true})); 
app.use(express.json())

var sqlite3 = require('sqlite3').verbose();
var db = new sqlite3.Database(':memory:');//new sqlite3.Database('./data/test.db');

var fridgeRoute = require('./fridgeRouter')(db)
app.use('/fridge', fridgeRoute)

app.get('/', function(req, res)
{
	res.send("Hi there! You aren't supposed to be loading me!")
})

app.listen(port, function()
{
	console.log("Server started!");
})