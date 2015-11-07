'use strict';

var glob = require('glob')
  , path = require('path')
  , webpack = require('webpack');

var CommonsChunkPlugin = webpack.optimize.CommonsChunkPlugin
  , HtmlWebpackPlugin = require('html-webpack-plugin');

var modulesPath = path.join(__dirname, 'node_modules');

/* See https://github.com/christianalfoni/react-webpack-cookbook/issues/35 for details. */
var reactPath = path.join(modulesPath, 'react', 'react.js')
  , reactDOMPath = path.join(modulesPath, 'react', 'lib', 'ReactDOM.js')
  , reactCSSTransitionGroup = path.join(modulesPath, 'react', 'lib', 'ReactCSSTransitionGroup.js');

var sourcePath = path.resolve(__dirname, 'src')
  , targetPath = path.resolve(__dirname, 'dist');

module.exports = {

  entry: {
    index: path.resolve(sourcePath, 'index.js'),
    vendors: [
      'react'
      , 'react-dom'
      , 'react-addons-css-transition-group'
    ]
  },

  resolve: {
    alias: {
      'react': reactPath,
      'react-dom': reactDOMPath,
      'react-addons-css-transition-group': reactCSSTransitionGroup
    }
  },

  output: {
    path: targetPath,
    filename: '[name]-[hash].min.js',
    chunkFilename: '[id]-[hash].chunk.min.js'
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
          presets: [
            'es2015'
            , 'stage-2'
            , 'react'
          ],
          cacheDirectory: true
        },
        noParse: ['react']
      }, {
        test: /\.less$/,
        loader: 'style!css!less?strictMath' //&noIeCompat'
      }
    ]

  },

  /* See https://github.com/ampedandwired/html-webpack-plugin/issues/97 for bug requiring that minify be set to false. */
  plugins: [
    new CommonsChunkPlugin('vendors', 'vendors-[hash].js')
    , new HtmlWebpackPlugin({title: 'Michael Ahlers'/*, minify: true*/})
  ]

};
