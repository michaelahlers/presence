'use strict';

var glob = require('glob')
  , path = require('path')
  , webpack = require('webpack');

var modulesPath = path.join(__dirname, 'node_modules')
  , reactPath = path.join(modulesPath, 'react', 'react.js');

var sourcePath = path.join(__dirname, 'src', 'main')
  , targetPath = path.join(__dirname, 'build');

module.exports = {

  entry: {
    index: [
      'webpack/hot/dev-server'
      , path.resolve(sourcePath, 'index.html')
      , path.resolve(sourcePath, 'index.js')
    ],
    vendors: ['react']
  },

  resolve: {
    alias: {
      'react': reactPath
    }
  },

  output: {
    path: targetPath,
    filename: '[name].js'
  },

  module: {

    preLoaders: [
      {
        test: /\.js$/,
        loader: 'eslint',
        exclude: /node_modules/
      }
    ],

    loaders: [
      {
        test: /\.js$/,
        exclude: modulesPath,
        loader: 'babel',
        query: {
          /* Enable ES7 features. */
          stage: 0,
          cacheDirectory: true
        },
        noParse: ['react']
      }, {
        test: /\.html$/,
        /* TODO: Switch to URL loader. */
        loader: 'file',
        query: {
          context: sourcePath,
          name: '[path][name].[ext]'
        }
      }, {
        test: /\.less$/,
        loader: 'style!css!less?strictMath' //&noIeCompat'
      }
    ]

  },

  plugins: [
    new webpack.optimize.CommonsChunkPlugin('vendors', 'vendors.js')
  ]

};
