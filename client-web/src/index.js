'use strict';

require('./index.less');

import React from 'react';

const content =
  <div>
    <h1>Presence</h1>
  </div>;

require('./settings').then(settings => {
  React.render(content, document.body);
});
