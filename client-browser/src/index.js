import React from 'react';
import ReactDOM from 'react-dom';

import {Router, browserHistory} from 'react-router';

const route = {

  component: 'div',

  childRoutes: [
    {
      path: '/',

      getComponent(location, cb) {
        require.ensure([], (require) => {
          cb(null, require('./components/Application.js'));
        });
      },

      getChildRoutes(location, cb) {
        require.ensure([], (require) => {
          cb(null, [
            require('./routes/Posts')
          ]);
        });
      }
    }
  ]

};

ReactDOM.render(
  <Router history={browserHistory} routes={route}/>,
  document.body.appendChild(document.createElement('div'))
);
