const route = {
  path: 'projects',

  getComponent(location, cb) {
    require.ensure([], (require) => {
      cb(null, require('./components/Projects.js'));
    });
  },

  getChildRoutes(location, cb) {
    require.ensure([], (require) => {
      cb(null, [
        require('./routes/Project')
      ]);
    });
  }
};

module.exports = route;
