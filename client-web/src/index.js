'use strict';

require('./index.less');

import 'regenerator/runtime';

import classNames from 'classnames';

import React from 'react';
import ReactDOM from 'react-dom';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';

import { createHistory, createHashHistory, useBasename } from 'history';
import { Router, Route, Link } from 'react-router';

class Root extends React.Component {
  render() {
    return (
      <div>
        <h1>Presence</h1>

        <ul>
          <li><Link to={`/projects`}>Projects</Link></li>
        </ul>

        {this.props.children}
      </div>
    );
  }
}

// @formatter:off
(async function () {
// @formatter:on
  const settings = await require('./settings');

  /* In light of http://stackoverflow.com/questions/16267339/s3-static-website-hosting-route-all-paths-to-index-html forgo using HTML5 browser history until a good solution is found to deploy to S3. See also http://rackt.org/history/stable/HashHistoryCaveats.html for details on (and caveats especially of) hash history. */
  // const history = useBasename(createHistory)({});
  const history = createHashHistory({queryKey: false});

  const routes = {
    path: '/',
    component: Root,
    childRoutes: [
      require('./projects')
    ]
  };

  ReactDOM.render(
    <Router history={history} routes={routes}/>,
    document.body.appendChild(document.createElement('div'))
  );

})();
