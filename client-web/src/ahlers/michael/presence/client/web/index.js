'use strict';

require('./index.less');

import React from 'react';
import restful from 'restful.js';

import settings from './settings';

const content =
  <div>
    <h1>Presence</h1>
  </div>;

//restful('localhost')
//  .port(9000)
//  .prefixUrl('api')
//  .all('sessions')
//  .getAll()
//  .then(function (response) {
//    console.log(response.body().map(session => session.data()));
//  });

require('./settings').then(settings => {
  console.log('settings', settings);
  React.render(content, document.body);
});
