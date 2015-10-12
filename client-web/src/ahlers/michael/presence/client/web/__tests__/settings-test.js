jest.dontMock('immutable');
jest.dontMock('../settings');

const Immutable = require('immutable');

describe('Settings', () => {

  beforeEach(function () {
    this.addMatchers({
      is: function (expected) {
        return Immutable.is(this.actual, expected);
      }
    });
  });

  describe('loader', () => {

    it('overrides defaults with deployment settings', () => {
      require('whatwg-fetch').setResponse({
        status: 200,
        json: function () {
          return {
            'api': {
              'host': 'production-server'
            }
          }
        }
      });

      return require('../settings').then(settings => {
        expect(settings).is(Immutable.fromJS({
          'api': {
            'host': 'production-server',
            'port': 9000
          }
        }));
      })
    });

    it('falls back on defaults on request error', () => {
      return require('../settings').then(settings => {
        expect(settings).is(Immutable.fromJS({
          'api': {
            'host': 'localhost',
            'port': 9000
          }
        }));
      })
    });

  });

});
