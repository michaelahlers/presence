import React from 'react';
import ReactDOM from 'react-dom';

import Container from './components/Container.js';

const route = {

  component: Container,

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
            require('./routes/Posts'),
            require('./routes/Projects')
          ]);
        });
      }
    }
  ]

};

/* In light of http://stackoverflow.com/q/16267339/700420 forgo using HTML5 browser history until a good solution is found to deploy to S3. See also http://rackt.org/history/stable/HashHistoryCaveats.html for details on (and caveats especially of) hash history. */

import { Router, useRouterHistory } from 'react-router';
import { createHistory } from 'history';

(async function () {

  const settings = await require('./settings');

  const history = useRouterHistory(createHistory)();

  history.listen(function (location) {
    const path = (/#(\/.*)$/.exec(location.hash) || [])[1];
    if (path) history.replace(path);
  });

  ReactDOM.render(
    <Router history={history} routes={route}/>,
    document.body.appendChild(document.createElement('div'))
  );

})();

