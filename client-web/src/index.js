'use strict';

require('./index.less');

import React from 'react';
import ReactDOM from 'react-dom';

import createBrowserHistory from 'history/lib/createBrowserHistory';
import { Router, Route, Link } from 'react-router';

import 'regenerator/runtime';

import Posts from './Posts';
import Post from './Post';

import Projects from './Projects';
import Project from './Project';

import NotFound from './NotFound';

export default class Root extends React.Component {
  render() {
    return (
      <div>
        <ul>
          <li><Link to={`/posts`}>Posts</Link></li>
          <li><Link to={`/projects`}>Projects</Link></li>
        </ul>

        {this.props.children}
      </div>
    );
  }
};

// @formatter:off
(async function () {
// @formatter:on
  const settings = await require('./settings');

  ReactDOM.render((
    <Router history={createBrowserHistory()}>
      <Route path="/" component={Root}>
        <Route path="posts" component={Posts}>
          <Route path=":postId" component={Post}/>
        </Route>
        <Route path="projects" component={Projects}>
          <Route path=":projectId" component={Project}/>
        </Route>
      </Route>
      <Route path="*" component={NotFound}/>
    </Router>
  ), document.body);

})();
