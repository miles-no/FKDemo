'use strict';

//express config
var express = require('express');
var app = express();
var router = express.Router();
var request = require('request');
var bodyParser = require('body-parser');
const appConfig = require('./appconfig');

const apiUrl = `${appConfig.api_server_host}:${appConfig.api_server_port}`;
app.use('/',express.static('./'));
app.use(bodyParser.json());
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
  console.log('/statement/details ::',body);
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
  console.log('/brand/config/brand ::',body);
  res.send(body);
})
});

app.get('/brand/config', function(req, res) {
  request({
    url: `${apiUrl}/brand/config`
},function(error,response,body){
  console.log('/brand/config',body);
  res.send(body);
})
});

app.get('/grid/config', function(req, res) {
  request({
    url: `${apiUrl}/grid/config`
},function(error,response,body){
  console.log('/grid/config',body);
  res.send(body);
})
});

app.get('/grid/config/brand', function(req, res) {
  request({
    url: `${apiUrl}/grid/config/brand`
},function(error,response,body){
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
  //console.log('Here in :: ',response.request.uri);
  console.log('Here in /dashboard/status :: ',body);
  res.send(body);
})
});
/******Brand Crud*****/
app.post('/brand/config',function(req,res,next){
  console.log('Here in POST /brand/config :: ',req.body);
  request({
    url :`${apiUrl}/brand/config`,
  method : 'POST',
    json : req.body
},function(error,response,body){
  res.send(body);
});
});
app.put('/brand/config',function(req,res,next){
  console.log('Here in put update /brand/config :: ',req.body);
  request({
    url :`${apiUrl}/brand/config`,
  method : 'PUT',
    json : req.body
},function(error,response,body){
  res.send(body);
})
});

app.delete('/brand/config/:id',function(req,res,next){
  console.log('Here in delete /brand/config',req.params.id);
  var delId = req.params.id;
  console.log(delId);
  request({
    url :`${apiUrl}/brand/config`,
    qs : {
      id : delId
    },
    method : 'DELETE'
},function(error,response,body){
  res.send(body);
})
});
/******Grid Crud*****/

app.post('/grid/config',function(req,res,next){
  console.log('Here in POST /grid/config : ',req.body);
  request({
    url :`${apiUrl}/grid/config`,
    method : 'POST',
    json : req.body
},function(error,response,body){
  console.log('Here in :: ',error,response.request.body);
  console.log('Here in /grid/config POST :: ',body);
  res.send(body);
})
});

app.put('/grid/config',function(req,res,next){
  console.log('Here in put update /grid/config :: ',req.body);
  request({
    url :`${apiUrl}/grid/config`,
    method : 'PUT',
    json : req.body
},function(error,response,body){
  res.send(body);
})
});

app.delete('/grid/config/:id',function(req,res,next){
  console.log('Here in delete /grid/config',req.params.id);
  var delId = req.params.id;
  console.log(delId);
  request({
    url :`${apiUrl}/grid/config`,
    qs : {
      id : delId
    },
    method : 'DELETE'
},function(error,response,body){
  res.send(body);
})
});

/******State Config Crud*****/

app.get('/config', function(req, res) {
  request({
    url: `${apiUrl}/config`
},function(error,response,body){
  res.send(body);
})
});

app.post('/config', function(req, res) {
  request({
    url: `${apiUrl}/config`,
    qs : {
      key : req.body.name,
      value: req.body.value
    },
    method : 'POST'
},function(error,response,body){
  console.log(response)
  res.send(body);
})
});

app.delete('/config/:id', function(req, res) {
  console.log(req.params)
  request({
    url: `${apiUrl}/config/`+req.params.id,
    method : 'DELETE'
},function(error,response,body){
  console.log(response)
  res.send(body);
})
});

app.put('/config/', function(req, res) {
  request({
    url: `${apiUrl}/config/`+req.body.name,
    qs:{
      value: req.body.value
    },
    method : 'PUT'
},function(error,response,body){
  console.log(response)
  res.send(body);
})
});

/******plug call*****/

app.post('/transferfile/process', function(req, res) {
  request({
    url: `${apiUrl}/transferfile/process`,
  method : 'POST'
},function(error,response,body){
  console.log(response)
  res.send(body);
})
});