'use strict';

//express config
var express = require('express');
var app = express();
var router = express.Router();
var request = require('request');
const appConfig = require('./appconfig');
const apiUrl = `${appConfig.api_server_host}:${appConfig.api_server_port}`;
app.listen(appConfig.application_port,function(){
  console.log('Started Express !!!',appConfig,apiUrl);
});
/*app.get('/',function(req,res){
  request({
    url: apiUrl
  },function(error,response,body){
    console.log('in body');
    console.log(body);
    res.send(body);
  })
});*/
app.use('/',express.static('./'));

app.get('/statement/details', function(req, res) {
  var fromTime = req.query.fromTime;
  var invoice= req.query.invoiceNumber;
  var toTime = req.query.toTime;
  var size = req.query.size;
  var page = req.query.page;
  request({
    url: `${apiUrl}/statement/details`,
    qs:{
      fromTime:fromTime,
      invoice:invoice,
      toTime:toTime,
      size:size,
      page:page
    }//params to be added
  },function(error,response,body){
    res.send(body);
  })
});

app.get('/statement/pdf/:id', function(req, res) {
  var invoiceId = req.params.id;
  console.log(invoiceId)
  /*request({
    url: `${apiUrl}/statement/pdf/${invoiceId}`
  },function(error,response,body){
    console.log('Invoice Details PDF ',response.headers,response.headers['content-type'],response.headers['content-disposition'],response.headers['content-length']);
    //res.send(body);
    res.writeHead(200,{
      'Content-Type': response.headers['content-type'],
      'Content-disposition': response.headers['content-disposition'],
      'Content-Length': response.headers['content-length']
    });
    res.end(new Buffer(body,'binary'));
  })*/
  // Used pipe since the return type is not json and the response is forwarded to the controller as is 
  request(`${apiUrl}/statement/pdf/${invoiceId}`).pipe(res);
});

app.get('/brand/config/brand', function(req, res) {
  request({
    url: `${apiUrl}/brand/config/brand`
  },function(error,response,body){
    console.log('Here in !!!!!!!!!!!',body);
    res.send(body);
  })
});

app.get('/dashboard/all', function(req, res) {
  var fromTime = req.query.fromTime;
  var toTime = req.query.toTime;
  request({
    url: `${apiUrl}/dashboard/all`,
    qs: {
      fromTime:fromTime,
      toTime:toTime
    }
  },function(error,response,body){
    console.log(response);
    res.send(body);
  })
});

app.get('/dashboard/status', function(req, res) {
  request({
    url: `${apiUrl}/dashboard/status`
  },function(error,response,body){
    console.log('Here in !!!!!!!!!!!',body);
    res.send(body);
  })
});

