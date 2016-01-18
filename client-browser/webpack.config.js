'use strict';

var path = require('path')
  , webpack = require('webpack');

var CommonsChunkPlugin = webpack.optimize.CommonsChunkPlugin
  , HtmlWebpackPlugin = require('html-webpack-plugin');

var sourcePath = path.resolve(__dirname, 'src')
  , targetPath = path.resolve(__dirname, 'build');

module.exports = {

  entry: [
    'webpack/hot/dev-server',
    'webpack-dev-server/client?http://localhost:8080',
    path.resolve(sourcePath, 'index.js')
  ],

  output: {
    path: targetPath,
    filename: '[name].js',
    chunkFilename: '[id].chunk.js'
  },

  plugins: [
    new HtmlWebpackPlugin({title: 'Michael Ahlers'})
  ]

};
