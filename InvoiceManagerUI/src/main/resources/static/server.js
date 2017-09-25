'use strict';

//express config
var express = require('express');
var app = express();
var router = express.Router();
var request = require('request');
var bodyParser = require('body-parser');
var multer = require('multer');
var upload = multer();
var FormData = require('form-data');

const appConfig = require('./appconfig');
var multer  = require('multer')
var upload = multer({dest :appConfig.tempFilePath});
var fs = require('fs');

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

app.put('/retry/statement',function(req,res){
  console.log(req.query.statementId)
  var qp = {
    statementId : req.query.statementId
  }
  request({
    url: `${apiUrl}/statement/details`,
    qs: qp,
    method : 'PUT'
  }),function(error,response,body){
      res.send(body);
  }
})


app.get('/statement/details', function(req, res) {
  var fromTime = req.query.fromTime;
  var invoice= req.query.invoiceNumber;
  var toTime = req.query.toTime;
  var size = req.query.size;
  var page = req.query.page;
  var customerID = req.query.customerID
  var accountNumber = req.query.accountNumber
  var states = req.query.states
  var qp = {
    fromTime:fromTime,
    invoice:invoice,
    toTime:toTime,
    size:size,
    page:page,
    customerID:customerID,
    accountNumber:accountNumber,
    states : states
  }

  console.log(qp)
  request({
    url: `${apiUrl}/statement/details`,
  qs: qp
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


app.get('/layout/statement/xml/:id', function(req, res) {
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
  request(`${apiUrl}/layout/statement/xml/${invoiceId}`).pipe(res);
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
  res.set('X-FK-Functions','Afi_Standard,Afi_Standard_vip,Afi_Meldingsutveksling,Afi_EndreKrav,Afi_Bunting,Afi_Kreditere,Afi_Rabatt,Afi_forsendelsesmaate,Admin_Brukeradministrasjon,Admin_Konfigurasjon,Admin_Jobber,Admin_Epostoppsett,Admin_ZipBackup').send(body);
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
    url :`${apiUrl}/brand/config/`+delId,
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
  var uri = `${apiUrl}/grid/config/`+delId
  console.log(uri)
  request({
    url : uri,
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
  console.log('In POST /config '+ JSON.stringify(req.query));
  request({
    url: `${apiUrl}/config`,
    qs : {
      key : req.query.key,
      value: req.query.value
    },
    method : 'POST'
},function(error,response,body){
  console.log(response.request)
  res.send(body);
})
});

app.delete('/config/:id', function(req, res) {
  console.log(req.params)
  request({
    url: `${apiUrl}/config/`+req.params.id,
    method : 'DELETE'
},function(error,response,body){
  console.log(body)
  res.send(body);
})
});

app.put('/config/:name', function(req, res) {
  request({
    url: `${apiUrl}/config/`+req.params.name,
    qs:{
      value: req.query.value
    },
    method : 'PUT'
},function(error,response,body){
  console.log(response)
  res.send(body);
})
});

/******rule page*****/
app.get('/layout/attribute', function(req, res) {
  request({
    url: `${apiUrl}/layout/attribute`
},function(error,response,body){
  res.send(body);
})
});

app.delete('/layout/attribute/:id',function(req,res,next){
  request({
    url :`${apiUrl}/layout/attribute/`+req.params.id,
  method : 'DELETE'
},function(error,response,body){
  res.send(body);
})
});

app.post('/layout/attribute', function(req, res) {
  request({
    url: `${apiUrl}/layout/attribute`,
    method : 'POST',
    json : req.body,
},function(error,response,body){
  console.log(response)
  res.send(body);
})
});

app.put('/layout/attribute/:id', function(req, res) {
  request({
    url: `${apiUrl}/layout/attribute/`+req.params.id,
    json : req.body,
    method : 'PUT'
},function(error,response,body){
  console.log(response)
  res.send(body);
})
});


/******templates*****/

app.get('/layout/list', function(req, res) {
  request({
    url: `${apiUrl}/layout/list`
},function(error,response,body){
  res.send(body);
})
});
app.get('/layout/template/all', function(req, res) {
  request({
    url: `${apiUrl}/layout/template/all`
},function(error,response,body){
  console.log(' /layout/template/all Resp is ', body)
  res.send(body);
})
});

app.get('/layout/rules', function(req, res) {
  request({
    url: `${apiUrl}/layout/rules`
},function(error,response,body){
  res.send(body);
})
});

app.get('/layout/list', function(req, res) {
  request({
    url: `${apiUrl}/layout/list`
},function(error,response,body){
  res.send(body);
})
});
app.post('/layout/rule', function(req, res) {
  request({
    url: `${apiUrl}/layout/rule`,
    method : 'POST',
    json : req.body,
},function(error,response,body){
  console.log(response)
  res.send(body);
})
});

app.put('/layout/rule/:id', function(req, res) {
  request({
    url: `${apiUrl}/layout/rule/`+req.params.id,
    json : req.body,
    method : 'PUT'
},function(error,response,body){
  console.log(response)
  res.send(body);
})
});

app.put('/layout/activate/:id/:id1',function(req,res){
  var u1 = `${apiUrl}/layout/activate/`+req.params.id+`/`+req.params.id1
  console.log(u1);
  request({
    url: u1,
    method : 'PUT'
  },function(error,response,body){
  res.send(body)
})
})

app.put('/layout/deActivate/:id/:id1',function(req,res){
  var u1 = `${apiUrl}/layout/deActivate/`+req.params.id+`/`+req.params.id1
  console.log(u1);
  request({
    url: u1,
    method : 'PUT'
  },function(error,response,body){
    res.send(body)
  })
})

app.delete('/layout/template/:id',function(req,res){
  request({
    url :`${apiUrl}/layout/template/`+req.params.id,
    method : 'DELETE'
},function(error,response,body){
  res.send(body);
})
});


app.post('/layout/template',upload.single('file'), function(req, res) {
  console.log('In POST /layout/template ', req.body);
  console.log('In POST /layout/template ', JSON.stringify(req.file));
  let formData = {
    name : req.body.name,
    description : req.body.description,
    file :  fs.createReadStream(`${req.file.destination}${req.file.filename}`)
  }
  request({
    url: `${apiUrl}/layout/template`,
    method : 'POST',
    formData : formData,
},function(error,response,body){
  console.log('POST /layout/template',JSON.stringify(response.request.headers))
  //fs.rm(`${req.file.destination}${req.file.filename}`);
  res.send(body);
})
});

app.put('/layout/template/:id',upload.single('file'),function(req,res) {
  var formData = {}
  if(req.file){
    var formData = {
      name : req.body.name,
      description : req.body.description,
      file :  fs.createReadStream(`${req.file.destination}${req.file.filename}`)
  }
  } else {
  var formData = {
    name : req.body.name,
    description : req.body.description
    }
  }
  console.log(formData)
  request({
    url: `${apiUrl}/layout/template`+req.params.id,
    method : 'PUT',
    formData : formData
  },function(error,response,body){
  //fs.rm(`${req.file.destination}${req.file.filename}`);
    res.send(body);
  })
})
app.get('/layout/preview', function(req, res) {
  console.log('in layout/preview',req.query);
  let qp ={
    layoutId: req.query.layoutId,
    version : req.query.version
  };

  request({url : `${apiUrl}/layout/preview`,qs: qp}).pipe(res);
});

app.get('/layout/rptdesign', function(req, res) {
  var id = req.query.id
  request(`${apiUrl}/layout/rptdesign?id=${id}`).pipe(res);
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

/**********audit logs*****/

app.get('/auditRecord',function(req,res){
  console.log(req.query)
  var invoiceNo = req.query.invoiceNo
  var fromTime = req.query.fromTime
  var toTime = req.query.toTime
  var page = req.query.page
  var size = req.query.size
  var customerID = req.query.customerID
  var accountNumber = req.query.accountNumber
  let qp = {
    fromTime:fromTime,
    invoiceNo:invoiceNo,
    toTime:toTime,
    page: page,
    size: size,
    customerID:customerID,
    accountNumber:accountNumber
  }
  console.log(qp)
  request({url : `${apiUrl}/auditRecord`,qs: qp}).pipe(res);
})
