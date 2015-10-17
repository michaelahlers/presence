'use strict';

var glob = require('glob')
  , path = require('path')
  , webpack = require('webpack');

var CommonsChunkPlugin = webpack.optimize.CommonsChunkPlugin
  , HtmlWebpackPlugin = require('html-webpack-plugin');

var modulesPath = path.join(__dirname, 'node_modules')
  , reactPath = path.join(modulesPath, 'react', 'react.js');

/* See https://github.com/christianalfoni/react-webpack-cookbook/issues/35 for details. */
var reactDOMPath = path.join(modulesPath, 'react-dom', 'dist', 'react-dom.js');

var sourcePath = path.join(__dirname, 'src')
  , targetPath = path.join(__dirname, 'dist');

module.exports = {

  entry: {
    index: path.resolve(sourcePath, 'index.js'),
    vendors: ['react', 'react-dom']
  },

  resolve: {
    alias: {
      'react': reactPath,
      'react-dom': reactDOMPath
    }
  },

  output: {
    path: targetPath,
    filename: '[name]-[hash].min.js'
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
        test: /\.less$/,
        loader: 'style!css!less?strictMath' //&noIeCompat'
      }
    ]

  },

  plugins: [
    new CommonsChunkPlugin('vendors', 'vendors-[hash].js')
    , new HtmlWebpackPlugin({title: 'Michael Ahlers', minify: true})
  ]

};
