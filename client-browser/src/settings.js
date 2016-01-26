'use strict';

import Immutable from 'immutable';
import 'whatwg-fetch';

const defaultSettings = Immutable.Map({
  'api': {
    'host': 'localhost',
    'port': 9000
  }
});

const settings =
  fetch('/settings.json')
    .then(response => response.json())
    .then(defaultSettings.mergeDeep, error => defaultSettings);

module.exports = settings;
