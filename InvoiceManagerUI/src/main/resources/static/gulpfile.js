'use strict;'
var gulp = require('gulp');
var sass = require('gulp-sass');
var concat = require('gulp-concat');
var del = require('del');
var clean = require('gulp-clean');
var connect = require('gulp-connect');
var path = require('path');
var nodemon = require('nodemon');
var browserify = require('browserify');
var babelify = require('babelify');
var vinylSourceStream = require('vinyl-source-stream');
var vinylBuffer = require('vinyl-buffer');
var gulpSequence = require('gulp-sequence')


 
gulp.task('sass', ['combileVendorCSS'],function(){
    console.log('In the css task',path.join(__dirname,'/dist/css'));
    return gulp.src(path.join(__dirname,'/scss/**/*.scss'))
        .pipe(sass().on('error',sass.logError))
        .pipe(concat('app-v1.css'))
        .pipe(gulp.dest(path.join(__dirname,'/dist/css')));
});

gulp.task('cleanAppFiles',function(){
    console.log('In the cleanAppFiles task',path.join(__dirname,'/dist'));
    return gulp.src([path.join(__dirname,'/dist/*'),path.join(__dirname,'/dist/js/*'),path.join(__dirname,'/dist/font-awesome/*')],{read: false}).pipe(clean());
});
gulp.task('clean', function(){
  'use strict';
  del(['dist','tempupload']);
  //return gulp.src(path.join(__dirname,'/dist/*'),{read: false}).pipe(clean());
});

gulp.task('testGulp' ,function() {
    if(process.env.NODE_ENV === 'production') {
        console.log('gulp runs as expected on production');
    } else {
        console.log('gulp runs as expected');
    }
});
gulp.task('cleanJsFiles',function(){
    console.log('In the cleanJsFiles task',path.join(__dirname,'/dist'));
    return gulp.src(path.join(__dirname,'/dist/js/*'),{read: false}).pipe(clean());
});
gulp.task('combineAppJs',['combileVendorJS'],function(){
    console.log('In the js task',path.join(__dirname,'/dist/js'));
    return gulp.src([
            path.join(__dirname,'/js/**/*.js'),
            path.join(__dirname,'/components/**/*.js'),
            path.join(__dirname,'/directives/**/*.js'),
            path.join(__dirname,'/modules/**/*.js'),
            ])
        .pipe(concat('app.js'))
        .pipe(gulp.dest(path.join(__dirname,'/dist/js')));
});
gulp.task('connect',function(){
    connect.server({
        port : 8091,
        livereload: true
    });
});
gulp.task('watch',function(){
    gulp.watch(path.join(__dirname,'/scss/**/*.scss'),['sass']);
    gulp.watch([path.join(__dirname,'/js/**/*.js'),path.join(__dirname,'/components/**/*.js')],['browserify']);
});
gulp.task('runExpress',['browserify'],function(cb){
    var started = false;
    return nodemon({
        script: 'server.js'
    }).once('start',cb)
});
gulp.task('browserify',['combineAppJs'],function(){
    
    console.log('Came here in browserify');
    var sources = browserify({
        entries: path.join(__dirname,'/js/invoiceManager.js'),
        debug: false
    }).transform(babelify.configure({
        presets: ["es2015"]
    }));
    return sources.bundle()
		.pipe(vinylSourceStream('app-v1.min.js'))
		.pipe(vinylBuffer())
		.pipe(gulp.dest(path.join(__dirname,'/dist/js')))
})
gulp.task('moveFontAwesome',['sass'],function(){
    return gulp.src(path.join(__dirname,'/node_modules/font-awesome/**'))
            .pipe(gulp.dest(path.join(__dirname,'/dist/')));
});
gulp.task('bootstrapFonts',['moveFontAwesome'],function(){
    return gulp.src(path.join(__dirname,'./node_modules/bootstrap/fonts/*.*'))
      .pipe(gulp.dest(path.join(__dirname,'/dist/fonts')));
})
gulp.task('combileVendorJS',['bootstrapFonts'],function(){
    var libFiles = [
        './node_modules/lodash/lodash.min.js',
        './node_modules/jquery/dist/jquery.min.js',
        './node_modules/bootstrap/dist/js/bootstrap.min.js',
        './node_modules/angular/angular.js',
        './node_modules/angular-sanitize/angular-sanitize.min.js',
        './node_modules/@uirouter/angularjs/release/angular-ui-router.min.js',
        './node_modules/angular-aria/angular-aria.min.js',
        './node_modules/angular-animate/angular-animate.min.js',
        './node_modules/angular-messages/angular-messages.min.js',
        './node_modules/angular-material/angular-material.min.js',
        './node_modules/angular-ui-bootstrap/dist/ui-bootstrap-tpls.js',
        './node_modules/chart.js/dist/Chart.min.js',
        './node_modules/angular-chart.js/dist/angular-chart.min.js',
        './node_modules/angular-odometer-js/dist/angular-odometer.min.js',
        './node_modules/odometer/odometer.min.js',
        './node_modules/ui-select/dist/select.min.js',
        './node_modules/moment/min/moment.min.js',
        './node_modules/angular-moment/angular-moment.min.js',
        './node_modules/angular-smart-table/dist/smart-table.min.js',
        './node_modules/angular-circular-timepicker/dist/javascript/angular.circular.timepicker.js',
        './node_modules/angular-modal-service/dst/angular-modal-service.min.js',
        './node_modules/ng-flat-datepicker/dist/ng-flat-datepicker.min.js',
        './node_modules/clipboard/dist/clipboard.min.js',
        './node_modules/ngclipboard/dist/ngclipboard.min.js'
    ]
    return gulp.src(libFiles)
            .pipe(concat('lib.js'))
            .pipe(gulp.dest(path.join(__dirname,'/dist/js')));

})
gulp.task('combileVendorCSS',['clean'],function(){
    var libCSSFiles = [
        './node_modules/bootstrap/dist/css/bootstrap.min.css',
        './node_modules/angular-ui-bootstrap/dist/ui-bootstrap-csp.css',
        './node_modules/odometer/themes/odometer-theme-car.css' ,
        './node_modules/odometer/themes/odometer-theme-digital.css' ,
        './node_modules/odometer/themes/odometer-theme-plaza.css' ,
        './node_modules/odometer/themes/odometer-theme-slot-machine.css' ,
        './node_modules/odometer/themes/odometer-theme-train-station.css' ,
        './node_modules/angular-material/angular-material.min.css' ,
        './node_modules/font-awesome/css/font-awesome.min.css' ,
        './node_modules/font-awesome-animation/dist/font-awesome-animation.min.css' ,
        './node_modules/ui-select/dist/select.min.css',
        './node_modules/angular-circular-timepicker/dist/stylesheets/angular.circular.timepicker.css',
        './node_modules/ng-flat-datepicker/dist/ng-flat-datepicker.min.css'
    ]
    return gulp.src(libCSSFiles)
            .pipe(concat('lib.css'))
            .pipe(gulp.dest(path.join(__dirname,'/dist/css')));

})
//gulp.task('moveComponents',[],() => {
//    var testFiles = [
//         './node_modules/app-common/modules/authorization/authorization.component.js',
//         './node_modules/app-common/modules/authorization/authorization.service.js',
//         './node_modules/app-common/modules/authorization/authorization.provider.js'
//    ]
//    return gulp.src(testFiles)
//      .pipe(gulp.dest(path.join(__dirname,'./node_modules/app-common/node_modules/')))
//})
gulp.task('default',['testGulp','browserify']);
gulp.task('local',['testGulp','runExpress','watch']);