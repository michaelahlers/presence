'use strict';

var glob = require('glob')
  , path = require('path')
  , webpack = require('webpack');

var CommonsChunkPlugin = webpack.optimize.CommonsChunkPlugin
  , HtmlWebpackPlugin = require('html-webpack-plugin');

var modulesPath = path.join(__dirname, 'node_modules')
  , reactPath = path.join(modulesPath, 'react', 'react.js');

var sourcePath = path.join(__dirname, 'src')
  , targetPath = path.join(__dirname, 'build');

module.exports = {

  entry: {
    index: [
      'webpack/hot/dev-server'
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
          stage: 2,
          experimental: true,
          cacheDirectory: true
        },
        noParse: ['react']
      }, {
        test: /\.less$/,
        loader: 'style!css!less?strictMath' //&noIeCompat'
      }
    ]

  },

  plugins: [
    new CommonsChunkPlugin('vendors', 'vendors.js')
    , new HtmlWebpackPlugin({title: 'Michael Ahlers'})
  ]

};
