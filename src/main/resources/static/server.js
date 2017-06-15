'use strict';

//express config
var express = require('express');
var bodyParser = require('body-parser');
var app = express();
var router = express.Router();
var request = require('request');
app.use(express.static('static'));
app.use(bodyParser.json());
app.listen(9090,function(){
  console.log('Started Express !!!')
});
app.use('/',express.static('./'));
app.get('/dashboard/status', function(req, res) {
  console.log(req.headers);
  request({
    url: 'http://localhost:8090/dashboard/status'
  },function(error,response,body){
    console.log('Here in !!!!!!!!!!!',error);
    
    console.log('Here in !!!!!!!!!!!',body);
    res.send(body);
  })
});

//app.get('/statement/details', function(req, res) {
// request.get('localhost:8090/statement/details')
//});

//app.get('/statement/pdf/', function(req, res) {
//  request.get('localhost:8090/dashboard/all',)
//  var
//});
//app.get('/brand/config/brand', function(req, res) {
//  request.get('localhost:8090/dashboard/all',)
//});
//app.get('/dashboard/all', function(req, res) {
//  request.get('localhost:8090/dashboard/all',)
//});
//app.get('/dashboard/status', function(req, res) {
//  request.get('localhost:8090/dashboard/all',)
//});
