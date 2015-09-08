'use strict';

var del = require('del')
  , gulp = require('gulp')
  , gutil = require('gulp-util')
  , path = require('path')
  , webpack = require('webpack');

var paths = {
  src: path.join(__dirname, 'src', 'main', 'client'),
  target: path.join(__dirname, 'src', 'main', 'public')
}

gulp.task('clean', function (done) {
  del([paths.target], done);
});

gulp.task('build', function (done) {
  var settings = {
    entry: path.join(paths.src, 'index.js'),
    //plugins: [
    //  new webpack.optimize.UglifyJsPlugin({
    //    compress: {
    //      warnings: false
    //    },
    //    output: {
    //      comments: false,
    //      semicolons: true
    //    }
    //  })
    //],
    output: {
      path: paths.target,
      filename: 'index.js'
    },
    module: {
      loaders: [
        {
          test: /\.js$/,
          //exclude: /node_modules/,
          loader: 'babel-loader',
          query: {
            /* Enable ES7 features. */
            stage: 0,
            cacheDirectory: true,
            /* Use babel-runtime dependency. */
            optional: 'runtime'
          }
        }, {
          test: /\.less$/,
          loader: 'style!css!less'//?strictMath&noIeCompat'
        }//,
        //{
        //  test: /\.html$/,
        //  loader: 'html-loader'
        //}
      ]
    }
  };

  webpack(settings, function (err, stats) {
    if (err) throw new gutil.PluginError('webpack', err);
    gutil.log('[webpack]', stats.toString({}));
    done();
  });
});

gulp.task('watch', function (done) {
  gulp.start('build');
  gulp.watch([
    path.join(paths.src, '**', '*js'),
    path.join(paths.src, '**', '*less')
  ], ['build']);
});
