'use strict;'
var gulp = require('gulp');
var sass = require('gulp-sass');
var concat = require('gulp-concat');
var clean = require('gulp-clean');
var connect = require('gulp-connect');
var path = require('path');

gulp.task('sass', function(){
    console.log('In the css task',path.join(__dirname,'/dist/css'));
    return gulp.src(path.join(__dirname,'/scss/**/*.scss'))
        .pipe(sass().on('error',sass.logError))
        .pipe(concat('app.css'))
        .pipe(gulp.dest(path.join(__dirname,'/dist/css')));
});

gulp.task('cleanAppFiles',function(){
    console.log('In the cleanAppFiles task',path.join(__dirname,'/dist'));
    return gulp.src([path.join(__dirname,'/dist/css/*'),path.join(__dirname,'/dist/js/*')],{read: false}).pipe(clean());
});

gulp.task('testGulp', function() {
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
gulp.task('combineAppJs',function(){
    console.log('In the js task',path.join(__dirname,'/dist/js'));
    return gulp.src([path.join(__dirname,'/js/**/*.js'),path.join(__dirname,'/components/**/*.js')])
        .pipe(concat('app.js'))
        .pipe(gulp.dest(path.join(__dirname,'/dist/js')));
});
gulp.task('connect',function(){
    connect.server({
        port : 8090,
        livereload: true
    });
});
gulp.task('watch',function(){
    gulp.watch(path.join(__dirname,'/scss/**/*.scss'),['sass']);
    gulp.watch([path.join(__dirname,'/js/**/*.js'),path.join(__dirname,'/components/**/*.js')],['cleanJsFiles','combineAppJs']);
});
gulp.task('default',['testGulp','cleanAppFiles','sass','combineAppJs']);
gulp.task('local',['testGulp','cleanAppFiles','sass','combineAppJs','connect','watch']);