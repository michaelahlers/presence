const route = {
  path: ':projectId',

  getComponent(location, cb) {
    require.ensure([], (require) => {
      cb(null, require('./components/Project.js'));
    });
  }
};

module.exports = route;
