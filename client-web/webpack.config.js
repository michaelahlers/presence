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
  , targetPath = path.resolve(__dirname, 'build');

module.exports = {

  entry: {
    index: [
      'webpack/hot/dev-server'
      , path.resolve(sourcePath, 'index.js')
    ],
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
    filename: '[name].js',
    chunkFilename: '[id].chunk.js'
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
  ],

  devServer: {
    /* See https://github.com/rackt/react-router/issues/676#issuecomment-143834149 for source. */
    historyApiFallback: true
  }

};
