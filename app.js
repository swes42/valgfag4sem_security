const express = require('express');
const app = express();
const mongoose = require('mongoose');

// Hide MongoDB URI
require('dotenv/config');

// Import routes
const postsRoute = require('./routes/posts');

// So called 'Middlewares'
app.use('/posts', postsRoute);

app.use(express.urlencoded({
    extended: true
}));
app.use(express.json());

// Routes a.k.a. REST Resource paths in Java
app.get('/', (req, res) => {
    res.send('We are on home')
});

// Connect to DB and call .env file with DB_CONNECTION config
mongoose.connect(
    process.env.DB_CONNECTION,
    { 
        useNewUrlParser: true,
        useUnifiedTopology: true,
    }, 
    (err) => {
        if (err) console.error(err);
        console.log('Connected to DB!');
});


// Start listening on the server with port '3000'
app.listen(3000, function(){
    console.log("info", 'Server is running at port : ' + 3000);
});