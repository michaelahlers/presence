jest.dontMock('babel-polyfill');
jest.dontMock('immutable');
jest.dontMock('../settings');

/** See http://stackoverflow.com/a/34195553/700420. */
/* TODO: Document this more broadly. */
require('babel-polyfill');
const Immutable = require('immutable');

describe('Settings', () => {

  beforeEach(() => {
    jasmine.addMatchers({
      is(expected) {
        return Immutable.is(this.actual, expected)
      }
    });
  });

  describe('loader', () => {

    it('overrides defaults with deployment settings', async () => {
      require('whatwg-fetch').setResponse({
        status: 200,
        json() {
          return {
            'api': {
              'host': 'production-server'
            }
          }
        }
      });

      const settings = await require('../settings.js');

      expect(settings).is(Immutable.Map({
        'api': {
          'host': 'production-server',
          'port': 9000
        }
      }));
    });

    it('falls back on defaults on request error', async () => {
      const settings = await require('../settings.js');

      expect(settings).is(Immutable.Map({
        'api': {
          'host': 'localhost',
          'port': 9000
        }
      }));
    });

    it('falls back on defaults on non-JSON response', async () => {
      require('whatwg-fetch').setResponse({
        status: 200,
        json() {
          return 'Non-JSON.'
        }
      });

      expect(settings).is(Immutable.Map({
        'api': {
          'host': 'localhost',
          'port': 9000
        }
      }));
    });

  });

});
