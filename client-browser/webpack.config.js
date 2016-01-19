'use strict';

var path = require('path')
  , webpack = require('webpack');

var CommonsChunkPlugin = webpack.optimize.CommonsChunkPlugin
  , HotModuleReplacementPlugin = webpack.HotModuleReplacementPlugin
  , HtmlWebpackPlugin = require('html-webpack-plugin');

var modulesPath = path.join(__dirname, 'node_modules');

/* See https://github.com/christianalfoni/react-webpack-cookbook/issues/35 for details. */
var reactPath = path.join(modulesPath, 'react', 'lib', 'React.js')
  , reactDOMPath = path.join(modulesPath, 'react', 'lib', 'ReactDOM.js')
  , reactCSSTransitionGroup = path.join(modulesPath, 'react', 'lib', 'ReactCSSTransitionGroup.js');

var sourcePath = path.resolve(__dirname, 'src')
  , targetPath = path.resolve(__dirname, 'build');

module.exports = {

  devtool: 'source-map',

  resolve: {
    alias: {
      'react': reactPath,
      'react-dom': reactDOMPath,
      'react-addons-css-transition-group': reactCSSTransitionGroup
    }
  },

  entry: {
    index: [
      'webpack/hot/dev-server',
      'webpack-dev-server/client?http://localhost:8080',
      'babel-polyfill',
      path.resolve(sourcePath, 'index.js')
    ],
    vendors: [
      'react',
      'react-dom',
      'react-addons-css-transition-group'
    ]
  },

  output: {
    path: targetPath,
    filename: '[name].js',
    chunkFilename: '[id].chunk.js'
  },

  module: {

    preLoaders: [
      {
        loader: 'eslint',
        exclude: /node_modules/,
        test: /\.js$/
      }
    ],

    loaders: [
      {
        include: sourcePath,
        test: /\.js$/,
        noParse: [reactPath],
        loader: 'babel',
        query: {
          plugins: ['transform-runtime'],
          presets: ['es2015', 'stage-0', 'react']
        }

      }
    ]

  },

  devServer: {
    contentBase: targetPath
  },

  plugins: [
    new CommonsChunkPlugin('vendors', 'vendors-[hash].js'),
    new HotModuleReplacementPlugin(),
    new HtmlWebpackPlugin({title: 'Michael Ahlers'})
  ]

};
