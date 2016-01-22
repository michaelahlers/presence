import React from 'react';
import ReactDOM from 'react-dom';

import { createHistory } from 'history';

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

/* In light of http://stackoverflow.com/questions/16267339/s3-static-website-hosting-route-all-paths-to-index-html forgo using HTML5 browser history until a good solution is found to deploy to S3. See also http://rackt.org/history/stable/HashHistoryCaveats.html for details on (and caveats especially of) hash history. */
// const history = useBasename(createHistory)({});
const history = createHistory();

history.listen(function (location) {
  if (-1 < location.hash.indexOf('#/')) {
    history.replace(location.hash.substring(1));
  }
});

ReactDOM.render(
  <Router history={history} routes={route}/>,
  document.body.appendChild(document.createElement('div'))
);
