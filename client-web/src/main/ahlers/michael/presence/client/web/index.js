'use strict';

require('./index.less');

import React from 'react';
import restful from 'restful.js';

const content =
  <div>
    <h1>Presence</h1>
    <p>Hello world!</p>
  </div>;

restful('localhost')
  .port(9000)
  .prefixUrl('api')
  .all('sessions')
  .getAll()
  .then(function (response) {
    console.log(response.body().map(session => session.data()));
  });

React.render(content, document.body);
