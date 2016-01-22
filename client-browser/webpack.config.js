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

var profiles = {
  development: {
    entry: {
      index: [
        'webpack/hot/dev-server',
        'webpack-dev-server/client?http://localhost:8080',
        'babel-polyfill',
        path.resolve(__dirname, 'src', 'index.js')
      ]
    },
    output: {
      path: path.resolve(__dirname, 'build'),
      filename: '[name].js',
      chunkFilename: '[id].chunk.js',
      publicPath: '/'
    },
    plugins: [
      new CommonsChunkPlugin('vendors', 'vendors.js'),
      new HotModuleReplacementPlugin(),
      new HtmlWebpackPlugin({title: 'Michael Ahlers'})
    ]

  },
  production: {
    entry: {
      index: [
        'babel-polyfill',
        path.resolve(__dirname, 'src', 'index.js')
      ]
    },
    output: {
      path: path.resolve(__dirname, 'dist'),
      filename: '[name]-[hash].js',
      chunkFilename: '[id]-[hash].chunk.js',
      publicPath: '/'
    },
    plugins: [
      new CommonsChunkPlugin('vendors', 'vendors-[hash].js'),
      new HtmlWebpackPlugin({title: 'Michael Ahlers'})
    ]
  }
};

var profile = profiles[require('minimist')(process.argv.slice(2)).profile || 'development'];

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
    index: profile.entry.index,
    vendors: [
      'react',
      'react-dom',
      'react-addons-css-transition-group'
    ]
  },

  output: profile.output,

  module: {

    preLoaders: [
      {
        loader: 'eslint',
        exclude: [modulesPath],
        test: /\.js$/
      }
    ],

    loaders: [
      {
        loader: 'babel',
        exclude: [modulesPath],
        test: /\.js$/,
        noParse: [reactPath],
        query: {
          plugins: ['transform-runtime', 'transform-decorators-legacy'],
          presets: ['es2015', 'stage-0', 'react']
        }

      }
    ]

  },

  devServer: {
    contentBase: path.resolve(__dirname, 'build'),
    /* See https://github.com/webpack/webpack-dev-server/issues/87 for details. */
    // hot: true,
    quiet: false,
    noInfo: false,
    stats: {colors: true},
    /* See https://github.com/rackt/react-router/issues/676#issuecomment-143834149 for source. */
    historyApiFallback: true
  },

  plugins: profile.plugins

};
